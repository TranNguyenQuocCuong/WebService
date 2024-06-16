package com.example.webservice;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPostActivity extends AppCompatActivity {
    private EditText etTitle, etBody;
    private Button btnSave;
    private ApiService apiService;
    private Post post;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etTitle = findViewById(R.id.etTitle);
        etBody = findViewById(R.id.etBody);
        btnSave = findViewById(R.id.btnSave);
        apiService = ApiClient.getApiService();
        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.getStringExtra("id") != null) {
            postId = Integer.parseInt(intent.getStringExtra("id"));
            fetchPost(postId);
        } else {
            Toast.makeText(this, "Post ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String body = etBody.getText().toString();
                if (title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(EditPostActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                editPost(new Post(postId, title, body));
            }
        });
    }
    private void fetchPost(int postId) {
        apiService.getPostbyId(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    post = response.body();
                    etTitle.setText(post.getTitle());
                    etBody.setText(post.getBody());
                } else {
                    Toast.makeText(EditPostActivity.this, "Failed to fetch post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Failed to fetch post", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPost(Post post) {
        apiService.updatePost(post.getId(), post).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPostActivity.this, "Post updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(EditPostActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(EditPostActivity.this, "Failed to update post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

