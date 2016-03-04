package edu.uw.jyinouye.assassin;

import android.app.Application;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Application class that contains global state for login and auth stuff
 */
public class Assassin extends Application {

    private static final String TAG = "Assassin";
    private static Assassin singleton;

    private Firebase ref;
    private Firebase.AuthResultHandler authResultHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        //Setup firebase
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://info-498d-assassin.firebaseio.com/");

        // Create a handler to handle the result of the authentication
        authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                Log.v(TAG, "Authenticated");
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
                Log.v(TAG, firebaseError.toString());
            }
        };
    }

    public Assassin getInstance() {
        return this.singleton;
    }

    public void signup(String email, String password) {
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.v(TAG, "Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Log.v(TAG, firebaseError.toString());
            }
        });
    }

    public void login(String email, String password) {
        ref.authWithPassword(email, password, authResultHandler);
    }

    public void joinGroup(String groupName, String groupPassword) {

        ref.child("groups").push().setValue(groupName);
    }
}
