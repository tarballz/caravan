package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.cmps121.app.api.State;

public class PartyOptionsActivity extends AppCompatActivity {
    private State state; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_find_party);

        state = new State(this);

        TextView textView = (TextView) findViewById(R.id.display_name_tv);
        textView.setText("Hello " + state.username + "!");
    }

    public void onClickCreateParty(View view) {
        state.nextActivity(this, CreatePartyActivity.class);
    }

    public void onClickFindParty(View view) {
        state.nextActivity(this, FindPartyActivity.class);
    }
}
