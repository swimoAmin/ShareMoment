package com.swimo.sharemoment.view.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.swimo.sharemoment.view.Accueil;
import com.swimo.sharemoment.view.Home;
import com.swimo.sharemoment.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.parse.ParseQuery.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

Button upload,discard,select;
    Bitmap bimage;
    Accueil acc;
    Home h;
    ImageView mimage;
    private static final int ACTION_REQUEST_GALLERY = 99;
    EditText desc;
    int maximages=0;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View v=inflater.inflate(R.layout.fragment_upload, container, false);


        select=(Button)v.findViewById(R.id.galerieP);
        upload=(Button)v.findViewById(R.id.uploadC);
        discard=(Button)v.findViewById(R.id.discard);
        mimage=(ImageView)v.findViewById(R.id .mimage);
        desc=(EditText)v.findViewById(R.id.desc);
        acc=new Accueil();
        desc.setVisibility(View.GONE);
        upload.setVisibility(View.GONE);
        discard.setVisibility(View.GONE);
        if(acc.ok){
            bimage=acc.btinit;
            mimage.setImageBitmap(bimage);
            desc.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
            discard.setVisibility(View.VISIBLE);
        }


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = getQuery("Images");
                query.include("User");
                query.whereEqualTo("owner", ParseUser.getCurrentUser());
                query.countInBackground(new CountCallback() {
                    public void done(int count, ParseException e) {
                        if (e == null) {

                            maximages=count;
                            Toast.makeText(getActivity(),""+count,Toast.LENGTH_LONG).show();
                            // The count request succeeded. Log the count

                        } else {
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();

                            // The request failed
                        }
                    }
                });

                    final ParseUser u = ParseUser.getCurrentUser();


                    BitmapDrawable drawableqr = (BitmapDrawable) mimage
                            .getDrawable();

                    Bitmap bitmapqr = drawableqr
                            .getBitmap();
                    int mDstWidth=400;
                   int mDstHeight=600;
                    // bitmapqr = Bitmap.createScaledBitmap(bitmapqr, mDstHeight, mDstWidth, true);


                    ByteArrayOutputStream streamqr = new ByteArrayOutputStream();
                    bitmapqr=getResizedBitmap(bitmapqr,1024);
                    bitmapqr.compress(
                            Bitmap.CompressFormat.PNG,
                            100, streamqr);
                    bitmapqr.recycle();
                    byte[] image = streamqr
                            .toByteArray();
/*

                    // Part 1: Decode image
                    Bitmap unscaledBitmap = ScalingUtilities.decodeResource2(image1,
                            mDstWidth, mDstHeight, ScalingUtilities.ScalingLogic.FIT);

                    // Part 2: Scale image
                    Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, mDstWidth,
                            mDstHeight, ScalingUtilities.ScalingLogic.FIT);
                    unscaledBitmap.recycle();
                    ByteArrayOutputStream streamqr2 = new ByteArrayOutputStream();
                    scaledBitmap.compress(
                            Bitmap.CompressFormat.PNG,
                            100, streamqr2);
                    byte[] image = streamqr2
                            .toByteArray();*/

                    ParseFile file = new ParseFile(
                            "Pimg", image);
                    file.saveInBackground();
                    ParseObject img = new ParseObject("Images");
                    img.put("description", desc.getText().toString());
                    img.put("image", file);
                    img.put("previlege", false);
                    img.put("owner", u);
                    img.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(
                                ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Image Saved", Toast.LENGTH_LONG).show();
                                desc.setText("");
                               /* ParseUser user = ParseUser.getCurrentUser();
                                user.put("Points", (user.getInt("Points")-10));
                                user.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {

                                    }
                                });*/
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Point");
                                query.whereEqualTo("owner", ParseUser.getCurrentUser());
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> scoreList, ParseException e) {
                                        if (e == null) {
                                            for(ParseObject i: scoreList){

                                                i.put("Point", i.getInt("Points")-10);
                                                i.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e!=null)
                                                            Log.e("teeeeeest",e.getMessage());
                                                        else
                                                            Log.e("teeeeeest","yess");

                                                    }
                                                });
                                            }
                                        } else {
                                            Log.d("score", "Error: " + e.getMessage());
                                        }
                                    }
                                });
                                Home.result.setSelection(2, true);
                                if (acc.ok) {
                                    Accueil.btinit = null;
                                }
                                acc.ok = false;
                                desc.setVisibility(View.GONE);
                                upload.setVisibility(View.GONE);
                                discard.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getActivity(), "Image not Saved", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }

        });


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();

            }
        });
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              h=new Home();
              desc.setText("");
              h.result.setSelection(2,true);
                if(acc.ok){
                    Accueil.btinit =null;
                }
                acc.ok=false;
                desc.setVisibility(View.GONE);
                upload.setVisibility(View.GONE);
                discard.setVisibility(View.GONE);
            }
        });

        return v;
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

        if (requestCode == ACTION_REQUEST_GALLERY  && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            mimage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            desc.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
            discard.setVisibility(View.VISIBLE);
            BitmapDrawable drawableqr = (BitmapDrawable) mimage
                    .getDrawable();

            Bitmap bitmapqr = drawableqr
                    .getBitmap();
            acc.btinit=bitmapqr;
        }


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
