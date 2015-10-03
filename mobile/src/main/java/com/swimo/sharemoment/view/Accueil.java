package com.swimo.sharemoment.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import com.aviary.android.feather.sdk.internal.utils.DecodeUtils;
import com.aviary.android.feather.sdk.internal.utils.ImageInfo;
import com.aviary.android.feather.sdk.utils.AviaryIntentConfigurationValidator;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.extra.Connectivity;
import com.swimo.sharemoment.extra.Save;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Accueil extends Activity {
    public static final String LOG_TAG = Accueil.class.getName();
    private static final int ACTION_REQUEST_GALLERY = 99;
    private static final int ACTION_REQUEST_FEATHER = 100;
    private static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;
    /** Folder name on the sdcard where the images will be saved * */
    private static final String FOLDER_NAME = "Share Moment";
    ImageButton mGalleryButton;
    ImageButton mEditButton;
    ImageButton take;
    ImageButton share,save;
    ImageView mImage,initb,cloud,dash;
    ShareButton FBshare;
    View mImageContainer;
    String mOutputFilePath;
    Uri mImageUri;
    int imageWidth, imageHeight;
    File mGalleryFolder;
    public static Bitmap btinit;
    Button Facebook,signup,cancel;
    EditText log,pass,mail;
    RelativeLayout loglayout;
    public static  boolean ok=false;
    boolean si=false;

    ParseUser current=null;
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGalleryButton = (ImageButton) findViewById(R.id.Pick);
        mEditButton = (ImageButton) findViewById(R.id.Edit);
        take = (ImageButton) findViewById(R.id.Take);
        share = (ImageButton) findViewById(R.id.Share);
        mImage = (ImageView) findViewById(R.id.image);
        mImageContainer = findViewById(R.id.image_container);
        cloud = (ImageButton) findViewById(R.id.grayscale);
        initb = (ImageButton) findViewById(R.id.init);
        save = (ImageButton) findViewById(R.id.Save);
        FBshare = (ShareButton) findViewById(R.id.reflection);
        Facebook=(Button)findViewById(R.id.button1fb);
        dash=(ImageButton)findViewById(R.id.dashbord);

        signup=(Button)findViewById(R.id.signup);
        cancel=(Button)findViewById(R.id.buttoncancel);
        log=(EditText)findViewById(R.id.log);
        pass=(EditText)findViewById(R.id.pass);
        mail=(EditText)findViewById(R.id.email);



        loglayout=(RelativeLayout)findViewById(R.id.login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
       // ParseFacebookUtils.initialize(getApplicationContext());

        setContentView(R.layout.activity_accueil);
        Parse.initialize(this, "zzg2F5c2W3aPzDNXnWBESiAETIEzRsCddUOdiPkC", "2Q1RNGzWIPMcDQhH5Jw3u6nPsKSirdGCJOQbMwty");



        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = (int) ((float) metrics.widthPixels );
        imageHeight = (int) ((float) metrics.heightPixels );

        initb.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      mImage.setImageBitmap(btinit);
                    }
                });
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        BitmapDrawable drawable = (BitmapDrawable) mImage.getDrawable();
                        Bitmap b = drawable.getBitmap();
                        Save s= new Save();
                        s.SaveImage(getApplicationContext(), b);
                    }
                });
        mGalleryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickFromGallery();

                    }
                });

        mEditButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* BitmapDrawable drawable = (BitmapDrawable) mImage.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        String fileName="img.jpg";

                        try
                        {
                            FileOutputStream ostream = getApplicationContext().openFileOutput( fileName, Context.MODE_WORLD_READABLE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }*/

                        if (mImageUri != null) {
                            //startFeather(Uri.fromFile(new File(getApplicationContext().getFileStreamPath(fileName).getAbsolutePath())));
                            startFeather(mImageUri);
                        }
                    }
                });
        share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BitmapDrawable drawable = (BitmapDrawable) mImage.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        String fileName="img.jpg";

                        try
                        {
                            FileOutputStream ostream = getApplicationContext().openFileOutput( fileName, Context.MODE_WORLD_READABLE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.close();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_SUBJECT, "Share Moment");
                        share.putExtra(Intent.EXTRA_TEXT, "Edited by me using  \" Share Moments \" image editor !");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getApplicationContext().getFileStreamPath(fileName).getAbsolutePath())));
                        startActivity(Intent.createChooser(share, "Share via"));
                                           }
                });
        take.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE), 37);
                    }
                });


