package edu.cmps121.app.api;

import java.util.List;

import edu.cmps121.app.model.Team;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Payton on 10/10/17.
 */

public interface TeamService {
    @GET("teams")
    Call<List<Team>> listTeams();
}
