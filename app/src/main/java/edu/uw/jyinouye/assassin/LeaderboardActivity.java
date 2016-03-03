package edu.uw.jyinouye.assassin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    List<String> leaders = Arrays.asList("1", "Test User", "2", "3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ListView leaderList = (ListView)findViewById(R.id.leaderboard_listview);

    }
}
