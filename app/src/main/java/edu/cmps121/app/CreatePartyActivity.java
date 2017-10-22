package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    String url = "https://169.233.219.84:8000/teams/?name=";

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

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.28:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Call<List<Team>> teams = retrofit
                .create(TeamService.class)
                .listTeams();

        // Remember, this enqueue() method works asynchronously.
        teams.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> t, Response<List<Team>> response) {
                // the request worked!!
                if (response.isSuccessful()) {
                    if (!checkTeamExists(response.body())) {
                        intent.putExtra(EXTRA_MESSAGE, cPartyName);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Team>> t, Throwable e) {
                Log.e("RESULTS: ", "FAILURE!");
                shortToast(CreatePartyActivity.this, "Server unresponsive, please try again later");
                t.cancel();

                // TODO: Remove this move to the next activity, just doing this so others can work off this branch.
                intent.putExtra(EXTRA_MESSAGE, cPartyName);
                startActivity(intent);
            }
        });*/

        // Instantiate the RequestQueue
        url = strAppend(url, cPartyName);
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private boolean checkTeamExists(List<Team> teamList) {
        try {
            boolean teamExists = teamList.stream()
                    .filter(e -> e.getName().equals(cPartyName))
                    .findFirst()
                    .isPresent();

            if (teamExists) {
                shortToast(CreatePartyActivity.this, "Team already exists!");
                return true;
            }

            return false;
        } catch (NullPointerException e) {
            // What does it mean when teamList is null? Does this mean that something is broken?
            // If so, then throw a new RunTimeException(e) here instead of logging & returning
            Log.i("log", "teamList is null");
            shortToast(CreatePartyActivity.this, "teamList is null!");
            return false;
        }
    }
}
