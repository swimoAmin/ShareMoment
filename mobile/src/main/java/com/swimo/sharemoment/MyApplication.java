package com.swimo.sharemoment;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;

public class MyApplication extends MultiDexApplication implements IAviaryClientCredentials {

    /*private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;*/
    @Override
    public void onCreate() {
        super.onCreate();
        //initTypeface();


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
