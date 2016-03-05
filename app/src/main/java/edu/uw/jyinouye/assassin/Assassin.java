package edu.uw.jyinouye.assassin;

import android.app.Application;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Application class that contains global state for login and auth stuff
 */
public class Assassin extends Application {

    private static final String TAG = "Assassin";
    private static Assassin singleton;
    String id;

    private Firebase ref;
    private Firebase.AuthResultHandler authResultHandler;
    private OnAuthenticateListener mAuthenticateListener;

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
                mAuthenticateListener.onLoginSuccess(authData.getUid());
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
                Log.v(TAG, firebaseError.toString());
                mAuthenticateListener.onLoginError(firebaseError);
            }
        };
    }

    public Assassin getInstance() {
        return this.singleton;
    }

    public void signup(final String email, final String password) {
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                id = (String) result.get("uid");
                Log.v(TAG, "Successfully created user account with uid: " + id);
                mAuthenticateListener.onSignUpSuccess(id);
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Log.v(TAG, firebaseError.toString());
                mAuthenticateListener.onSignUpError(firebaseError);
            }
        });
    }

    public void login(String email, String password) {
        ref.authWithPassword(email, password, authResultHandler);
    }

    public void joinGroup(String groupName, String groupPassword) {

        ref.child("groups").push().setValue(groupName);
    }

    public void getUserInfo(){
        //
        
    }

    public void setOnAuthenticateListener(OnAuthenticateListener mListener) {
        mAuthenticateListener = mListener;
    }

    public void setGroupListener(ValueEventListener mListener) {
        ref.child("groups").addValueEventListener(mListener);
    }

    public interface OnAuthenticateListener {
        void onSignUpSuccess(String uid);

        void onSignUpError(FirebaseError error);

        void onLoginSuccess(String uid);

        void onLoginError(FirebaseError error);

    }
}
