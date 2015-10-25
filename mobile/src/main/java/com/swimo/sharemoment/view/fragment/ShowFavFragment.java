package com.swimo.sharemoment.view.fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.swimo.sharemoment.Adapter.CardAdapter;
import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.Save;
import com.swimo.sharemoment.Adapter.FavAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFavFragment extends Fragment {

    ImageView img;
    ImageLoader imageLoader ;
    Button download,delete;
    ParseUser owner;

    public ShowFavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v=inflater.inflate(R.layout.fragment_show_fav, container, false);
        Home.toolbar.setBackgroundColor(Color.BLACK);
        Home.toolbar.setTitle("");


        imageLoader = new ImageLoader(getActivity());
        img = (ImageView) v.findViewById(R.id.imageShowfav);

        download = (Button) v.findViewById(R.id.downloadfav);
        delete = (Button) v.findViewById(R.id.deletefav);
        //Toast.makeText(getActivity(),GridViewAdapter.id+"  test",Toast.LENGTH_LONG).show();
        imageLoader.DisplayImage(FavAdapter.url, img);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), FavAdapter.id, Toast.LENGTH_LONG).show();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Favoris");
                query.getInBackground(FavAdapter.id, new GetCallback<ParseObject>() {
                    public void done(ParseObject img, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.

                            img.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {


                                        if(e==null){
                                            Home.result.setSelection(4,true);
                                        }else {
                                            Toast.makeText(getActivity(), "Image not deleted, try again", Toast.LENGTH_LONG).show();
                                        }


                                    } else {

                                    }
                                }
                            });


                        }
                    }
                });

            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!FavAdapter.id.equals(ParseUser.getCurrentUser().getObjectId())) {
                    Toast.makeText(getActivity(), CardAdapter.owner.getObjectId(), Toast.LENGTH_SHORT).show();

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("objectId",FavAdapter.id);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), objects.size() + "", Toast.LENGTH_SHORT).show();
                                for (ParseUser ob : objects) {
                                    owner = ob;

                                }

                            } else {
                                // Something went wrong.
                            }
                        }
                    });

                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Point");
                    query1.whereEqualTo("owner", owner);
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> scoreList, ParseException e) {
                            if (e == null) {

                                for (ParseObject ob : scoreList) {
                                    int p = ob.getInt("pointslead") + 2;
                                    ob.put("pointslead", p);
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


                                Log.d("score", "Retrieved " + scoreList.size() + " scores");
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
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
        return v;
    }


}
