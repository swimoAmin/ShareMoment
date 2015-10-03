package com.swimo.sharemoment.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.SquareImageView;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.view.fragment.ShowFragment;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ImageLoader imageLoader;
	private List<ImagesList> phonearraylist = null;
	private ArrayList<ImagesList> arraylist;
	public static String desc,img,id;

	public GridViewAdapter(Context context, List<ImagesList> phonearraylist) {
		this.context = context;
		this.phonearraylist = phonearraylist;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<ImagesList>();
		this.arraylist.addAll(phonearraylist);
		imageLoader = new ImageLoader(context);

	}

	public class ViewHolder {
		SquareImageView image;
		TextView desc;
	}

	@Override
	public int getCount() {
		return phonearraylist.size();
	}

	@Override
	public Object getItem(int position) {
		return phonearraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.gridview_item, null);
			// Locate the ImageView in gridview_item.xml
			holder.image = (SquareImageView) view.findViewById(R.id.phone);
			holder.desc = (TextView) view.findViewById(R.id.textdesc);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Load image into GridView

		imageLoader.DisplayImage(phonearraylist.get(position).getimageurl(),
				holder.image);
		if(!phonearraylist.get(position).getDesc().equals(""))
		holder.desc.setText(phonearraylist.get(position).getDesc());
		else
		holder.desc.setVisibility(View.GONE);
		// Capture GridView item click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				desc=phonearraylist.get(position).getDesc();
				img=phonearraylist.get(position).getimageurl();
				id=phonearraylist.get(position).getId();
				Home.dr=true;

				Fragment videoFragment = new ShowFragment();
				Home.mFragmentManager.beginTransaction()
						.replace(R.id.frame_container, videoFragment).commit();

				// Send single item click data to SingleItemView Class
				/*Intent intent = new Intent(context, SingleItemView.class);
				// Pass all data phone
				intent.putExtra("phone", phonearraylist.get(position)
						.getPhone());
				context.startActivity(intent);*/
			}
		});
		return view;
	}
}
