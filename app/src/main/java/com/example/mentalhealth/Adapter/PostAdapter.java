package com.example.mentalhealth.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.model.Upload;
import com.example.mentalhealth.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private List<Upload> posts = new ArrayList<Upload>();

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);
        return new PostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Upload currentPost = posts.get(position);
        holder.tv_name.setText(currentPost.getUserName());
        Log.e("test5", "" + currentPost.getUserName());
        holder.tv_content.setText(currentPost.getPost());
        Picasso.get().load(Uri.parse(currentPost.getImageUrl())).into(holder.tv_image);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Upload> allPosts) {
        //this.posts = allPosts;
        this.posts = allPosts;
        Log.e("test4", "" + posts.get(0).getUserName());
        notifyDataSetChanged();
    }

    class PostHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_content;
        private ImageView tv_image;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.postUserName);
            tv_content = itemView.findViewById(R.id.postContent);
            tv_image = itemView.findViewById(R.id.postImageView);

        }
    }
}

