package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

public class CreateParty extends AppCompatActivity {

    private ListView listView;
    public String path = "http://127.0.0.1:8000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        listView = (ListView) findViewById(R.id.pagination_list);

        // Builder for Retrofit object so we can use Retrofit's API
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(path)
                // Converter to translate between Gson and Json
                .addConverterFactory(GsonConverterFactory.create());

        // Actually creating the Retrofit object
        Retrofit retrofit = builder.build();

        // Create instance of retrofit object
        APIClient client = retrofit.create(APIClient.class);
        Call<List<APITeam>> call = client.teamList("DreamTeam");

        // Utilize Call object asynchronously
        call.enqueue(new Callback<List<APITeam>>() {
            // If we acutally get a response.
            @Override
            public void onResponse(Call<List<APITeam>> call, Response<List<APITeam>> response) {
                List<APITeam> team = response.body();

                listView.setAdapter(new APITeamAdapter(CreateParty.this, team));
            }

            // Called if there is a network failure.
            @Override
            public void onFailure(Call<List<APITeam>> call, Throwable t) {
                Toast.makeText(CreateParty.this, "Error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
