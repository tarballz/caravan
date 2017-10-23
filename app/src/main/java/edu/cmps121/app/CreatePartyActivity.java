package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.Request;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import java.util.List;

//import edu.cmps121.app.api.TeamService;
import edu.cmps121.app.model.Team;
import static edu.cmps121.app.api.CaravanUtils.shortToast;
import static edu.cmps121.app.api.CaravanUtils.strAppend;


public class CreatePartyActivity extends AppCompatActivity {

    String cPartyName = "";
    // Tutorial says to prefix our intent extras with our package name
    // to avoid conflicts.
    public static final String EXTRA_MESSAGE = "edu.cmps121.app.USERSNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        // Initialize the Amazon Cognito credentials provider.
        // Pass the credentials provider object to the constructor of the AWS client you are using.
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
                Regions.US_WEST_2 // Region
        );
    }

    public void lookup(View view) {
        EditText editText = (EditText) findViewById(R.id.c_party_name);
        cPartyName = editText.getText().toString();
        final Intent intent = new Intent(this, PartyMenuActivity.class);

        Log.i("TEAM NAME", cPartyName);

        
    }
    
}
