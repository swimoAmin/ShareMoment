package com.swimo.sharemoment.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.extra.SquareImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swimo on 19/09/15.
 */
public class GridViewAdapterPublic extends BaseAdapter {

    ImageLoader imageLoader;
    Context context;
    LayoutInflater inflater;

    private List<ImagesList> mItems = null;
    private ArrayList<ImagesList> arraylist;
    // private ArrayList<String> arrayid;
    public boolean favoite=false;
    List<ParseObject> ob;
    String idobj="",idfk="";
    int device_width;
    public boolean in=true;
    int k=0;

    public GridViewAdapterPublic(Context context, List<ImagesList> mItems) {
        super();

        this.context = context;
        this.mItems = mItems;
        this.arraylist = new ArrayList<ImagesList>();
        this.arraylist.addAll(mItems);

        imageLoader = new ImageLoader(context);
        inflater = LayoutInflater.from(context);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        device_width = size.x;


    }

    public class ViewHolder {

        public SquareImageView imgThumbnail;
        public Button fav;
        public Button delfav;



    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int i, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.recycler_view_card_item, null);
            // Locate the ImageView in gridview_item.xml
            holder.imgThumbnail = (SquareImageView)view.findViewById(R.id.img_thumbnail);
            holder.fav=(Button)view.findViewById(R.id.addfav);
            holder.delfav=(Button)view.findViewById(R.id.delfav);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        k+=1;
        if(k>mItems.size()) {
            in=false;
        }



      /*  (new AQuery(context)).id(holder.imgThumbnail).image(mItems.get(i).getimageurl(),
                true, true, device_width, R.drawable.profile, null, 0);

        Picasso.with(context)
                .load(mItems.get(i).getimageurl())
                .placeholder(R.drawable.ic_collections_white_18dp) // optional
                .error(R.drawable.ic_error_white_18dp)
                .into(holder.imgThumbnail);*/
        imageLoader.DisplayImage(mItems.get(i).getimageurl(),
                holder.imgThumbnail);
if(in) {
    try {
        // Locate the class table named "SamsungPhones" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Favoris");
        // Locate the column named "position" in Parse.com and order list
        // by ascending

        query.whereEqualTo("Image", mItems.get(i).getId());
        ob = query.find();
        for (ParseObject img : ob) {
            idobj = img.getObjectId();
            if (!idobj.equals("")) {
                //arrayid.add(i, idobj);
                holder.fav.setVisibility(View.GONE);
                holder.delfav.setVisibility(View.VISIBLE);


                idobj = "";
            }

        }

    } catch (ParseException e) {
        Log.e("Error", e.getMessage());
        e.printStackTrace();
    }


}

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ParseObject img = new ParseObject("Favoris");
                img.put("Image", mItems.get(i).getId());
                img.put("image", mItems.get(i).getimageurl());
                img.put("owner", mItems.get(i).getU());
                img.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(
                            ParseException e) {
                        if (e == null) {

                            holder.fav.setVisibility(View.GONE);
                            holder.delfav.setVisibility(View.VISIBLE);
                        } else {


                        }

                    }
                });

            }
        });

        holder.delfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Locate the class table named "SamsungPhones" in Parse.com
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                            "Favoris");
                    // Locate the column named "position" in Parse.com and order list
                    // by ascending

                    query.whereEqualTo("Image", mItems.get(i).getId());
                    ob = query.find();
                    for (ParseObject img : ob) {
                        idfk = img.getObjectId();


                    }
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favoris");
                query.getInBackground(idfk, new GetCallback<ParseObject>() {
                    public void done(ParseObject img, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.

                            img.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {


                                        holder.fav.setVisibility(View.VISIBLE);
                                        holder.delfav.setVisibility(View.GONE);


                                    } else {

                                    }
                                }
                            });


                        }
                    }
                });


            }
        });

      /*  viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) viewHolder.imgThumbnail.getDrawable();
                Bitmap b = drawable.getBitmap();
                Save s = new Save();
                s.SaveImage(context, b);
            }
        });*/        return view;
    }
}
