package edu.uw.jyinouye.assassin.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by iguest on 3/9/16.
 */
public class Ranking {

    private String email;
    private String userName;
    private long kills;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Ranking() {
    }

    public Ranking(String email, String userName, long kills) {
        this.email = email;
        this.userName = userName;
        this.kills = kills;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return userName;
    }

    //@JsonIgnore
    public long getKills() {
        return kills;
    }
}