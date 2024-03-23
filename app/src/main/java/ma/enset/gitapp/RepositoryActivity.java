package ma.enset.gitapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ma.enset.gitapp.model.GitRepo;
import ma.enset.gitapp.service.GitRepoServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repository_layout);
        TextView textViewLogin = findViewById(R.id.textViewUserLogin);
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        Log.i("info",login);
        ListView listView = findViewById(R.id.listViewRepo);
        List<String> data = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,data);
        textViewLogin.setText(login);
        listView.setAdapter(arrayAdapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitRepoServiceApi gitRepoServiceApi = retrofit.create(GitRepoServiceApi.class);
        Call<List<GitRepo>> reposCall = gitRepoServiceApi.userRepositories(login);
        reposCall.enqueue(new Callback<List<GitRepo>>() {
            @Override
            public void onResponse(Call<List<GitRepo>> call, Response<List<GitRepo>> response) {
                Log.i("info",call.request().url().toString());
                if(!response.isSuccessful()){
                    Log.e("error",String.valueOf(response.code()));
                    return;
                }
                List<GitRepo> gitRepos = response.body();
                for (GitRepo g : gitRepos){
                    String content="";
                    content+= g.id+"\n";
                    content+= g.name+"\n";
                    content+= g.language+"\n";
                    data.add(content);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitRepo>> call, Throwable throwable) {

            }
        });
    }
}
