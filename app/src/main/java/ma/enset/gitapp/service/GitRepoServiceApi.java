package ma.enset.gitapp.service;

import java.util.List;

import ma.enset.gitapp.model.GitRepo;
import ma.enset.gitapp.model.GitUsersResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface GitRepoServiceApi {
    @GET("search/users")
    Call<GitUsersResponse> searchUsers(@Query("q") String query);
    @GET("users/{u}/repos")
    Call<List<GitRepo>> userRepositories(@Path("u") String login);
}
