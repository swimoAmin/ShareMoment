package com.swimo.sharemoment.Adapter;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.extra.SquareImageView;
import com.swimo.sharemoment.view.fragment.ShowFavFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swimo on 22/09/15.
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {


    ImageLoader imageLoader;
    Context context;
    private List<ImagesList> mItems = null;
    private ArrayList<ImagesList> arraylist;

    public static String url=null,id=null;
    ProgressDialog mProgressDialog;


    public FavAdapter(Context context,List<ImagesList> mItems) {
        super();

        this.context = context;
        this.mItems = mItems;
        this.arraylist = new ArrayList<ImagesList>();
        this.arraylist.addAll(mItems);
        imageLoader = new ImageLoader(context);
        //  arrayid= new ArrayList<String>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item_fav, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        mProgressDialog = new ProgressDialog(context);
        // Set progressdialog title
        mProgressDialog.setTitle("Share Moments ");
        // Set progressdialog message
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        // Show progressdialog
        mProgressDialog.show();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {



        imageLoader.DisplayImage(mItems.get(i).getimageurl(),
                viewHolder.imgThumbnail);
        mProgressDialog.hide();
        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        url=mItems.get(i).getimageurl();
        id=mItems.get(i).getDesc();
        Home.dr=true;
        Log.e("test2",mItems.get(i).getId());

        Fragment videoFragment = new ShowFavFragment();
        Home.mFragmentManager.beginTransaction()
                .replace(R.id.frame_container, videoFragment).commit();


    }
});
       /* Picasso.with(context)
                .load(mItems.get(i).getimageurl())
                .placeholder(R.drawable.ic_collections_white_18dp) // optional
                .error(R.drawable.ic_error_white_18dp)
                .into(viewHolder.imgThumbnail);*/

       /* (new AQuery(context)).id(viewHolder.imgThumbnail).image(mItems.get(i).getimageurl(),
                true, true, device_width, R.drawable.profile, null, 0);

*/



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public SquareImageView imgThumbnail;




        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (SquareImageView)itemView.findViewById(R.id.img_thumbnail_fav);

        }
    }



}



