package edu.uw.jyinouye.assassin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {
    Assassin assassin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        assassin = ((Assassin)getApplicationContext()).getInstance();

    }
}
