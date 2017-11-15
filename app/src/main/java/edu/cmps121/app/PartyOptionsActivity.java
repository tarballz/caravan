package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.cmps121.app.api.State;

public class PartyOptionsActivity extends AppCompatActivity {
    private State state;

    private static final String TAG = PartyOptionsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_options);

        state = new State(this);

        if (state.party != null && !state.party.isEmpty())
            state.nextActivity(this, PartyMenuActivity.class);

        TextView textView = (TextView) findViewById(R.id.display_name_tv);
        textView.setText("Hello " + state.username + "!");
    }

    public void onClickCreatePartyOptions(View view) {
        state.nextActivity(this, CreatePartyActivity.class);
    }

    public void onClickFindPartyOptions(View view) {
        state.nextActivity(this, FindPartyActivity.class);
    }
}
