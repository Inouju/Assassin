package edu.uw.jyinouye.assassin;

import android.app.Application;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Application class that contains global state for login and auth stuff
 */
public class Assassin extends Application {

    private static final String TAG = "Assassin";
    private static Assassin singleton;
    Firebase ref;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        //Setup firebase
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://info-498d-assassin.firebaseio.com/");
        //Listen for changes in auth state
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                } else {
                    // user is not logged in
                }
            }
        });
    }

    public Assassin getInstance() {
        return this.singleton;
    }


    public void login(String email, String password) {
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                Log.v(TAG, "Successfully logged in");
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
                Log.v(TAG, "Failed to login");
            }
        };
        //authorize user
        ref.authWithPassword(email, password, authResultHandler);
    }
}
