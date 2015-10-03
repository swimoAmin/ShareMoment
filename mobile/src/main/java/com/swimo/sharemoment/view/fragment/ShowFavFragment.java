package com.swimo.sharemoment.view.fragment;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.Save;
import com.swimo.sharemoment.Adapter.FavAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFavFragment extends Fragment {

    ImageView img;
    ImageLoader imageLoader ;
    Button download,delete;
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
                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap b = drawable.getBitmap();
                Save s = new Save();
                s.SaveImage(getActivity(), b);
            }
        });
        return v;
    }


}
