package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cmps121.app.api.State;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class CreatePartyActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        state = new State(this);
    }

    public void onClickCreateParty(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_create_party_name_et);
        String potentialParty = editText.getText().toString();

        if (isUnique(potentialParty)) {
            state.party = potentialParty;
            state.nextActivity(this, PartyMenuActivity.class);
        } else
            shortToast(this, potentialParty + " has already been taken");
    }

    private boolean isUnique(String potentialParty) {
        // TODO: check for uniqueness here. Scan db party names
        return true;
    }
}
