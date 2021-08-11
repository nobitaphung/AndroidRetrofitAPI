package com.example.retrofitclientexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CpuUsageInfo;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.retrofitclientexample.JasonPlaceHolderApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    private final String TAG = "RetrofitExample";

    JasonPlaceHolderApi jsonPlaceHolderApi;

    private final String baseUrl = "https://jsonplaceholder.typicode.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JasonPlaceHolderApi.class);

        getComments();
//        getPosts();
    }

    private void getPosts () {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("userId", "1");
            parameters.put("_sort", "id");
            parameters.put("_order", "desc");

            Call<List<Post>> call = jsonPlaceHolderApi.getPost(parameters);

            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                    Log.d(TAG, "onResponse");

                    if (!response.isSuccessful()) {
                        textViewResult.setText("Code: " + response.code());
                        return;
                    }
                    List<Post> posts = response.body();

                    for (Post post : posts) {
                        String Content = "";
                        Content += "ID: " + post.getId() + "\n";
                        Content += "UserId: " + post.getUserId() + "\n";
                        Content += "Title: " + post.getTitle() + "\n";
                        Content += "Text: " + post.getText() + "\n\n";

                        textViewResult.append(Content);
                    }

                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {

                    Log.d(TAG, "onFailure");
                    textViewResult.setText(t.getMessage());

                }
            });

        }

    private void getComments() {
            Call<List<Comment>> call = jsonPlaceHolderApi.getComment("posts/3/comments");

            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                    Log.d(TAG, "onResponse");

                    if(!response.isSuccessful()) {
                        textViewResult.setText("Code: " + response.code());
                        return;
                    }

                    List<Comment> comments = response.body();

                    for(Comment comment : comments) {
                        String Content ="";
                        Content += "PostId: " + comment.getPostId() + "\n";
                        Content += "ID: " + comment.getId() + "\n";
                        Content += "Name: " + comment.getName() + "\n";
                        Content += "Email: " + comment.getEmail() + "\n";
                        Content += "Text: " + comment.getText() + "\n\n";

                        textViewResult.append(Content);
                    }
                }


                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {

                    Log.d(TAG, "onFailure");
                    textViewResult.setText(t.getMessage());
                }
            });
        }
    }
