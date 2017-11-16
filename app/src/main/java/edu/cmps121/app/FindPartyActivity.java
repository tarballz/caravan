package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;
import edu.cmps121.app.model.Party;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class FindPartyActivity extends AppCompatActivity {
    private State state;
    private DynamoDB dynamoDB;

    private static final String TAG = FindPartyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_party);

        state = new State(this);
        dynamoDB = new DynamoDB(this);
    }

    public void onClickFindParty(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_find_party_name_et);
        String partyName = editText.getText().toString();

        if (!dynamoDB.itemExists(Party.class, partyName))
            shortToast(this, "Unable to find the party: " + partyName);
        else {
            state.party = partyName;

//            dynamoDB.updateUserParty(state.user, partyName);

            dynamoDB.updateItem(User.class, state.user, (obj) -> {
                User user = (User) obj;
                user.setParty(partyName);
                dynamoDB.saveItem(user);
            });

            state.nextActivity(this, PartyMenuActivity.class);
        }
    }
}
