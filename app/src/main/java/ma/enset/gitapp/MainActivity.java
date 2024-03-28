package ma.enset.gitapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ma.enset.gitapp.model.GitUser;
import ma.enset.gitapp.model.GitUsersResponse;
import ma.enset.gitapp.adapters.UsersListViewModel;
import ma.enset.gitapp.service.GitRepoServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        EditText searchText = findViewById(R.id.searchText);
        Button searchButton = findViewById(R.id.searchButton);
        ListView resultView = findViewById(R.id.resultView);
        List<GitUser> data = new ArrayList<>();
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        UsersListViewModel arrayAdapter = new UsersListViewModel(this,R.layout.users_list_view_layout,data);
        resultView.setAdapter(arrayAdapter);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchButton.setOnClickListener(v->{
            arrayAdapter.clear();
            String query = searchText.getText().toString();
            GitRepoServiceApi gitRepoServiceApi = retrofit.create(GitRepoServiceApi.class);
            Call<GitUsersResponse> callGitUsers = gitRepoServiceApi.searchUsers(query);
            callGitUsers.enqueue(new Callback<GitUsersResponse>() {
                @Override
                public void onResponse(@NonNull Call<GitUsersResponse> call, Response<GitUsersResponse> response) {
                    if(!response.isSuccessful()){
                        return;
                    }
                    GitUsersResponse gitUsersResponse= response.body();
                    assert gitUsersResponse != null;
                    data.addAll(gitUsersResponse.users);
                    arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<GitUsersResponse> call, Throwable throwable) {

                }
            });
        });
        resultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String login = data.get(position).login;
                Intent intent = new Intent(getApplicationContext(),RepositoryActivity.class);
                intent.putExtra("login",login);
                startActivity(intent);
            }
        });


    }
}