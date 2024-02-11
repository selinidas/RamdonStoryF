package com.android.randomstory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivityChooseAvatarBinding;

public class ChooseAvatarActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityChooseAvatarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseAvatarBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        //Function Calling
        SetDrawableResource();

    }

    // Setting Images on ImageViews
    //Function Definition
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


        //Interface onClick Listeners
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


    //onclick Functions
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

    // Send selected image to Signup Screen for User profile image
    void startIntent(String index) {
        Intent intent = new Intent(ChooseAvatarActivity.this, SignupScreen.class);
        intent.putExtra("img", index);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}