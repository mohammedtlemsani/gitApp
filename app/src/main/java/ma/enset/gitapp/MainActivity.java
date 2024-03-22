package ma.enset.gitapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import ma.enset.gitapp.model.GitUser;
import ma.enset.gitapp.model.GitUsersResponse;
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
        EditText searchText = findViewById(R.id.searchText);
        Button searchButton = findViewById(R.id.searchButton);
        ListView resultView = findViewById(R.id.resultView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
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
                public void onResponse(Call<GitUsersResponse> call, Response<GitUsersResponse> response) {
                    Log.i("info",String.valueOf(call.request().url()));
                    if(!response.isSuccessful()){
                        Log.i("info",String.valueOf(response.code()));
                        return;
                    }
                    GitUsersResponse gitUsersResponse= response.body();
                    for(GitUser user: gitUsersResponse.users){
                        arrayAdapter.add(user.login);
                    }
                    arrayAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<GitUsersResponse> call, Throwable throwable) {

                }
            });
        });


    }
}