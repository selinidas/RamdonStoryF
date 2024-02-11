package com.android.randomstory.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Models.CommentModel;
import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.CommentItemLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.viewholder> {

    ArrayList<CommentModel> list;
    Context context;
    FirebaseDatabase mDatabase;

    private static final SparseIntArray avatarMap;

    static {
        avatarMap = new SparseIntArray();
        avatarMap.put(1, R.drawable.img_1);
        avatarMap.put(2, R.drawable.img_2);
        avatarMap.put(3, R.drawable.img_3);
        avatarMap.put(4, R.drawable.img_4);
        avatarMap.put(5, R.drawable.img_5);
        avatarMap.put(6, R.drawable.img_6);
        avatarMap.put(7, R.drawable.img_7);
        avatarMap.put(8, R.drawable.img_8);
        avatarMap.put(9, R.drawable.img_9);
    }

    public CommentsAdapter(ArrayList<CommentModel> list, Context context) {
        this.list = list;
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        CommentModel comment = list.get(position);
        holder.binding.commentbody.setText(comment.getCommentBody());

        loadUserData(comment.getCommentedBy(), holder);
    }

    private void loadUserData(String userId, viewholder holder) {
        mDatabase.getReference().child("users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        if (model != null) {
                            holder.binding.name.setText(model.getName());
                            int index = Integer.parseInt(model.getImageUrl());
                            holder.binding.avatar.setBackgroundResource(avatarMap.get(index, 0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Error fetching data", error.toException());
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        CommentItemLayoutBinding binding;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding = CommentItemLayoutBinding.bind(itemView);
        }
    }
}