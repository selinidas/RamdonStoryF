package com.android.randomstory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Activities.OtherUserProfileActivity;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.Utils.Constant;
import com.android.randomstory.databinding.NotificationItemLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder> {
    Context context;
    ArrayList<UserModel> list;
    private boolean ischat;
    DatabaseReference database;

    public UsersAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
        this.ischat = ischat;
        database = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UserModel userModel = list.get(position);
        holder.binding.commentBy.setText(userModel.getName());


        Log.d("TAGdfgfdg", "onBindViewHolder: "+userModel.toString());
        String profilePicture = userModel.getImageUrl();


        switch (Objects.requireNonNull(profilePicture)) {
            case "1":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_1);
                break;
            case "2":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_2);
                break;
            case "3":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_3);
                break;
            case "4":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_4);
                break;
            case "5":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_5);
                break;
            case "6":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_6);
                break;
            case "7":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_7);
                break;
            case "8":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_8);
                break;
            case "9":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_9);
                break;
        }


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, OtherUserProfileActivity.class);
               intent.putExtra("UserModel",userModel);
               context.startActivity(intent);
           }
       });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        NotificationItemLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationItemLayoutBinding.bind(itemView);
        }
    }


}
