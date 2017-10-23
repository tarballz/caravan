package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cmps121.app.api.State;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class FindPartyActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_party);

        state = new State(this);
    }

    public void onClickFindParty(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_find_party_name_et);
        String potentialParty = editText.getText().toString();

        if (isReal(potentialParty)) {
            state.party = potentialParty;
            state.nextActivity(this, PartyMenuActivity.class);
        }
        else
            shortToast(this, "Unable to find the party: " + potentialParty);
    }

    private boolean isReal(String potentialParty) {
        // TODO: search DB for party name,
        return true;
    }
}
