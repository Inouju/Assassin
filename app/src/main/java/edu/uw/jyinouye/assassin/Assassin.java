package edu.uw.jyinouye.assassin;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
                ref.child("players").child(result.get("uid").toString()).child("email").setValue(mPlayer.getEmail());
                ref.child("players").child(result.get("uid").toString()).child("userName").setValue(mPlayer.getUserName());

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
    }

    public void killPressed() {
        final Location l = new Location("");
        l.setLatitude(mPlayer.getLatitude());
        l.setLongitude(mPlayer.getLongitude());
        final Firebase target = groupRef.child("players").child(mPlayer.getTargetuid());
        final int[] counter2 = {0};
        ValueEventListener targetListener = target.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (counter2[0] < 1) {
                    if(dataSnapshot.hasChild("isPlaying") && (Boolean)dataSnapshot.child("isPlaying").getValue() == true) {
                        Long lat = (Long) dataSnapshot.child("latitude").getValue();
                        Long longit = (Long) dataSnapshot.child("longitude").getValue();
                        Location r = new Location("enemy user");
                        r.setLatitude(lat);
                        r.setLongitude(longit);
                        if (l.distanceTo(r) < 15) {
                            final Integer value2 = (int) (long) dataSnapshot.child("deaths").getValue();
                            mPlayer.incKill();
                            final int[] counter = {0};
                            final Firebase playerkill = groupRef.child("players").child(mPlayer.getUid()).child("kills");
                            ValueEventListener listener = playerkill.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot2) {
                                    if (counter[0] < 1) {
                                        Integer value = (int) (long) dataSnapshot2.getValue();
                                        counter[0]++;
                                        playerkill.setValue(value + 1);
                                        target.child("deaths").setValue(value2 + 1);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


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

    // callback when data in groupRef object gets changed
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.v(TAG, "Group data change: " + dataSnapshot.getValue());
        // user provides correct credentials
        if(dataSnapshot.child("password").getValue().equals(groupPassword)) {
            // reference to list of players for current groupRef
            Firebase playersRef = groupRef.child("players");
            playersRef.child(this.mPlayer.getUid()).setValue(this.mPlayer);

            playersRef.child(this.mPlayer.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Player player = snapshot.getValue(Player.class);
                    Log.v(TAG, "Kills: " + player.getKills());
                    mPlayer.setKills(player.getKills());
                    mPlayer.setDeaths(player.getDeaths());
                    mPlayer.setCurrency(player.getCurrency());
                    mPlayer.setUserName(player.getUserName());
                    mPlayer.setTargetuid(player.getTargetuid());
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
