package com.swimo.sharemoment.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.swimo.sharemoment.Home;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.CircularImageView;
import com.swimo.sharemoment.extra.GridViewAdapter;
import com.swimo.sharemoment.extra.ImageLoader;
import com.swimo.sharemoment.extra.ImagesList;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
View v;
    GridView gridview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    GridViewAdapter adapter;
    private List<ImagesList> imagesarraylist = null;
    TextView username,email;
    CircularImageView imgprofil;
    private static final int ACTION_REQUEST_GALLERY = 99;
    Button select;
    ImageLoader imageLoader;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        imageLoader = new ImageLoader(getActivity());

        select=(Button)v.findViewById(R.id.buttonselect);
        username=(TextView)v.findViewById(R.id.username);
        username.setText(Home.name);
        email=(TextView)v.findViewById(R.id.maildield);
        email.setText(Home.email);
        imgprofil=(CircularImageView)v.findViewById(R.id.imageViewcercle);

        imageLoader.DisplayImage(Home.image ,imgprofil);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();

            }
        });
        new RemoteDataTask().execute();



        // Inflate the layout for this fragment
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
            imagesarraylist = new ArrayList<ImagesList>();
            try {
                // Locate the class table named "SamsungPhones" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Images");
                // Locate the column named "position" in Parse.com and order list
                // by ascending
                query.include("user");
                query.whereEqualTo("owner", ParseUser.getCurrentUser());
                ob = query.find();
                for (ParseObject img : ob) {
                    ParseFile image = (ParseFile) img.get("image");
                    ImagesList map = new ImagesList();
                    map.setId(img.getObjectId());
                    map.setimageurl(image.getUrl());
                    map.setDesc(img.getString("description"));
                    imagesarraylist.add(map);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the gridview in gridview_main.xml
            gridview = (GridView) v.findViewById(R.id.gridview);
            // Pass the results into ListViewAdapter.java
            adapter = new GridViewAdapter(getActivity(),
                    imagesarraylist);
            // Binds the Adapter to the ListView
            gridview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_REQUEST_GALLERY && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath,bmOptions);
            bitmap = Bitmap.createScaledBitmap(bitmap,imgprofil.getWidth(),imgprofil.getHeight(),true);
            update(ParseUser.getCurrentUser().getObjectId(),bitmap);


        }
    }
    public void update(String id, final Bitmap b){
        Toast.makeText(getActivity(),"update",Toast.LENGTH_LONG).show();


        ByteArrayOutputStream streamqr = new ByteArrayOutputStream();

        b.compress(
                Bitmap.CompressFormat.PNG,
                100, streamqr);
        byte[] image = streamqr
                .toByteArray();

        ParseFile file = new ParseFile(
                "Pimg", image);
        file.saveInBackground();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("img",file);
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {

                    imgprofil.setImageBitmap(b);
                    Toast.makeText(getActivity(), "Your profile image is updated", Toast.LENGTH_LONG).show();
                    Home.image = ParseUser.getCurrentUser().getParseFile("img").getUrl();
                    Home.headerResult.updateProfile(new ProfileDrawerItem().withName(Home.name).withEmail(Home.email).withIcon(Home.getDrawableFromURL(Home.image)).withIdentifier(21));
                } else {
                    Toast.makeText(getActivity(), "Your profile image is Image not updated, try again", Toast.LENGTH_LONG).show();

                }
            }
        });


    }


}

