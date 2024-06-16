package com.example.webservice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FloatingActionButton fab;
    private ApiService apiService;
    private ImageButton buttonEdit;
    private ImageButton buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        apiService = ApiClient.getApiService();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
            startActivity(intent);
        });
        fetchPosts();
    }

    private void fetchPosts() {
        apiService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postAdapter = new PostAdapter(MainActivity.this, response.body(), new PostAdapter.OnItemClickListener() {
                        @Override
                        public void onEditClick(Post post) {
                            Intent intent = new Intent(MainActivity.this, EditPostActivity.class);
                            intent.putExtra("id", String.valueOf(post.getId()));
                            startActivity(intent);
                        }

                        @Override
                        public void onDeleteClick(Post post) {
                            postAdapter.deletePost(post);
                            deletePost(post);
                        }
                    });
                    recyclerView.setAdapter(postAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch posts", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void deletePost(Post post) {
        apiService.deletePost(post.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Post" + post.getId() + "deleted successfully", Toast.LENGTH_SHORT).show();
                }
                fetchPosts();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to delete post", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
