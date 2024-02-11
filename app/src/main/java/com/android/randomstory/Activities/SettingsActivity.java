package com.android.randomstory.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySettingsBinding binding;
    // Objects of Firebase Auth, Database
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    String ProfileVisibility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        initView();

    }

    private void initView() {
        // Creating Instance of Firebase Auth, Database
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        binding.privateBtn.setOnClickListener(this);
        binding.publicBtn.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);

        // geting data from Firebase database from the node of current User named "users"
        mDatabase.getReference().child("users").child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // get the node value and store in the model;

                        UserModel userModel = snapshot.getValue(UserModel.class);

                        // get the value of Visibility
                        ProfileVisibility = userModel.getVisibility();

                        Log.d("TAGddsd", "onDataChange: " + ProfileVisibility);


                        // changing the colors of the buttons based on the value
                        if (ProfileVisibility.equals("0")) {
                            binding.publicBtn.setBackgroundResource(R.drawable.btn_design_two);
                            binding.publicBtn.setTextColor(Color.parseColor("#FFFFFFFF"));//white

                            binding.privateBtn.setTextColor(Color.parseColor("#0177EC"));//blue
                            binding.privateBtn.setBackgroundColor(Color.parseColor("#00000000"));//white

                        } else if (ProfileVisibility.equals("1")) {
                            binding.privateBtn.setBackgroundResource(R.drawable.btn_design_two);
                            binding.privateBtn.setTextColor(Color.parseColor("#FFFFFFFF"));

                            binding.publicBtn.setTextColor(Color.parseColor("#0177EC"));
                            binding.publicBtn.setBackgroundColor(Color.parseColor("#00000000"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    void SetValue(String status) {
        // getting data from the firebase from the "users" Node of current User
        mDatabase.getReference().child("users").child(mAuth.getUid())
                .child("visibility")
                .setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == binding.publicBtn) {
            binding.publicBtn.setBackgroundResource(R.drawable.btn_design_two);
            binding.publicBtn.setTextColor(Color.parseColor("#FFFFFFFF"));//white

            binding.privateBtn.setTextColor(Color.parseColor("#0177EC"));//blue
            binding.privateBtn.setBackgroundColor(Color.parseColor("#00000000"));//white
            ProfileVisibility = "0";
            SetValue(ProfileVisibility);

        } else if (view == binding.privateBtn) {
            binding.privateBtn.setBackgroundResource(R.drawable.btn_design_two);
            binding.privateBtn.setTextColor(Color.parseColor("#FFFFFFFF"));

            binding.publicBtn.setTextColor(Color.parseColor("#0177EC"));
            binding.publicBtn.setBackgroundColor(Color.parseColor("#00000000"));

            ProfileVisibility = "1";
            SetValue(ProfileVisibility);
        } else if (view == binding.icBack) {
            finish();
        }
    }
}