package com.android.randomstory.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Activities.OtherUserProfileActivity;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostModel> list;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public FavListAdapter(Context context, ArrayList<PostModel> list) {
        this.context = context;
        this.list = list;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostModel postModel = list.get(position);
        holder.name.setText(postModel.getPosterName());
        holder.ic_delete.setVisibility(View.VISIBLE);

        setAvatarBackground(holder.avatar, postModel.getImageUrl());

        holder.ic_delete.setOnClickListener(view -> showDeleteDialog(position));
        holder.itemView.setOnClickListener(view -> openOtherUserProfileActivity(postModel));
        holder.itemView.setOnLongClickListener(view -> {
            showDeleteDialog(position);
            return true;
        });
    }

    private void setAvatarBackground(CircleImageView avatar, String profilePicture) {
        int drawableId = context.getResources().getIdentifier(
                "img_" + profilePicture,
                "drawable",
                context.getPackageName()
        );
        if (drawableId != 0) {
            avatar.setBackgroundResource(drawableId);
        }
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
        alertbox.setMessage("Are you sure you want to Remove?");
        alertbox.setTitle("Remove From Favourite");
        alertbox.setIcon(R.drawable.ic_delete);
        alertbox.setPositiveButton("Yes", (dialogInterface, i) -> {
            list.remove(position);
            Paper.book().write("FavList", list);
            notifyDataSetChanged();
            // Update Firebase
            updateFirebase();
        });
        alertbox.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        alertbox.show();
    }

    private void updateFirebase() {
        mDatabase.getReference().child("users").child(mAuth.getUid()).child("favLists")
                .setValue(list).addOnSuccessListener(unused ->
                        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Log.e("FirebaseError", "Error updating data", e));
    }

    private void openOtherUserProfileActivity(PostModel postModel) {
        Intent intent = new Intent(context, OtherUserProfileActivity.class);
        intent.putExtra("PostModel", postModel);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView name;
        ImageView ic_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.commentBy);
            ic_delete = itemView.findViewById(R.id.ic_delete);
        }
    }
}
