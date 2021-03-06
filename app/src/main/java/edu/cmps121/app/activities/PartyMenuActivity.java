package edu.cmps121.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import edu.cmps121.app.utilities.NearbyPlace;
import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

public class PartyMenuActivity extends AppCompatActivity {

    private State state;
//    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_menu);

        TextView textView = (TextView) findViewById(R.id.team_name_tv);
        state = new State(this);

        String welcomeText = state.party + "\'s";
        textView.setText(welcomeText);
    }

    public void onClickCreateCarMenu(View view) {
        state.nextActivity(this, CreateCarActivity.class);
    }

    public void onClickFindCarMenu(View view) {
        state.nextActivity(this, FindCarActivity.class);
    }

    public void onClickMapMenu(View view) {
        state.nextActivity(this, MapsActivity.class);
    }

    public void onClickSettingsMenu(View view) {
        state.nextActivity(this, SettingsActivity.class);
    }

    public void onClickLogout(View view) {
        state.nextActivity(this, MainActivity.class);
    }

    /**
     * Do nothing, force user to log out if they want to return
     **/
    @Override
    public void onBackPressed() {
    }
}
