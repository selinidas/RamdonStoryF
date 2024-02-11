package com.android.randomstory.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Activities.DetailPostActivity;
import com.android.randomstory.Models.NotificationModel;
import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.NotificationItemLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewholder> {

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false);
        return new NotificationAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.viewholder holder, int position) {

        // Get all the Notification History

        NotificationModel notification = list.get(position);
        String type = notification.getType();

        Log.d("TAGsdfdsdf", "onBindViewHolder: " + notification.toString());


        // Get data from Firebase of User Notification History
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(notification.getNotificationBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

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

                        if (type.equals("Story liked")) {
                            holder.binding.commentBy.setText(Html.fromHtml("<b>" + userModel.getName() + "</b>" + " like your post"));
                        } else if (type.equals("Story Commented")) {
                            holder.binding.commentBy.setText(Html.fromHtml("<b>" + userModel.getName() + "</b>" + " commented on your post"));
                        } else {
                            holder.binding.commentBy.setText(Html.fromHtml("<b>" + userModel.getName() + "</b>" + ""));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPostActivity.class);
                intent.putExtra("postId", notification.getPostID());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        NotificationItemLayoutBinding binding;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationItemLayoutBinding.bind(itemView);

        }
    }
}
