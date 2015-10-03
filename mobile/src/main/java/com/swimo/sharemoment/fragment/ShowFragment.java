package com.swimo.sharemoment.fragment;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.swimo.sharemoment.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.GridViewAdapter;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.Save;

import static android.graphics.Color.BLACK;
import static com.swimo.sharemoment.R.color.md_black_1000;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFragment extends Fragment {

    ImageView imgphone;
    TextView txt;
    ImageLoader imageLoader ;
    Button pub,download,delete;

    public ShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_show, container, false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");
        Home.toolbar.setBackgroundColor(Color.BLACK);
        Home.toolbar.setTitle(GridViewAdapter.desc);



// Retrieve the object by id
        query.getInBackground(GridViewAdapter.id, new GetCallback<ParseObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void done(ParseObject img, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    if (!img.getBoolean("previlege")) {


                        pub.setBackground(getResources().getDrawable(R.drawable.menu_item_selected));
                    } else {

                        pub.setBackground(getResources().getDrawable(R.drawable.menu_item_unselected));

                    }


                    img.saveInBackground();
                }
            }
        });
        imageLoader = new ImageLoader(getActivity());
         imgphone = (ImageView) v.findViewById(R.id.imageShow);
         txt = (TextView) v.findViewById(R.id.txtshow);
        pub = (Button) v.findViewById(R.id.pub);
        download = (Button) v.findViewById(R.id.download);
        delete = (Button) v.findViewById(R.id.delete);
        //Toast.makeText(getActivity(),GridViewAdapter.id+"  test",Toast.LENGTH_LONG).show();
        txt.setText(GridViewAdapter.desc);
        imageLoader.DisplayImage(GridViewAdapter.img, imgphone);

pub.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        update(GridViewAdapter.id);
    }
});
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imgphone.getDrawable();
                Bitmap b = drawable.getBitmap();
                Save s = new Save();
                s.SaveImage(getActivity(), b);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(GridViewAdapter.id);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
    public void update(String id){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");

// Retrieve the object by id
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void done(ParseObject img, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    if(!img.getBoolean("previlege")) {
                        img.put("previlege", true);
                        pub.setBackground(getResources().getDrawable(R.drawable.menu_item_selected));
                        Toast.makeText(getActivity(),GridViewAdapter.id+"  3",Toast.LENGTH_LONG).show();

                    }else {
                    img.put("previlege", false);
                        pub.setBackground(getResources().getDrawable(R.drawable.menu_item_unselected));
                        Toast.makeText(getActivity(),GridViewAdapter.id+"  4",Toast.LENGTH_LONG).show();


                    }


                    img.saveInBackground();
                }
            }
        });
    }
    public void delete(String id){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Images");

// Retrieve the object by id
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject img, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.

                    img.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Home.result.setSelection(3,true);
                            }else {
                                Toast.makeText(getActivity(),"Image not deleted, try again",Toast.LENGTH_LONG).show();
                            }
                        }
                    });



                }
            }
        });
    }


}
