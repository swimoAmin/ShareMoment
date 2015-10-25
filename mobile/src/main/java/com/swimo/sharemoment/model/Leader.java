package com.swimo.sharemoment.model;

/**
 * Created by swimo on 21/10/15.
 */
public class Leader {
    public String url;
    public String username;
    public String points;
    public boolean me;

    public Leader(String url, String username, String points, boolean me) {
        this.url = url;
        this.username = username;
        this.points = points;
        this.me = me;
    }

    public Leader() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }
}
