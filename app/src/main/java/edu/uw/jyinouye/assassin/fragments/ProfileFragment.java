package edu.uw.jyinouye.assassin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.jyinouye.assassin.Assassin;
import edu.uw.jyinouye.assassin.Player;
import edu.uw.jyinouye.assassin.R;

/**
 * Displays stats for a player
 */
public class ProfileFragment extends Fragment {



    private static final String TAG = "ProfileFragment";

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
        Player player = assassin.getPlayer();

        //get all of the textViews
        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView kills = (TextView) rootView.findViewById(R.id.profile_kills);
        TextView deaths = (TextView) rootView.findViewById(R.id.profile_deaths);
        TextView currency = (TextView) rootView.findViewById(R.id.profile_currency);

        //set all of the textViews
        name.setText(player.getUserName());
        kills.setText(player.getKills() + "");
        deaths.setText(player.getDeaths() + "");
        currency.setText(player.getCurrency() + "");

        return rootView;
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
