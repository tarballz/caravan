package edu.cmps121.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

public class PartyMenuActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_menu);

        state = new State(this);

        TextView textView = (TextView) findViewById(R.id.team_name_tv);
        textView.setText(state.party + "\'s");
    }

    public void onClickCreateCarMenu(View view) {
       state.nextActivity(this, CreateCarActivity.class);
    }

    public void onClickFindCarMenu(View view) {
        state.nextActivity(this, FindCarActivity.class);
    }

    public void onClickMapsMenu(View view) {
        state.nextActivity(this, MapsActivity.class);
    }

    public void onClickSettingsMenu(View view) {
        state.nextActivity(this, SettingsActivity.class);
    }
}
