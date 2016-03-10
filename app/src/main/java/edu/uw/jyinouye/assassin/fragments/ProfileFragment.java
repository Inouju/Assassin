package edu.uw.jyinouye.assassin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.uw.jyinouye.assassin.Assassin;
import edu.uw.jyinouye.assassin.Player;
import edu.uw.jyinouye.assassin.R;

/**
 * Displays stats for a player
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private Player player;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        Assassin assassin = (Assassin)getActivity().getApplication();
        player = assassin.getPlayer();

        //get all of the textViews
        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView kills = (TextView) rootView.findViewById(R.id.profile_kills);
        TextView deaths = (TextView) rootView.findViewById(R.id.profile_deaths);
        TextView currency = (TextView) rootView.findViewById(R.id.profile_currency);

        final int selectedAvator = player.getAvator();
        ImageView profile_image = (ImageView) rootView.findViewById(R.id.profile_image);
        setProfileImage(profile_image, selectedAvator, rootView);

        //set all of the textViews
        name.setText(player.getUserName());
        kills.setText(player.getKills() + "");
        deaths.setText(player.getDeaths() + "");
        currency.setText(player.getCurrency() + "");

        //get opponent information from firebase
        String targetUid = player.getTargetuid();
        Firebase target = assassin.getGroup().child("players").child(player.getTargetuid());
        target.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object avatar = dataSnapshot.child("avator").getValue();
                int selectedAvatar2 = 0;
                if(avatar != null) {
                    selectedAvatar2 = (int) avatar;
                }
                String name = dataSnapshot.child("userName").getValue(String.class);
                TextView target_name = (TextView) rootView.findViewById(R.id.target_name);
                target_name.setText(name);
                ImageView target_image = (ImageView) rootView.findViewById(R.id.target_image);
                setProfileImage(target_image, selectedAvatar2, rootView);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        return rootView;
    }

    //sets profile avatars
    public void setProfileImage(ImageView image, int selectedAvator, View rootView) {

        if (selectedAvator == 1) {
            image.setImageResource(R.drawable.avator1);
        } else if (selectedAvator == 2) {
            image.setImageResource(R.drawable.avator2);
        } else if (selectedAvator == 3) {
            image.setImageResource(R.drawable.avator3);
        } else if (selectedAvator == 4) {
            image.setImageResource(R.drawable.avator4);
        } else if (selectedAvator == 5) {
            image.setImageResource(R.drawable.avator5);
        } else if (selectedAvator == 6) {
            image.setImageResource(R.drawable.avator6);
        } else if (selectedAvator == 7) {
            image.setImageResource(R.drawable.avator7);
        } else if (selectedAvator == 8) {
            image.setImageResource(R.drawable.avator8);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}