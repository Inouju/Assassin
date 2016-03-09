package edu.uw.jyinouye.assassin;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Application class that contains global state for login and auth stuff
 */
public class Assassin extends Application implements ValueEventListener {

    private static final String TAG = "Assassin";
    private static Assassin singleton;
    private Player player;
    private String groupPassword;
    private Map<String, Player> players;

    private Firebase ref;
    private Firebase groupRef;
    private Firebase.AuthResultHandler authResultHandler;
    private OnAuthenticateListener mAuthenticateListener;
    private OnJoinGroupListener mJoinGroupListener;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        player = new Player();

        //Setup firebase
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://info-498d-assassin.firebaseio.com/");

        // Create a handler to handle the result of the authentication
        authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                player.setUid(authData.getUid());
                Firebase playerRef = ref.child("players").child(authData.getUid());
                mAuthenticateListener.onLoginSuccess();
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
                Log.v(TAG, "Successfully created user account with uid: " + result.get("uid"));
                // add player to firebase players list
                //player.setUid(result.get("uid").toString());
                ref.child("players").child(result.get("uid").toString()).child("email").setValue(email);
                mAuthenticateListener.onSignUpSuccess(player.getUid());
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
        player.setEmail(email);
        ref.authWithPassword(email, password, authResultHandler);
    }

    public void joinGroup(String groupName, String groupPassword) {
        this.groupRef = ref.child("groups").child(groupName);
        this.groupPassword = groupPassword;
        player.setRef(this.groupRef);
        Log.v(TAG, "Join Group");
        // check that password is correct
        groupRef.addListenerForSingleValueEvent(this);
    }

    public void createGroup(String groupName, String groupPassword) {
        // create new groupRef, set password property
        Map<String, Object> group = new HashMap<>();
        Map<String, Object> groupDetails = new HashMap<>();
        Map<String, Object> players = new HashMap<>();
        groupDetails.put("password", groupPassword);
        groupDetails.put("players", players);
        group.put(groupName, groupDetails);
        ref.child("groups").updateChildren(group);
    }

    public void killPressed() {
        ref.orderByChild("kills");
        player.incKill();
    }

    public Firebase getRef() {
        return ref;
    }

    public Player getPlayer() {
        return player;
    }

    public Firebase getGroup() {
        return groupRef;
    }

    public void setOnAuthenticateListener(OnAuthenticateListener mListener) {
        mAuthenticateListener = mListener;
    }

    public void setOnJoinGroupListener(OnJoinGroupListener mListener) {
        mJoinGroupListener = mListener;
    }

    public void setGroupListener(ValueEventListener mListener) {
        ref.child("groups").addValueEventListener(mListener);
    }

    // callback when data in groupRef object gets changed
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.v(TAG, "Group data change: " + dataSnapshot.getValue());
        // user provides correct credentials
        if(dataSnapshot.child("password").getValue().equals(groupPassword)) {
            // reference to list of players for current groupRef
            Firebase playersRef = groupRef.child("players");
            playersRef.child(this.player.getUid()).setValue(this.player);
            mJoinGroupListener.onJoinGroupSuccess();
        } else {
            mJoinGroupListener.onJoinGroupError("Error: incorrect password");
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    public interface OnAuthenticateListener {
        void onSignUpSuccess(String uid);

        void onSignUpError(FirebaseError error);

        void onLoginSuccess();

        void onLoginError(FirebaseError error);

    }

    public interface OnJoinGroupListener {
        void onJoinGroupSuccess();

        void onJoinGroupError(String error);
    }

}
