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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        EditText editText = (EditText) findViewById(R.id.c_party_name);
        cPartyName = editText.getText().toString();
    }

    public void lookup(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://169.233.242.236:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TeamService service = retrofit.create(TeamService.class);
        Call<List<Team>> teams = service.listTeams();

        teams.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> t, Response<List<Team>> response) {
                // the request worked!!
                if (response.isSuccessful()) {
                    Log.d("RESULTS: ", "SUCCESS!");
                    List<Team> list = response.body();
                    /*Team team = null;
                    for (int i = 0; i < list.size(); i++) {
                        team = new Team();
                        String name = list.get(i).getName();
                        Log.d("NAME: ", name);
                        String[] cars = list.get(i).getCars();
                        Log.d("CAR[0]: ", cars[0]);
                        team.setName(name);
                        team.setCars(cars);
                        // For some reason this crashes the app...
                        //localTeamList.add(team);
                    }*/
                }
            }

            @Override
            public void onFailure(Call<List<Team>> t, Throwable e) {
                Log.d("RESULTS: ", "FAILURE!");
                Toast.makeText(CreateParty.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

//        for (int i = 0; i < localTeamList.size(); i++) {
//            if (cPartyName.equals(localTeamList.get(i).getName())) {
//                Toast.makeText(CreateParty.this, "Team already exists!", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
