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
import com.swimo.sharemoment.view.Home;

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
   public String name="mohamed";
    public  String url="test";
    public  String idobj="hhh";
    public Leader map;


    public LeaderBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_favorit, container, false);

       /* mItems = new ArrayList<Leader>();
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Point");
            query.include("User");
            query.orderByDescending("pointslead");
            ob = query.find();
             map = new Leader();

            for (final ParseObject img : ob) {
                Log.e("1", img.getInt("pointslead") + "");
                map.setpLead(img.getInt("pointslead"));

                mItems.add(map);
                //new RemoteDataTask(img.getParseUser("owner").getObjectId()).execute();

            }

        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }*/





        mRecyclerView = (RecyclerView) v.findViewById(R.id.Recfav);


        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LeaderAdapter(getActivity());
        mRecyclerView.setItemAnimator(new FadeInAnimator());

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //        scaleAdapter.setFirstOnly(false);
        //        scaleAdapter.setInterpolator(new OvershootInterpolator());
        mRecyclerView.setAdapter(scaleAdapter);
        // Close the progressdialog



        return v;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        public String g;
        public RemoteDataTask(String g){
          this.g=g;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create the array
           /* mItems = new ArrayList<Leader>();
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Point");
                query.include("User");
                query.orderByDescending("pointslead");
                ob = query.find();
                final Leader map = new Leader();

                for (final ParseObject img : ob) {
                    Log.e("1", img.getInt("pointslead") + "");
                    map.setpLead(img.getInt("pointslead"));*/

                    ParseQuery<ParseUser> queryuser = ParseUser.getQuery();
                    queryuser.getInBackground(g, new GetCallback<ParseUser>() {
                        public void done(ParseUser object, ParseException e) {
                            if (e == null) {
                                Log.e("--:--","-- yes --");
                                name = object.getUsername();
                                url = object.getParseFile("img").getUrl();
                                idobj = object.getObjectId();

                                Log.e("--:--",name);
                                Log.e("--:--",url);
                                Log.e("--:--",idobj);
                                map.setUsername(name);
                                map.setUrl(url);
                                if(ParseUser.getCurrentUser().getObjectId().equals(idobj)){
                                    map.setMe(true);
                                }else {
                                    map.setMe(false);
                                }
                                mItems.add(map);


                            } else {
                                Log.e("Error", e.getMessage());
                            }
                        }
                    });


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mRecyclerView = (RecyclerView) v.findViewById(R.id.Recfav);

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new LeaderAdapter(getActivity());
            mRecyclerView.setItemAnimator(new FadeInAnimator());

            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            //        scaleAdapter.setFirstOnly(false);
            //        scaleAdapter.setInterpolator(new OvershootInterpolator());
            mRecyclerView.setAdapter(scaleAdapter);
            // Close the progressdialog



        }
    }

}