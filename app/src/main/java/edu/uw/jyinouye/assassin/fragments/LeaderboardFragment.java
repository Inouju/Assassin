package edu.uw.jyinouye.assassin.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uw.jyinouye.assassin.Assassin;
import edu.uw.jyinouye.assassin.Player;
import edu.uw.jyinouye.assassin.R;
import edu.uw.jyinouye.assassin.util.Chat;
import edu.uw.jyinouye.assassin.util.FirebaseListAdapter;
import edu.uw.jyinouye.assassin.util.Ranking;

/**
 *
 */
public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    private Firebase mPlayers;
    private Firebase mPlayerGroup;
    private ListView listView;
    private ArrayAdapter LeaderboardAdapter;
    private List<Ranking> rankings;
    //private ChatListAdapter mChatListAdapter;

    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rankings = new ArrayList<Ranking>();
        Assassin assassin = (Assassin) getActivity().getApplication();
        mPlayers = assassin.getRef().child("players");
        mPlayers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rankings.clear();
                LeaderboardAdapter.clear();
                for(DataSnapshot dater : dataSnapshot.getChildren()) {
                    String email = dater.child("email").getValue(String.class);
                    int kills = dater.child("kills").getValue(Integer.class);
                    String username = dater.child("username").getValue(String.class);
                    Ranking rank = new Ranking(email, username, kills);
                    rankings.add(rank);
                    Collections.sort(rankings);
                    for(int i = 0;i < rankings.size();i++) {
                        LeaderboardAdapter.insert(rankings.get(i),0);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        LeaderboardAdapter = new ArrayAdapter(getActivity(), R.layout.list_item,R.id.txtItem);
        Log.v(TAG, "mPlayers: " + mPlayers);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        listView = (ListView) v.findViewById(R.id.leaderboard_listview);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        listView.setAdapter(LeaderboardAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
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
