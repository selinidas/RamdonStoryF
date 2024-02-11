package com.android.randomstory.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.viewholder> {

    private Context context;
    private ArrayList<String> likedList;
    private FirebaseDatabase mDatabase;

    public LikesAdapter(Context context, ArrayList<String> likedList) {
        this.context = context;
        this.likedList = likedList;
        mDatabase = FirebaseDatabase.getInstance();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.likes_item_post, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String likedBy = likedList.get(position);
        fetchUserFromFirebase(holder, likedBy);
    }

    private void fetchUserFromFirebase(@NonNull viewholder holder, String likedBy) {
        mDatabase.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel.getUserId().contains(likedBy)) {
                        holder.name.setText(userModel.getName());
                        setAvatarBackground(holder.avatar, userModel.getImageUrl());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error here
                Log.e("FirebaseError", "Error fetching data", error.toException());
            }
        });
    }

    private void setAvatarBackground(ImageView avatar, String profilePicture) {
        int drawableId = context.getResources().getIdentifier(
                "img_" + profilePicture,
                "drawable",
                context.getPackageName()
        );
        if (drawableId != 0) {
            avatar.setBackgroundResource(drawableId);
        }
    }

    @Override
    public int getItemCount() {
        return likedList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
        }
    }
}