FBshare.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        BitmapDrawable drawable = (BitmapDrawable) mImage.getDrawable();

        Bitmap image = drawable.getBitmap();
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        //ShareApi.share(content, null);

        FBshare.setShareContent(content);
    }
});
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               current=ParseUser.getCurrentUser();
                ok=true;
                BitmapDrawable drawable = (BitmapDrawable) mImage.getDrawable();

                btinit= drawable.getBitmap();
                if(current==null)
                    loglayout.setVisibility(View.VISIBLE);
                else {


                    startActivity(new Intent(getApplicationContext(), Home.class));
                }

            }
        });
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current=ParseUser.getCurrentUser();

                if(current==null)
                    loglayout.setVisibility(View.VISIBLE);
                else {


                    startActivity(new Intent(getApplicationContext(), Home.class));
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(si==true){
                    si=false;
                    mail.setVisibility(View.GONE);
                    Facebook.setVisibility(View.VISIBLE);
                }else {

                    loglayout.setVisibility(View.GONE);
                }

            }
        });
        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(!si){
                 si=true;
                 mail.setVisibility(View.VISIBLE);
                 Facebook.setVisibility(View.GONE);

             }  else {

                 if (Connectivity.isConnected(getApplicationContext())) {

                     Validate emailField = new Validate(mail);
                     Validate passField = new Validate(pass);
                     Validate logi = new Validate(log);

                     emailField.addValidator(new NotEmptyValidator(getApplicationContext()));
                     passField.addValidator(new NotEmptyValidator(getApplicationContext()));
                     logi.addValidator(new NotEmptyValidator(getApplicationContext()));
                     emailField.addValidator(new EmailValidator(getApplicationContext()));

                     Form mForm = new Form();
                     mForm.addValidates(emailField);
                     mForm.addValidates(passField);
                     mForm.addValidates(logi);


                     if (mForm.validate()) {
                         final ParseUser user = new ParseUser();
                         user.setUsername(log.getText().toString());
                         user.setPassword(pass.getText().toString());
                         user.setEmail(mail.getText().toString());
                        // user.put("emailVerified",false);
                         //user.put("Max", 20);
                        // user.put("Points",1000);

                         //changes here

                         user.signUpInBackground(new SignUpCallback() {
                             public void done(ParseException e) {
                                 if (e == null) {
                                     si=false;
                                     mail.setVisibility(View.GONE);
                                     Facebook.setVisibility(View.VISIBLE);
                                     BitmapDrawable drawableqr = (BitmapDrawable) getResources().getDrawable(R.drawable.profile4);


                                     Bitmap bitmapqr = drawableqr
                                             .getBitmap();

                                     ByteArrayOutputStream streamqr = new ByteArrayOutputStream();

                                     bitmapqr.compress(
                                             Bitmap.CompressFormat.PNG,
                                             100, streamqr);
                                     byte[] image = streamqr
                                             .toByteArray();

                                     ParseFile file = new ParseFile(
                                             "Pimg", image);
                                     file.saveInBackground();
                                     user.put("img", file);
                                   /*  ParseACL groupACL = new ParseACL();
                                     groupACL.setReadAccess(user, true);
                                     groupACL.setWriteAccess(user, true);
                                     user.setACL(groupACL);*/
                                     user.saveInBackground();
                                     ParseObject p = new ParseObject("Point");
                                     p.put("points", 1000);
                                     p.put("owner", user);
                                     p.saveInBackground();


                                     // Hooray! Let them use the app now.
                                 } else {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                     // Sign up didn't succeed. Look at the ParseException
                                     // to figure out what went wrong
                                 }
                             }
                         });

                     }
                 }
             }
            }
        });




        mGalleryFolder = createFolders();
        registerForContextMenu(mImageContainer);

        // pre-load the cds service
        Intent cdsIntent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(cdsIntent);

        printConfiguration();

        // verify the CreativeSDKImage configuration
        try {
            AviaryIntentConfigurationValidator.validateConfiguration(this);
        } catch (Throwable e) {
            new AlertDialog.Builder(this).setTitle("Error")
                    .setMessage(e.getMessage()).show();
        }






    }


    public void onLoginButtonClicked() {
        if (Connectivity.isConnected(getApplicationContext())) {

            Validate passField = new Validate(pass);
            Validate logi = new Validate(log);


            passField.addValidator(new NotEmptyValidator(getApplicationContext()));
            logi.addValidator(new NotEmptyValidator(getApplicationContext()));


            Form mForm = new Form();

            mForm.addValidates(passField);
            mForm.addValidates(logi);


            if (mForm.validate()) {

                ParseUser.logInInBackground(log.getText().toString(), pass.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {

                        if (user != null) {

                           Boolean x= ParseUser.getCurrentUser().getBoolean("emailVerified");
                            if(x) {
                                loglayout.setVisibility(View.GONE);
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(),  "verify your email first", Toast.LENGTH_LONG).show();

                            }

                        } else {

                            Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent() != null) {
            handleIntent(getIntent());
            setIntent(new Intent());
        }
    }

    /**
     * Handle the incoming {@link Intent}
     */
    private void handleIntent(Intent intent) {
        final String action = intent.getAction();

        if (null != action) {
            if (Intent.ACTION_SEND.equals(action)) {
                Bundle extras = intent.getExtras();
                if (extras != null && extras.containsKey(Intent.EXTRA_STREAM)) {
                    Uri uri = (Uri) extras.get(Intent.EXTRA_STREAM);
                    loadAsync(uri);
                }
            } else if (Intent.ACTION_VIEW.equals(action)) {
                Uri data = intent.getData();
                loadAsync(data);
            }
        }
    }

    /**
     * Load the incoming Image
     *
     * @param uri
     */
    private void loadAsync(final Uri uri) {
        Drawable toRecycle = mImage.getDrawable();
        if (toRecycle != null && toRecycle instanceof BitmapDrawable) {
            if (((BitmapDrawable) mImage.getDrawable()).getBitmap() != null) {
                ((BitmapDrawable) mImage.getDrawable()).getBitmap().recycle();
            }
        }
        mImage.setImageDrawable(null);
        mImageUri = null;

        DownloadAsync task = new DownloadAsync();
        task.execute(uri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOutputFilePath = null;
    }

    /**
     * Delete a file without throwing any exception
     *
     * @param path
     * @return
     */
    private boolean deleteFileNoThrow(String path) {
        File file;
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            return false;
        }

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    @Override
    /**
     * This method is called when feather has completed ( ie. user clicked on "done" or just exit the activity without saving ).
     * <br />
     * If user clicked the "done" button you'll receive RESULT_OK as resultCode, RESULT_CANCELED otherwise.
     *
     * @param requestCode
     * 	- it is the code passed with startActivityForResult
     * @param resultCode
     * 	- result code of the activity launched ( it can be RESULT_OK or RESULT_CANCELED )
     * @param data
     * 	- the result data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:
                    // user chose an image from the gallery
                    loadAsync(data.getData());

                    break;

                case ACTION_REQUEST_FEATHER:

                    boolean changed = true;

                    if (null != data) {
                        Bundle extra = data.getExtras();
                        if (null != extra) {
                            // image was changed by the user?
                            changed = extra
                                    .getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
                        }
                    }

                    if (!changed) {

                    }

                    // send a notification to the media scanner
                    updateMedia(mOutputFilePath);

                    // update the preview with the result
                    loadAsync(data.getData());
                    mOutputFilePath = null;

                    break;
                case 37:
                    Bitmap bp = (Bitmap) data.getExtras().get("data");
                    mImage.setImageBitmap(bp);
                    Uri u = data.getData();
                    mEditButton.setEnabled(true);
                    setImageURI(u, bp);

                    btinit = bp;

                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case ACTION_REQUEST_FEATHER:

                    // delete the result file, if exists
                    if (mOutputFilePath != null) {
                        deleteFileNoThrow(mOutputFilePath);
                        mOutputFilePath = null;

                    }
                    break;


            }
        }
    }

    /**
     * Given an Uri load the bitmap into the current ImageView and resize it to
     * fit the image container size
     *
     * @param uri
     */
    @SuppressWarnings ("deprecation")
    private boolean setImageURI(final Uri uri, final Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
        mImage.setBackgroundDrawable(null);

        mEditButton.setEnabled(true);
        mImageUri = uri;

        return true;
    }

    /**
     * We need to notify the MediaScanner when a new file is created. In this
     * way all the gallery applications will be notified too.
     *
     * @param filepath
     */
    private void updateMedia(String filepath) {
        MediaScannerConnection.scanFile(
                getApplicationContext(),
                new String[]{filepath}, null, null);
    }

    /**
     * Pick a random image from the user gallery
     *
     * @return
     */
    @SuppressWarnings ("unused")
    private Uri pickRandomImage() {
        Cursor c = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA},
                MediaStore.Images.ImageColumns.SIZE + ">?", new String[]{"90000"}, null);
        Uri uri = null;

        if (c != null) {
            int total = c.getCount();
            int position = (int) (Math.random() * total);
            if (total > 0) {
                if (c.moveToPosition(position)) {
                    String data = c.getString(
                            c.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    long id = c.getLong(
                            c.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                    uri = Uri.parse(data);
                }
            }
            c.close();
        }
        return uri;
    }

    /**
     * Return a new image file. Name is based on the current time. Parent folder
     * will be the one created with createFolders
     *
     * @return
     * @see #createFolders()
     */
    private File getNextFileName() {
        if (mGalleryFolder != null) {
            if (mGalleryFolder.exists()) {
                File file = new File(
                        mGalleryFolder, "aviary_"
                        + System.currentTimeMillis() + ".jpg");
                return file;
            }
        }
        return null;
    }

    /**
     * Once you've chosen an image you can start the feather activity
     *
     * @param uri
     */
    @SuppressWarnings ("deprecation")
    private void startFeather(Uri uri) {

Toast.makeText(getApplicationContext(),""+uri,Toast.LENGTH_LONG).show();
        // first check the external storage availability
        if (!isExternalStorageAvailable()) {
            showDialog(EXTERNAL_STORAGE_UNAVAILABLE);
            return;
        }

        // create a temporary file where to store the resulting image
        File file = getNextFileName();

        if (null != file) {
            mOutputFilePath = file.getAbsolutePath();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(android.R.string.dialog_alert_title)
                    .setMessage("Failed to create a new File").show();
            return;
        }

        // Create the intent needed to start feather
        Intent newIntent = new AviaryIntent.Builder(this).setData(uri)
                .withOutput(Uri.parse("file://" + mOutputFilePath))
                .withOutputFormat(Bitmap.CompressFormat.JPEG)
                .withOutputSize(MegaPixels.Mp5).withNoExitConfirmation(true)
                .saveWithNoChanges(true).withPreviewSize(1024)
                .build();

        // ..and start feather
        startActivityForResult(newIntent, ACTION_REQUEST_FEATHER);

    }

    /**
     * Check the external storage status
     *
     * @return
     */
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Start the activity to pick an image from the user gallery
     */
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
    }

    private File createFolders() {
        File baseDir;

        if (android.os.Build.VERSION.SDK_INT < 8) {
            baseDir = Environment.getExternalStorageDirectory();
        } else {
            baseDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }

        if (baseDir == null) {
            return Environment.getExternalStorageDirectory();
        }

        File aviaryFolder = new File(baseDir, FOLDER_NAME);

        if (aviaryFolder.exists()) {
            return aviaryFolder;
        }
        if (aviaryFolder.mkdirs()) {
            return aviaryFolder;
        }

        return Environment.getExternalStorageDirectory();
    }

    class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements
            DialogInterface.OnCancelListener {
        ProgressDialog mProgress;
        private Uri mUri;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgress = new ProgressDialog(Accueil.this);
            mProgress.setIndeterminate(true);
            mProgress.setCancelable(true);
            mProgress.setMessage("Loading image...");
            mProgress.setOnCancelListener(this);
            mProgress.show();
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            mUri = params[0];

            Bitmap bitmap = null;

           while (1 > mImageContainer.getWidth()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            final int w = mImageContainer.getWidth();
            ImageInfo info = new ImageInfo();
            bitmap = DecodeUtils.decode(
                    Accueil.this, mUri, imageWidth,
                    imageHeight, info);
            btinit=bitmap;
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (mProgress.getWindow() != null) {
                mProgress.dismiss();
            }

            if (result != null) {
                setImageURI(mUri, result);
            } else {
                Toast.makeText(
                        Accueil.this,
                        "Failed to load image " + mUri, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }

    private void printConfiguration() {

    }


public void method(boolean x,ImageView v1,ImageView v2,ImageView v3,ImageView v4,ImageView v5){

    if(x==true){
        v2.setClickable(false);
        v3.setClickable(false);
        v4.setClickable(false);
        v5.setClickable(false);

    }else{
        v1.setClickable(true);
        v2.setClickable(true);
        v3.setClickable(true);
        v4.setClickable(true);
        v5.setClickable(true);

    }

}
    public void method1(boolean x,ImageView v1,ImageView v2,ImageView v3,ImageView v4,ImageView v5,ImageView v6,ImageView v7,ImageView v8){

        if(x==true){
            v2.setClickable(false);
            v3.setClickable(false);
            v4.setClickable(false);
            v5.setClickable(false);
            v6.setClickable(false);
            v7.setClickable(false);
            v8.setClickable(false);

        }else{
            v1.setClickable(true);
            v2.setClickable(true);
            v3.setClickable(true);
            v4.setClickable(true);
            v5.setClickable(true);
            v6.setClickable(true);
            v7.setClickable(true);
            v8.setClickable(true);

        }

    }
}