package com.swimo.sharemoment;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MyApplication extends MultiDexApplication implements IAviaryClientCredentials {

    /*private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;*/
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "zzg2F5c2W3aPzDNXnWBESiAETIEzRsCddUOdiPkC", "2Q1RNGzWIPMcDQhH5Jw3u6nPsKSirdGCJOQbMwty");
      //  ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return "cc492f4c44e94e4a83205752718c08bb";
    }

    @Override
    public String getClientSecret() {
        return "9782cfea-fe1a-441d-8703-9e19ad734c4a";
    }

    /*private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);

    }*/
}
