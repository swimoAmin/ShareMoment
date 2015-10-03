package com.swimo.sharemoment.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.swimo.sharemoment.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.Save;
import com.swimo.sharemoment.extra.recycleranimation.CardAdapter;
import com.swimo.sharemoment.extra.recycleranimation.FavAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowPublicFragment extends Fragment {

    ImageView img;
    ImageLoader imageLoader ;
    Button download,share,fav;
    List<ParseObject> ob,ob1;
    String idfk;
    public ShowPublicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_show_public, container, false);
        Home.toolbar.setBackgroundColor(Color.BLACK);
        Home.toolbar.setTitle("");


        imageLoader = new ImageLoader(getActivity());
        img = (ImageView) v.findViewById(R.id.imageShowPub);
        share = (Button) v.findViewById(R.id.sharepub);

        download = (Button) v.findViewById(R.id.downloadPub);
        fav = (Button) v.findViewById(R.id.FavPub);
        //Toast.makeText(getActivity(),GridViewAdapter.id+"  test",Toast.LENGTH_LONG).show();
       if(CardAdapter.f==1){
            fav.setBackgroundResource(R.drawable.ic_favorite_red_a700_18dp);

        }

       imageLoader.DisplayImage(CardAdapter.url, img);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> queryimg = new ParseQuery<ParseObject>(
                        "Images");
                // Locate the column named "position" in Parse.com and order list
                // by ascending

                queryimg.whereEqualTo("objectId", CardAdapter.idob);
                try {
                    ob1 = queryimg.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ParseObject imginit = ob1.get(0);
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Favoris");
                // Locate the column named "position" in Parse.com and order list
                // by ascending

                query.whereEqualTo("Imginit", imginit);
                try {
                    ob = query.find();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (ParseObject img : ob) {
                    idfk=img.getObjectId();


                }
                if(CardAdapter.f==0){

                    // Locate the class table named "SamsungPhones" in Parse.com

                    //ParseObject imgf=ob.get(0);
                    ParseObject img = new ParseObject("Favoris");

                    img.put("Imginit", imginit);
                    img.put("owner", CardAdapter.owner);
                    img.put("imagee", imginit.getObjectId());
                    img.put("url", CardAdapter.url);
                    img.put("user", ParseUser.getCurrentUser());


                    img.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(
                                ParseException e) {
                            if (e == null) {
                                fav.setBackgroundResource(R.drawable.ic_favorite_red_a700_18dp);
                                CardAdapter.f=1;

                            } else {


                            }

                        }
                    });



            }else{
                    Toast.makeText(getActivity(),"You have this image  already in your favorite list ",Toast.LENGTH_LONG).show();
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Favoris");
                    query1.getInBackground(idfk, new GetCallback<ParseObject>() {
                        public void done(ParseObject img, ParseException e) {
                            if (e == null) {
                                // Now let's update it with some new data. In this case, only cheatMode and score
                                // will get sent to the Parse Cloud. playerName hasn't changed.

                                img.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {

                                            fav.setBackgroundResource(R.drawable.ic_favorite_outline_red_a700_18dp);
                                            CardAdapter.f=0;



                                        } else {

                                        }
                                    }
                                });


                            }
                        }
                    });
                }

            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!CardAdapter.owner.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                    Toast.makeText(getActivity(), CardAdapter.owner.getObjectId(), Toast.LENGTH_SHORT).show();

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("objectId", CardAdapter.owner.getObjectId());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), objects.size()+"", Toast.LENGTH_SHORT).show();
                                for (ParseUser ob : objects) {

                                    int p = ob.getInt("Points") + 2;
                                    ob.put("Points", p);
                                    ob.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(getActivity(), "ok3", Toast.LENGTH_SHORT).show();

                                            } else {

                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });
                                }

                            } else {
                                // Something went wrong.
                            }
                        }
                    });


                }
                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap b = drawable.getBitmap();
                Save s = new Save();
                s.SaveImage(getActivity(), b);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String fileName = "img.jpg";

                try {
                    FileOutputStream ostream = getActivity().openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_SUBJECT, "Share Moment");
                share.putExtra(Intent.EXTRA_TEXT, "Shared By Share Moments storage cloud");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getActivity().getFileStreamPath(fileName).getAbsolutePath())));
                startActivity(Intent.createChooser(share, "Share via"));


            }
        });
        return v;
    }


}
