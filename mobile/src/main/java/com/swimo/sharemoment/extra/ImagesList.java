package com.swimo.sharemoment.extra;

import android.support.v7.graphics.Palette;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by swimo on 13/09/15.
 */
public class ImagesList {

    private String image;
    private String desc;
    private String id;
    private Boolean privilege;
    transient private Palette.Swatch swatch;
    private float ratio;
    private int imginit;

    public int getImginit() {
        return imginit;
    }

    public void setImginit(int imginit) {
        this.imginit = imginit;
    }

    public Boolean getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Boolean privilege) {
        this.privilege = privilege;
    }

    public ParseUser getU() {
        return u;
    }

    public void setU(ParseUser u) {
        this.u = u;
    }

    private ParseUser u;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getimageurl() {
        return image;
    }

    public void setimageurl(String phone) {
        this.image = phone;
    }






    ///ziyed
    //
    //
    public Palette.Swatch getSwatch() {
        return swatch;
    }

    public void setSwatch(Palette.Swatch swatch) {
        this.swatch = swatch;
    }
    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public String getImageSrc(int screenWidth) {
        return image  + Utils.optimalImageWidth(screenWidth);
    }
    ////////

}
