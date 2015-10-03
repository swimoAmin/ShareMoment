package com.swimo.sharemoment.view.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.extra.recycleranimation.AlphaInAnimationAdapter;
import com.swimo.sharemoment.extra.recycleranimation.FadeInAnimator;
import com.swimo.sharemoment.Adapter.FavAdapter;
import com.swimo.sharemoment.extra.recycleranimation.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritFragment extends Fragment {


    View v;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    // GridViewAdapterPublic mAdapter;

    List<ImagesList> mItems;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;


    public FavoritFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_favorit, container, false);

        new RemoteDataTask().execute();




        return v;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   pB=(ProgressBar)v.findViewById(R.id.fragment_images_progressfav);
            // Show progressdialog
           // pB.setVisibility(View.GONE);
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Share Moments ");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
            mItems = new ArrayList<ImagesList>();
            try{
                // Locate the class table named "SamsungPhones" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Favoris");
                // Locate the column named "position" in Parse.com and order list
                // by ascending
                query.include("User");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                ob=query.find();

                for (ParseObject img : ob) {

                    ImagesList map = new ImagesList();
                    map.setimageurl(img.getString("url"));
                    map.setDesc(img.getParseObject("owner").getObjectId());
                    map.setId(img.getObjectId());
                    mItems.add(map);

                }

            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mRecyclerView = (RecyclerView)v.findViewById(R.id.Recfav);

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new FavAdapter(getActivity(),mItems);
            mRecyclerView.setItemAnimator(new FadeInAnimator());

            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            //        scaleAdapter.setFirstOnly(false);
            //        scaleAdapter.setInterpolator(new OvershootInterpolator());
            mRecyclerView.setAdapter(scaleAdapter);
            // Close the progressdialog
            mProgressDialog.dismiss();

            /*

             mRecyclerView = (GridView)v.findViewById(R.id.Recpublic);

           // mRecyclerView.setHasFixedSize(true);

           // mLayoutManager = new GridLayoutManager(getActivity(),1);
           // mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new GridViewAdapterPublic(getActivity(),mItems);
           // mRecyclerView.setItemAnimator(new FadeInAnimator());

           // AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
           // ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            //        scaleAdapter.setFirstOnly(false);
            //        scaleAdapter.setInterpolator(new OvershootInterpolator());
            mRecyclerView.setAdapter(mAdapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            * */

        }
    }


}
