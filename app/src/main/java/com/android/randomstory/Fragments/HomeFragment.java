package com.android.randomstory.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.randomstory.Activities.SearchActivity;
import com.android.randomstory.Adapters.PostAdapter;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    PostAdapter postAdapter;

    // Objects of Firebase Auth, Database and Storage
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    // Create arraylist
    ArrayList<PostModel> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Creating Instance of Firebase Auth, Database and Storage
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        // Go to another Activity from current Activity
        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromFirebase();
    }

    // Get data of all the posts from Firebase
    private void getDataFromFirebase() {
        database.getReference().child("posts").orderByChild("postAt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the ArrayList Before Adding Data
                list.clear();
                // // Iteration to get data from each node
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    if (post != null) {
                        post.setPostID(dataSnapshot.getKey());
                        list.add(post);
                    }
                }
                Collections.reverse(list);

                if (postAdapter != null) {
                    postAdapter.notifyDataSetChanged();
                }

                binding.spinKit.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.spinKit.setVisibility(View.GONE);
            }
        });


        // Notifying adapter that we have some data to show after Loops end
        postAdapter = new PostAdapter(getContext(), list);
        binding.PostViewPager.setAdapter(postAdapter);

    }


}