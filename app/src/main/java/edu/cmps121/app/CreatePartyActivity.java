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

    // Initialize the Amazon Cognito credentials provider.
    // Pass the credentials provider object to the constructor of the AWS client you are using.
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
//                Regions.US_WEST_2 // Region
//        );

//    private boolean checkTeamExists(List<Team> teamList) {
//        try {
//            boolean teamExists = teamList.stream()
//                    .filter(e -> e.getName().equals(cPartyName))
//                    .findFirst()
//                    .isPresent();
//
//            if (teamExists) {
//                shortToast(CreatePartyActivity.this, "Team already exists!");
//                return true;
//            }
//
//            return false;
//        } catch (NullPointerException e) {
//            // What does it mean when teamList is null? Does this mean that something is broken?
//            // If so, then throw a new RunTimeException(e) here instead of logging & returning
//            Log.i("log", "teamList is null");
//            shortToast(CreatePartyActivity.this, "teamList is null!");
//            return false;
//        }
//    }
}
