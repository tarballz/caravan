package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class CreatePartyActivity extends AppCompatActivity {

    String cPartyName = "";
    // Tutorial says to prefix our intent extras with our package name
    // to avoid conflicts.
    public static final String EXTRA_MESSAGE = "edu.cmps121.app.USERSNAME";

    String url = "https://169.233.219.84:8000/teams/?name=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        // Initialize the Amazon Cognito credentials provider.
        // Pass the credentials provider object to the constructor of the AWS client you are using.
//        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
//                getApplicationContext(),
//                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
//                Regions.US_WEST_2 // Region
//        );
    }

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
