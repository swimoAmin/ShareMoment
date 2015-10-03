package com.swimo.sharemoment.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.parse.ParseUser;
import com.swimo.sharemoment.R;
import com.swimo.sharemoment.view.fragment.ContactFragment;
import com.swimo.sharemoment.view.fragment.FavoritFragment;
import com.swimo.sharemoment.view.fragment.ProfileFragment;
import com.swimo.sharemoment.view.fragment.PublicFragment;
import com.swimo.sharemoment.view.fragment.SettingsFragment;
import com.swimo.sharemoment.view.fragment.SuggestionFragment;
import com.swimo.sharemoment.view.fragment.UploadFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Home extends AppCompatActivity {
    public static AccountHeader headerResult = null;
    public static Drawer result = null;
    ParseUser current;
    public static String name,email,image;
    public static int max;
    public static  FragmentManager mFragmentManager;
    Accueil   acc;
    public static boolean dr=false;
    public static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        current=ParseUser.getCurrentUser();
        name=current.getUsername();
        email=current.getEmail();
        max=current.getInt("Max");
        image=current.getParseFile("img").getUrl();

        Toast.makeText(getApplicationContext(),image,Toast.LENGTH_LONG).show();

        // Handle Toolbar
         toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(

                        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(getDrawableFromURL(image)).withIdentifier(21)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        result= new DrawerBuilder()
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActivity(this)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Upload").withIcon(FontAwesome.Icon.faw_cloud_upload).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Public").withIcon(FontAwesome.Icon.faw_desktop).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Profile").withIcon(FontAwesome.Icon.faw_home).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Favorit").withIcon(FontAwesome.Icon.faw_bookmark).withIdentifier(4)
                                .withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Settings").withIcon(Octicons.Icon.oct_tools).withIdentifier(5),
                        new SecondaryDrawerItem().withName("Suggestions").withIcon(Octicons.Icon.oct_dashboard).withIdentifier(6),
                        new SecondaryDrawerItem().withName("Contact").withIcon(FontAwesome.Icon.faw_send).withIdentifier(7),
                        new SecondaryDrawerItem().withName("Log Out").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(8)


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                        Fragment mFragment = null;
                        mFragmentManager = getFragmentManager();
                        switch (drawerItem.getIdentifier()) {
                            case 1:
                                mFragment = new UploadFragment();
                                setTitleActionBar("Upload Image");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;
                            case 2:
                                mFragment = new PublicFragment();
                                setTitleActionBar("Public Images");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;
                            case 3:
                                mFragment = new ProfileFragment();
                                setTitleActionBar("Profile Image");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));
                                dr=false;
                                break;
                            case 4:
                                mFragment = new FavoritFragment();
                                setTitleActionBar("Favorites Images");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;
                            case 5:
                                mFragment = new SettingsFragment();
                                setTitleActionBar("Settings");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;
                            case 6:
                                mFragment = new SuggestionFragment();
                                setTitleActionBar("Suggestion");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;
                            case 7:
                                mFragment = new ContactFragment();
                                setTitleActionBar("Contact");
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;
                            case 8:
                                ParseUser.logOut();
                                startActivity(new Intent(getApplicationContext(), Accueil.class));
                                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));

                                break;


                        }
                        if (mFragment != null) {

                            mFragmentManager.beginTransaction()
                                    .replace(R.id.frame_container, mFragment).commit();
                        }

                        return false;
                    }
                })
                .build();
           acc=new Accueil();
        if(!acc.ok)
            result.setSelection(2, true);
        else
            result.setSelection(1, true);


    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
            acc.ok=false;
        }
        else if (dr ){
            result.openDrawer();
        }
        else
        {
            super.onBackPressed();
            acc.ok=false;
        }


    }
    public void setTitleActionBar(CharSequence informacao) {
        getSupportActionBar().setTitle(informacao);
    }
    public static Drawable getDrawableFromURL(String url1) {
        try {
            URL url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Drawable d = new BitmapDrawable(Resources.getSystem(),myBitmap);
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
