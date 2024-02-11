package com.android.randomstory.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.randomstory.Adapters.FavListAdapter;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.databinding.ActivityFavouriteListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteListActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityFavouriteListBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    ArrayList<PostModel> favLists;
    FavListAdapter favListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavouriteListBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        initViews();


    }

    private void initViews() {
        binding.icBack.setOnClickListener(this);

        // Creating Instance of Firebase Authentication, RealTime Database & Storage
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        favLists = new ArrayList<>();

        favListAdapter = new FavListAdapter(FavouriteListActivity.this, favLists);

        // getting data from Firebase database from node named "favLists"
        mDatabase.getReference().child("users").child(mAuth.getUid()).child("favLists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iteration to get data from each node
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    favLists.add(postModel);
                    Log.d("TAG12fav", "onDataChange: " + favLists.toString());
                }

                favListAdapter.notifyDataSetChanged();
                binding.spinKit.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.spinKit.setVisibility(View.GONE);
            }
        });


        // Notifying adapter that we have some data to show after Loops end
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.favListRv.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        binding.favListRv.setHasFixedSize(true);
        binding.favListRv.setNestedScrollingEnabled(false);
        binding.favListRv.setAdapter(favListAdapter);


    }

    @Override
    public void onClick(View view) {
        if (view == binding.icBack) {
            finish();
        }
    }
}