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

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;

import edu.uw.jyinouye.assassin.Assassin;
import edu.uw.jyinouye.assassin.Player;
import edu.uw.jyinouye.assassin.R;
import edu.uw.jyinouye.assassin.util.Chat;
import edu.uw.jyinouye.assassin.util.FirebaseListAdapter;

/**
 *
 */
public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    private Firebase mPlayers;
    private ListView listView;
    //private ChatListAdapter mChatListAdapter;


    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assassin assassin = (Assassin) getActivity().getApplication();
        mPlayers = assassin.getGroup().child("players");
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


    /*
    @Override
    public void onStart() {
        super.onStart();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mPlayers, getActivity(), R.layout.fragment_leaderboard, "TEST");
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
    }
    */



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*
    private class ChatListAdapter extends FirebaseListAdapter<Chat> {

        private String mUserId;

        public ChatListAdapter(Query ref, Activity activity, int layout, String mUserId) {
            super(ref, Chat.class, layout, activity);
            this.mUserId = mUserId;
        }

        @Override
        protected void populateView(View v, Chat chat) {
        }
    }
    */

}
