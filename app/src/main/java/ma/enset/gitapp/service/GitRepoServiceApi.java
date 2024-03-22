package ma.enset.gitapp.service;

import ma.enset.gitapp.model.GitUsersResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface GitRepoServiceApi {
    @GET("search/users")
    public Call<GitUsersResponse> searchUsers(@Query("q") String query);
}
