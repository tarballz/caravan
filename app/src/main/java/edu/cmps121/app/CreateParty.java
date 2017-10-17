package edu.cmps121.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import edu.cmps121.app.api.TeamService;
import edu.cmps121.app.model.Team;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateParty extends AppCompatActivity {

    String cPartyName = "";
    List<Team> teamList = null;
    // Tutorial says to prefix our intent extras with our package name
    // to avoid conflicts.
    public static final String EXTRA_MESSAGE = "edu.cmps121.app.USERSNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

    }

    public void lookup(View view) {
        EditText editText = (EditText) findViewById(R.id.c_party_name);
        cPartyName = editText.getText().toString();
        final Intent intent = new Intent(this, PartyMenuActivity.class);

        Log.d("TEAM NAME", cPartyName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.28:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TeamService service = retrofit.create(TeamService.class);
        Call<List<Team>> teams = service.listTeams();

        // Remember, this enqueue() method works asynchronously.
        teams.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> t, Response<List<Team>> response) {
                // the request worked!!
                boolean teamExists = false;
                if (response.isSuccessful()) {
                    Log.d("RESULTS: ", "SUCCESS1!");
                    teamList = response.body();

                    if (teamList != null) {
                        for (int i = 0; i < teamList.size(); i++) {
                            if (cPartyName.equals(teamList.get(i).getName())) {
                                teamExists = true;
                                Toast.makeText(CreateParty.this, "Team already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } else {
                        // TODO: Take this bool assignment out later, just using it to safeguard for now.
                        teamExists = true;
                        Toast.makeText(CreateParty.this, "teamList is null!", Toast.LENGTH_SHORT).show();
                    }
                    if (!teamExists) {
                        intent.putExtra(EXTRA_MESSAGE, cPartyName);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Team>> t, Throwable e) {
                Log.d("RESULTS: ", "FAILURE!");
                Toast.makeText(CreateParty.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                t.cancel();
                // TODO: Remove this move to the next activity, just doing this so others can work off this branch.
                intent.putExtra(EXTRA_MESSAGE, cPartyName);
                startActivity(intent);
            }
        });

    }
}
