package edu.uw.jyinouye.assassin;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.BooleanNode;

import java.util.HashMap;
import java.util.Map;

import edu.uw.jyinouye.assassin.util.Ranking;

/**
 * Application class that contains global state for login and auth stuff
 */
public class Assassin extends Application implements ValueEventListener {

    private static final String TAG = "Assassin";
    private static Assassin singleton;
    private Player mPlayer;
    private String groupPassword;
    private Map<String, Player> players;

    private Firebase ref;
    private Firebase groupRef;
    private Firebase.AuthResultHandler authResultHandler;
    private OnAuthenticateListener mAuthenticateListener;
    private Firebase globalPlayerRef;
    private OnJoinGroupListener mJoinGroupListener;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        mPlayer = new Player();

        //Setup firebase
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://info-498d-assassin.firebaseio.com/");

        // Create a handler to handle the result of the authentication
        authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                Log.v(TAG, "Authed with " + authData.getUid());
                mPlayer.setUid(authData.getUid());
                globalPlayerRef = ref.child("players").child(authData.getUid());

                // get username
                globalPlayerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Object userName = snapshot.child("userName").getValue();
                        if (userName != null) {
                            mPlayer.setUserName(userName.toString());
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

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

    public void signup(final String email, final String password, final String userName) {
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Log.v(TAG, "Successfully created user account with uid: " + result.get("uid"));
                // add mPlayer to firebase players list
                mPlayer.setUid(result.get("uid").toString());
                mPlayer.setEmail(email);
                mPlayer.setUserName(userName);

                // adds to update global players here
                Ranking newRank = new Ranking(mPlayer.getEmail(), mPlayer.getUserName(), mPlayer.getKills());
                ref.child("players").child(result.get("uid").toString()).setValue(newRank);
//                ref.child("players").child(result.get("uid").toString()).child("userName").setValue(mPlayer.getUserName());
//                ref.child("players").child(result.get("uid").toString()).child("kills").setValue(mPlayer.getKills());

                mAuthenticateListener.onSignUpSuccess(mPlayer.getUid());
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
        mPlayer.setEmail(email);
        ref.authWithPassword(email, password, authResultHandler);
    }

    public void joinGroup(String groupName, String groupPassword) {
        this.groupRef = ref.child("groups").child(groupName);
        this.groupPassword = groupPassword;
        mPlayer.setRef(this.groupRef);
        mPlayer.setisPlaying(true);
        mPlayer.setAdmin(false);
        mPlayer.setTargetuid("TEST TARGET UID");
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
        this.mPlayer.setAdmin(true);
    }

    public void killPressed() {
//        final Location l = new Location("");
//        l.setLatitude(mPlayer.getLatitude());
//        l.setLongitude(mPlayer.getLongitude());
        if(mPlayer.getTargetuid() != null) {
            final Firebase target = groupRef.child("players").child(mPlayer.getTargetuid());
            target.child("isDead").setValue(true);


            target.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mPlayer.setTargetuid(dataSnapshot.getValue(Player.class).getTargetuid());
                    Log.v(TAG, "New target: " + mPlayer.getTargetuid());

                    mPlayer.incKill();
                    ref.child("players").child(mPlayer.getUid()).child("kills").setValue(mPlayer.getKills());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    public Firebase getRef() {
        return ref;
    }

    public Player getPlayer() {
        return mPlayer;
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

    /*
    //gets the user
    final Firebase target = groupRef.child("players").child(mPlayer.getUid());
    target.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() == 1){
                Log.v(TAG, "THERES SOMEONE HERE!@!!!@@@KJ@LJ@LJH@KG@KGH@JKLH@LKJH@LKJH@KLJH@LKJ@HKLH@KLJH@KLJH@KLH@KLJH@KLH@");
            } else {
                Log.v(TAG,"OH SHITTTTTT GFUCK MY AAAAAAAARSE");
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    });
    */

    // callback when data in groupRef object gets changed
    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        Log.v(TAG, "Group data change: " + dataSnapshot.getValue());
        // user provides correct credentials
        if(dataSnapshot.child("password").getValue().equals(groupPassword)) {
            // reference to list of players for current groupRef
            final Firebase playersRef = groupRef.child("players");
            playersRef.child(this.mPlayer.getUid()).setValue(this.mPlayer);

//            playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                        Player newPlayer = postSnapshot.getValue(Player.class);
//                        Log.v(TAG, String.valueOf(dataSnapshot.getChildrenCount()));
//                        if (dataSnapshot.getChildrenCount() == 1 || newPlayer.getAdmin()) {
//                            Log.v(TAG, "THERES SOMEONE HERE!@!!!@@@KJ@LJ@LJH@KG@KGH@JKLH@LKJH@LKJH@KLJH@LKJ@HKLH@KLJH@KLJH@KLH@KLJH@KLH@");
//                            if(newPlayer.getUid().equals(mPlayer.getUid())) {
//                                mPlayer.setAdmin(true);
//                                playersRef.child(newPlayer.getUid()).child("admin").setValue(true);
//                            }
//
//                        } //else {
//                            //Log.v(TAG, "OH SHITTTTTT GFUCK MY AAAAAAAARSE");
//                            //newPlayer.setAdmin(false);
//                        //}
//                    }
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//
//                }
//            });

            playersRef.child(this.mPlayer.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Player player = snapshot.getValue(Player.class);
                    Log.v(TAG, "Kills: " + player.getKills());
                    mPlayer.setKills(player.getKills());
                        mPlayer.setDeaths(player.getDeaths());
                        mPlayer.setCurrency(player.getCurrency());
                        mPlayer.setUserName(player.getUserName());
                    mPlayer.setAdmin(player.getAdmin());
                    mPlayer.setisPlaying(true);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });

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
