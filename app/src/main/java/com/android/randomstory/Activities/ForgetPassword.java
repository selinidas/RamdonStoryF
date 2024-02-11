package com.android.randomstory.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {

    ActivityForgetPasswordBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

    }

    private void initView() {

        mAuth = FirebaseAuth.getInstance();

        binding.backtologin.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);

        setupUI(binding.parentScreen);
    }


    // For hiding the Keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    // For hiding the Keyboard
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ForgetPassword.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.backtologin) {
            finish();
        } else if (view == binding.btnSubmit) {
            String email = binding.etEmail.getText().toString().trim();

            // Validating if the user fields are empty or filled
            if (email.isEmpty()) {
                binding.etEmail.setError("Email is required");
                binding.etEmail.requestFocus();
            } else {
                // For resetting the Password
                binding.spinKit.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.spinKit.setVisibility(View.GONE);
                            binding.etEmail.setText("");
                            Toast.makeText(ForgetPassword.this, "Check Email to reset password", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        }

    }
}