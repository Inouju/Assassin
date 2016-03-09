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
    private String groupName;
    private Location location;
    private int kills;
    private int deaths;
    private int currency;

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
    }

    public void setRef(Firebase ref) {
        groupRef = ref;
    }

    public void incKill() {
        kills = kills + 1;
        currency = currency + 5;
    }

    public void incDeath() { deaths = deaths + 1; }

    public int getCurrency() { return currency; }

    public int getKills() { return kills; }

    public int getDeaths() { return deaths; }

    public String getUid() { return uid; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTargetuid() { return targetuid; }

    public void setTargetuid(String tuid) {
        this.targetuid = tuid;
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
        Log.v("PlayerObject", "set location");
        Map<String, Object> loc = new HashMap<>();
        loc.put("lat", location.getLatitude());
        loc.put("lng", location.getLongitude());
        groupRef.child("players").child(uid).child("location").updateChildren(loc);
        this.location = location;
    }

    //interface allows Assassin class to listen for changes in player location, send them to firebase
    public interface OnPlayerUpdatedListener {
        void onPlayerLocationChanged(Location location);
    }
}
