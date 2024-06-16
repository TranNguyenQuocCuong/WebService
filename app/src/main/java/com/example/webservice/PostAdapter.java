package com.example.webservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Post post);

        void onDeleteClick(Post post);
    }

    public PostAdapter(Context context, List<Post> postList, OnItemClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.listener = listener;
    }

    public void deletePost(Post post) {
        int position = postList.indexOf(post);
        if (position != -1) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.tvBody.setText(post.getBody());
        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(post);
            }
        });
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBody;
        ImageButton buttonEdit, buttonDelete;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
