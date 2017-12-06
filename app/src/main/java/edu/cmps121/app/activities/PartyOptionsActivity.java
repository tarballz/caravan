package edu.cmps121.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

public class PartyOptionsActivity extends AppCompatActivity {

    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_options);

        TextView textView = (TextView) findViewById(R.id.party_options_greeting_tv);
        state = new State(this);

        if (state.party != null && !state.party.isEmpty())
            state.nextActivity(this, PartyMenuActivity.class);

        String welcomeText = "Hello " + state.user + "!";
        textView.setText(welcomeText);
    }

    public void onClickCreatePartyOptions(View view) {
        state.nextActivity(this, CreatePartyActivity.class);
    }

    public void onClickFindPartyOptions(View view) {
        state.nextActivity(this, FindPartyActivity.class);
    }
}
