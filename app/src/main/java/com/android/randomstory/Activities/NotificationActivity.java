package com.android.randomstory.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.randomstory.Adapters.NotificationAdapter;
import com.android.randomstory.Models.NotificationModel;
import com.android.randomstory.databinding.ActivityNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityNotificationBinding binding;
    // ArrayList for Notification
    ArrayList<NotificationModel> list = new ArrayList<>();
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Creating Instance of Firebase Database
        database = FirebaseDatabase.getInstance();
        NotificationAdapter adapter = new NotificationAdapter(list, this);


        // gettting the Notification Data from Firebase
        database.getReference().child("notification").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iteration to get data from each node
                // Clear the ArrayList Before Adding Data
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    // Getting the data from Firebase and saving them into a notification Model
                    NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                    notificationModel.setNotificationID(dataSnapshot.getKey());
                    list.add(notificationModel);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.spinKit.setVisibility(View.GONE);
            }
        });

        // Notifying adapter that we have some data to show after Loops end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.notificationRv.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        binding.notificationRv.setHasFixedSize(true);
        binding.notificationRv.setNestedScrollingEnabled(false);
        binding.notificationRv.setAdapter(adapter);
        binding.spinKit.setVisibility(View.GONE);
        binding.icBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == binding.icBack) {
            // Remove the Current Activity from stack
            finish();
        }
    }
}