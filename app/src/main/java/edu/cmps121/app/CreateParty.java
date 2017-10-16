package edu.cmps121.app;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

    }

    public void lookup(View view) {
        EditText editText = (EditText) findViewById(R.id.c_party_name);
        cPartyName = editText.getText().toString();

        Log.d("TEAM NAME", cPartyName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://169.233.194.78:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TeamService service = retrofit.create(TeamService.class);
        Call<List<Team>> teams = service.listTeams();

        // Remember, this enqueue() method works asynchronously.
        teams.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> t, Response<List<Team>> response) {
                // the request worked!!
                if (response.isSuccessful()) {
                    Log.d("RESULTS: ", "SUCCESS!");
                    teamList = response.body();

                    if (teamList != null) {
                        for (int i = 0; i < teamList.size(); i++) {
                            if (cPartyName.equals(teamList.get(i).getName())) {
                                Toast.makeText(CreateParty.this, "Team already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(CreateParty.this, "teamList is null!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Team>> t, Throwable e) {
                Log.d("RESULTS: ", "FAILURE!");
                Toast.makeText(CreateParty.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
