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
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.model.ImagesList;
import com.swimo.sharemoment.extra.recycleranimation.AlphaInAnimationAdapter;
import com.swimo.sharemoment.Adapter.CardAdapter;
import com.swimo.sharemoment.extra.recycleranimation.FadeInAnimator;
import com.swimo.sharemoment.extra.recycleranimation.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicFragment extends Fragment {

    View v;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
  // GridViewAdapterPublic mAdapter;
    ProgressBar pB;
    List<ImagesList> mItems;
    List<ParseObject> ob,ob1;
    ProgressDialog mProgressDialog;
    int s=0;

    public PublicFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_public, container, false);

        new RemoteDataTask().execute();




        return v;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                    "Images");
            // Locate the column named "position" in Parse.com and order list
            // by ascending
            query.include("User");
            query.whereEqualTo("previlege", true);
            ob=query.find();

                        for (ParseObject img : ob) {
                            ParseQuery<ParseObject> queryimg = new ParseQuery<ParseObject>(
                                    "Favoris");
                            // Locate the column named "position" in Parse.com and order list
                            // by ascending

                            queryimg.whereEqualTo("imagee", img.getObjectId());
                            queryimg.whereEqualTo("user",ParseUser.getCurrentUser());
                            ob1 = queryimg.find();

                            Log.e("test",img.getObjectId());
                            ParseFile image = (ParseFile) img.get("image");
                            ImagesList map = new ImagesList();
                            if(ob1.size()>0){
                                ParseObject imginit=ob1.get(0);
                                s=1;
                                ob1.remove(0);
                            }else{
                                s=0;
                            }
                            Log.e("test",s+"");
                            map.setId(img.getObjectId());

                            map.setimageurl(image.getUrl());

                            map.setImginit(s);
                            map.setU(img.getParseUser("owner"));
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
            mRecyclerView = (RecyclerView)v.findViewById(R.id.Recpublic);
            Log.e("tetetetetetetettetet",mItems.size()+":uuuuuuu");

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new CardAdapter(getActivity(),mItems);
           mRecyclerView.setItemAnimator(new FadeInAnimator());

            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);

            mRecyclerView.setAdapter(scaleAdapter);
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
