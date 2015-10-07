package com.swimo.sharemoment.Adapter;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.extra.SquareImageView;
import com.swimo.sharemoment.view.fragment.ShowPublicFragment;

import java.util.ArrayList;
import java.util.List;




public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


    ImageLoader imageLoader;
    Context context;
    private List<ImagesList> mItems = null;
    private ArrayList<ImagesList> arraylist;
   // private ArrayList<String> arrayid;
    public boolean favoite=false;
    List<ParseObject> ob,ob1;
    String idobj="",idfk="";
    int device_width;
    public boolean in=true;
    int k=0;
    ProgressDialog mProgressDialog;
    public static String url,idob;
    public static ParseUser owner;
    public static int f=0;
    String username2;



    public CardAdapter(Context context,List<ImagesList> mItems) {
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
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        device_width = size.x;
        int height = size.y;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Share Moments ");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


if(mItems.get(i).getImginit()==1){
    viewHolder.fav.setVisibility(View.GONE);
    viewHolder.delfav.setVisibility(View.VISIBLE);
}else{
    viewHolder.fav.setVisibility(View.VISIBLE);
    viewHolder.delfav.setVisibility(View.GONE);

}

        imageLoader.DisplayImage(mItems.get(i).getimageurl(),
                viewHolder.imgThumbnail);
        f=mItems.get(i).getImginit();

        mProgressDialog.hide();

       /* Picasso.with(context)
                .load(mItems.get(i).getimageurl())
                .placeholder(R.drawable.ic_collections_white_18dp) // optional
                .error(R.drawable.ic_error_white_18dp)
                .into(viewHolder.imgThumbnail);*/


        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    ParseQuery<ParseObject> queryimg = new ParseQuery<ParseObject>(
                            "Images");

                    queryimg.whereEqualTo("objectId", mItems.get(i).getId());
                    ob1 = queryimg.find();
                    ParseObject imginit=ob1.get(0);
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                            "Favoris");

                    query.whereEqualTo("Imginit", imginit);
                    ob = query.find();

                        ParseObject img = new ParseObject("Favoris");

                        img.put("Imginit",imginit);
                        img.put("owner", mItems.get(i).getU());
                        img.put("imagee",mItems.get(i).getId());
                        img.put("url",mItems.get(i).getimageurl());
                        img.put("user", ParseUser.getCurrentUser());

                    img.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(
                                ParseException e) {
                            if (e == null) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
                                query.include("User");
                                query.getInBackground(mItems.get(i).getId(), new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {


                                            Log.e("test", object.getParseUser("owner").getObjectId());
                                            f = 1;
                                            viewHolder.fav.setVisibility(View.GONE);
                                            viewHolder.delfav.setVisibility(View.VISIBLE);

                                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                            installation.put("liker", ParseUser.getCurrentUser());
                                            installation.put("owner", object.getParseUser("owner"));
                                            installation.saveInBackground();


                                            ParseQuery query = ParseInstallation.getQuery();
                                            query.whereEqualTo("owner", object.getParseUser("owner"));
                                            ParsePush.sendMessageInBackground(ParseUser.getCurrentUser().getUsername()+" like your picture", query);

                                            // object will be your game score
                                        } else {
                                            Log.e("test", e.getMessage());
                                            // something went wrong
                                        }
                                    }
                                });
                                 } else {


                            }

                        }
                    });




                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }




            }
        });

        viewHolder.delfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {
                    ParseQuery<ParseObject> queryimg = new ParseQuery<ParseObject>(
                            "Images");
                    queryimg.whereEqualTo("objectId", mItems.get(i).getId());
                    ob1 = queryimg.find();
                    ParseObject imginit=ob1.get(0);
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                            "Favoris");
                    query.whereEqualTo("Imginit", imginit);
                    ob = query.find();
                    for (ParseObject img : ob) {
                        idfk=img.getObjectId();


                    }
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }




                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favoris");
                query.getInBackground(idfk, new GetCallback<ParseObject>() {
                    public void done(ParseObject img, ParseException e) {
                        if (e == null) {

                            img.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {

                                        f=0;
                                        viewHolder.fav.setVisibility(View.VISIBLE);
                                        viewHolder.delfav.setVisibility(View.GONE);


                                    } else {

                                    }
                                }
                            });


                        }
                    }
                });



            }
        });

        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,f+"",Toast.LENGTH_LONG).show();
                url=mItems.get(i).getimageurl();
                idob=mItems.get(i).getId();
                Home.dr= true;
                owner=mItems.get(i).getU();

                Fragment videoFragment = new ShowPublicFragment();
                Home.mFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, videoFragment).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public SquareImageView imgThumbnail;
        public Button fav;
        public Button delfav;



        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (SquareImageView)itemView.findViewById(R.id.img_thumbnail);
            fav=(Button)itemView.findViewById(R.id.addfav);
            delfav=(Button)itemView.findViewById(R.id.delfav);
           // download=(Button)itemView.findViewById(R.id.down);
        }
    }



}


