package com.android.randomstory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Activities.DetailPostActivity;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.R;

import java.util.ArrayList;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostModel> list;

    // Constructor
    public MyPostsAdapter(Context context, ArrayList<PostModel> list) {
        this.context = context;
        this.list = list;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_posts_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel postModel = list.get(position);
        holder.storyText.setText(postModel.getPostDescription());

        holder.itemView.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                PostModel clickedData = list.get(pos);
                Intent intent = new Intent(context, DetailPostActivity.class);
                intent.putExtra("post_detail", clickedData);
                Log.d("TAG", "Clicked item: " + clickedData.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView storyText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyText = itemView.findViewById(R.id.storytext);
        }
    }

    // Method to add a new post to the list
    public void addPost(PostModel postModel) {
        list.add(postModel);
        notifyItemInserted(list.size() - 1);
    }

    // Method to remove a post from the list
    public void removePost(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    // Method to update a post
    public void updatePost(int position, PostModel newPostModel) {
        list.set(position, newPostModel);
        notifyItemChanged(position);
    }

    // Method to clear all the posts
    public void clearPosts() {
        list.clear();
        notifyDataSetChanged();
    }
}