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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.swimo.sharemoment.Adapter.FavAdapter;
import com.swimo.sharemoment.Adapter.LeaderAdapter;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.recycleranimation.AlphaInAnimationAdapter;
import com.swimo.sharemoment.extra.recycleranimation.FadeInAnimator;
import com.swimo.sharemoment.extra.recycleranimation.ScaleInAnimationAdapter;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.model.Leader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoardFragment extends Fragment {
    View v;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    // GridViewAdapterPublic mAdapter;

    List<Leader> mItems;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;


    public LeaderBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_favorit, container, false);

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
            mItems = new ArrayList<Leader>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Point");
                query.include("User");
                query.orderByDescending("pointslead");
                ob = query.find();

                for (ParseObject img : ob) {
                    ParseQuery<ParseUser> queryuser = ParseUser.getQuery();
                    queryuser.getInBackground(img.getParseUser("owner").getObjectId(), new GetCallback<ParseUser>() {
                        public void done(ParseUser object, ParseException e) {
                            if (e == null) {
                                Leader map = new Leader();
                               // map.setPoints(img.getString("pointslead"));
                                map.setUsername(object.getUsername());
                                map.setUrl(object.getParseFile("img").getUrl());
                               // Log.e("1", img.getString("pointslead"));
                                Log.e("2",object.getUsername());
                                Log.e("3",object.getParseFile("img").getUrl());
                                if(ParseUser.getCurrentUser().getObjectId().equals(object.getObjectId())){
                                   map.setMe(true);
                                }else {
                                   map.setMe(false);
                                }
                                mItems.add(map);
                                Log.e("tetetetetetetettetet", mItems.size() + ":kkkkkk");

                            } else {
                                Log.e("Error", e.getMessage());
                            }
                        }
                    });



                }

            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mRecyclerView = (RecyclerView) v.findViewById(R.id.Recfav);
            Log.e("tetetetetetetettetet",mItems.size()+":uuuuuuu");

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new LeaderAdapter(getActivity(), mItems);
            mRecyclerView.setItemAnimator(new FadeInAnimator());

            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            //        scaleAdapter.setFirstOnly(false);
            //        scaleAdapter.setInterpolator(new OvershootInterpolator());
            mRecyclerView.setAdapter(scaleAdapter);
            // Close the progressdialog
            mProgressDialog.dismiss();



        }
    }
}