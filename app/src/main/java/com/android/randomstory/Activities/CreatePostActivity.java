package com.android.randomstory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.Models.PostModel;
import com.android.randomstory.databinding.ActivityCreatePostBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;

import io.paperdb.Paper;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityCreatePostBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        // Creating Instance of Firebase Authentication, RealTime Database & Storage
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.submitPost.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == binding.submitPost) {

            // if story length is less than 280 show error message
            if (binding.storyEt.getText().toString().trim().length() < 280) {
                binding.storyEt.setError("Necesitas 280 caracteeres o +, No estas en X");
                binding.storyEt.requestFocus();
            } else {
                // Create a post
                binding.spinKit.setVisibility(View.VISIBLE);
                PostModel post = new PostModel();

                // Adding values in a model by Setters
                post.setPostDescription(binding.storyEt.getText().toString());
                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                post.setPosterName(Paper.book().read("name"));
                post.setPostAt(new Date().getTime());
                post.setImageUrl(Paper.book().read("MyDp", "2"));

                // Send the Post description to Firebase RealTime Database
                database.getReference().child("posts").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
                        // Clear the Stack / All Activities
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        binding.spinKit.setVisibility(View.GONE);
                        Toast.makeText(CreatePostActivity.this, "Post Created Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                binding.spinKit.setVisibility(View.GONE);
            }

        } else if (view == binding.icBack) {
            finish();
        }
    }
}