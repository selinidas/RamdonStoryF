package com.android.randomstory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivityUpdateAvatarBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import io.paperdb.Paper;

public class UpdateAvatarActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUpdateAvatarBinding binding;

    // Objects of Firebase Auth, Database

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateAvatarBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        initViews();

    }


    // Setting images to ImageViews
    void SetDrawableResource() {
        binding.avatar1.setBackgroundResource(R.drawable.img_1);
        binding.avatar2.setBackgroundResource(R.drawable.img_2);
        binding.avatar3.setBackgroundResource(R.drawable.img_3);
        binding.avatar4.setBackgroundResource(R.drawable.img_4);
        binding.avatar5.setBackgroundResource(R.drawable.img_5);
        binding.avatar6.setBackgroundResource(R.drawable.img_6);
        binding.avatar7.setBackgroundResource(R.drawable.img_7);
        binding.avatar8.setBackgroundResource(R.drawable.img_8);
        binding.avatar9.setBackgroundResource(R.drawable.img_9);
    }

    private void initViews() {

        Paper.init(getApplicationContext());

        SetDrawableResource();

        // Creating Instance of Firebase Auth, Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        binding.avatar1.setOnClickListener(this);
        binding.avatar2.setOnClickListener(this);
        binding.avatar3.setOnClickListener(this);
        binding.avatar4.setOnClickListener(this);
        binding.avatar5.setOnClickListener(this);
        binding.avatar6.setOnClickListener(this);
        binding.avatar7.setOnClickListener(this);
        binding.avatar8.setOnClickListener(this);
        binding.avatar9.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.avatar1) {
            startIntent("1");
        } else if (view == binding.avatar2) {
            startIntent("2");
        } else if (view == binding.avatar3) {
            startIntent("3");
        } else if (view == binding.avatar4) {
            startIntent("4");
        } else if (view == binding.avatar5) {
            startIntent("5");
        } else if (view == binding.avatar6) {
            startIntent("6");
        } else if (view == binding.avatar7) {
            startIntent("7");
        } else if (view == binding.avatar8) {
            startIntent("8");
        } else if (view == binding.avatar9) {
            startIntent("9");
        }

    }

    // For updating the User Profile Image
    void startIntent(String index) {
        Intent intent = new Intent(UpdateAvatarActivity.this, MainActivity.class);
        intent.putExtra("img", index);
        Paper.book().write("MyDp", index);
        HashMap<String, Object> objectHashMap = new HashMap<>();

        objectHashMap.put("imageUrl", index);

        // update the values on Firebase database on the node of the current User named "users"
        mDatabase.getReference().child("users").child(Objects.requireNonNull(mAuth.getUid()))
                .updateChildren(objectHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateAvatarActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateAvatarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Clear the stack or remove all the activities from stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}