package edu.uw.jyinouye.assassin;

import android.content.Intent;
import android.location.Location;

/**
 * Created by colec on 3/6/2016.
 */
public class Player {

    private String uid;
    private String email;
    private String groupName;
    private Location location;

    private OnPlayerUpdatedListener mPlayerUpdatedListener;

    public Player() {}

    public Player(String uid, String email, String groupName) {
        this.uid = uid;
        this.email = email;
        this.groupName = groupName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Location getLocation() { return this.location; }

    public void setLocation(Location location) {
        mPlayerUpdatedListener.onPlayerLocationChanged(location);
        this.location = location;
    }

    public void setPlayerUpdatedListener(OnPlayerUpdatedListener mListener) {
        mPlayerUpdatedListener = mListener;
    }

    //interface allows Assassin class to listen for changes in player location, send them to firebase
    public interface OnPlayerUpdatedListener {
        void onPlayerLocationChanged(Location location);
    }
}
