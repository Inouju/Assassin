package edu.uw.jyinouye.assassin;

import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by colec on 3/6/2016.
 */
public class Player {

    private Firebase groupRef;

    private String uid;
    private String targetuid;
    private String email;
    private String userName;
    private String groupName;
    private Location location;
    private double latitude;
    private double longitude;
    private long kills;
    private long deaths;
    private long currency;
    private boolean isAdmin;
    public boolean isPlaying;

    private OnPlayerUpdatedListener mPlayerUpdatedListener;

    public Player() {}

    public Player(String uid, String email, String groupName) {

        this.uid = uid;
        this.email = email;
        this.groupName = groupName;
        this.kills = 0;
        this.deaths = 0;
        this.currency = 0;
        this.targetuid = "YOU!";
        this.isPlaying = true;
    }

    public void setRef(Firebase ref) {
        groupRef = ref;
    }

    public void incKill() {
        kills = kills + 1;
        currency = currency + 5;
    }

    public void setisPlaying(boolean isPlaying2) {
        isPlaying = isPlaying2;
    }

    public void incDeath() { deaths = deaths + 1; }

    public long getCurrency() { return currency; }

    public long getKills() { return kills; }

    public long getDeaths() { return deaths; }

    public String getUid() { return uid; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTargetuid() { return targetuid; }

    public void setTargetuid(String tuid) {
        groupRef.child("players").child(uid).child("targetUid").setValue(tuid);
        this.targetuid = tuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public void setCurrency(long currency) {
        this.currency = currency;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLocation(Location location) {
        Log.v("PlayerObject", "set location: " + location.getLatitude() + " " + location.getLongitude());
        groupRef.child("players").child(uid).child("latitude").setValue((double)location.getLatitude());
        groupRef.child("players").child(uid).child("longitude").setValue((double)location.getLongitude());
        this.location = location;
    }

    //interface allows Assassin class to listen for changes in player location, send them to firebase
    public interface OnPlayerUpdatedListener {
        void onPlayerLocationChanged(Location location);
    }
}
