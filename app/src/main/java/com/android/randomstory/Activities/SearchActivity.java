package com.android.randomstory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.randomstory.Adapters.UsersAdapter;
import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    ArrayList<UserModel> list = new ArrayList<>();

    // Objects of Firebase Auth, Database
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView textView;
    UsersAdapter adapter;
    String data;

    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Creating Instance of Firebase Auth, Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        textView = findViewById(R.id.es_layout);

        // Initialize Adapter
        adapter = new UsersAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.followRv.setHasFixedSize(true);
        binding.followRv.setLayoutManager(layoutManager);

        // set Adapter
        binding.followRv.setAdapter(adapter);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        binding.followRv.setNestedScrollingEnabled(true);

        binding.searchView.clearFocus();

        //set All User in Search Fragment by Getting Data from Firebase
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Clear the ArrayList Before Adding Data
                list.clear();
                // Iteration to get data from each node
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    user.setUserId(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        // Adding the data to the Arraylist
                        list.add(user);
                    }

                }
                // Checking the Size of the List
                if (list.size() == 0) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Search EditText
        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data = s.toString();
                ArrayList<UserModel> searchList = new ArrayList<>();
                for (UserModel p : list) {

                    // if there is matching Exist
                    if (p.getName().toLowerCase(Locale.ROOT).startsWith(data.toLowerCase(Locale.ROOT))) {
                        searchList.add(p);
                    }

                }
                // set Adapter
                adapter = new UsersAdapter(SearchActivity.this, searchList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                binding.followRv.setHasFixedSize(true);
                binding.followRv.setLayoutManager(layoutManager);
                binding.followRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Jump from One Activity to Another
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                finish();
            }
        });


    }


}