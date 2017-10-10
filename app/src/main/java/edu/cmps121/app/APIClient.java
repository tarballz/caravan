package edu.cmps121.app;

import java.util.List;

import edu.cmps121.app.APITeam;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Payton on 10/10/17.
 */

public interface APIClient {

    @GET("/teams/{name}")
    Call<List<APITeam>> teamList(@Path("name") String name);

}
