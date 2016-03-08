package edu.uw.jyinouye.assassin;

/**
 * Created by colec on 3/6/2016.
 */
public class Player {

    private String uid;
    private String email;
    private String groupName;

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
}
