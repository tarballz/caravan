package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.cmps121.app.api.State;

public class PartyMenuActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_menu);

        state = new State(this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String teamName = intent.getStringExtra(CreatePartyActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text.
        TextView textView = (TextView) findViewById(R.id.team_name_tv);
        textView.setText(teamName + "\'s");
    }

    public void onClickMaps(View view) {
        state.nextActivity(this, MapsActivity.class);
    }

    public void onClickSettings(View view) {
        state.nextActivity(this, SettingsActivity.class);
    }
}
