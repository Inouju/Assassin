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
 *
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
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.v(TAG, "making the view -------------------------------------------");
        //Player player = Assassin.getPlayer();
        //Player player = Assassin.getPlayer();
        //Log.v(TAG, );

        /*
        profile_name
        profile_kills
        profile_deaths
        profile_currency
         */
        //bundle sends kills,deaths, currency, name

        //get all of the textViews
        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView kills = (TextView) rootView.findViewById(R.id.profile_kills);
        TextView deaths = (TextView) rootView.findViewById(R.id.profile_deaths);
        TextView currency = (TextView) rootView.findViewById(R.id.profile_currency);

        Bundle bundle = this.getArguments();
        String killsString = Integer.toString(bundle.getInt("kills"));
        //set all of the textViews
        name.setText(bundle.getString("name"));
        kills.setText(Integer.toString(bundle.getInt("kills")));
        deaths.setText(Integer.toString(bundle.getInt("deaths")));
        currency.setText(Integer.toString(bundle.getInt("currency")));


        //String name = bundle.getString("name");
        //Log.v(TAG, name);
        Log.v(TAG, "Finished! -------------------------------------------");
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
