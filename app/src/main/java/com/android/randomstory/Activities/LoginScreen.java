package com.android.randomstory.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.Models.UserModel;
import com.android.randomstory.databinding.ActivityLoginScreenBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import io.paperdb.Paper;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginScreenBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser mUser;
    private static final int RC_SIGN_IN = 65;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

    }

    // For checking the Network Connectivity
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    // For Registration with Google
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        try {
                            if (task.isSuccessful()) {
                                mUser = mAuth.getCurrentUser();
                                assert mUser != null;
                                String email = mUser.getEmail();
                                String displayName = mUser.getDisplayName();
                                String UserId = mAuth.getUid();

                                Paper.book().write("MyDp", "1");
                                Paper.book().write("name", displayName);
                                UserModel userModel = new UserModel(displayName, email,
                                        "withGoogle", "Male", UserId, "1", "0");

                                // 0 Means Public
                                // 1 Means Private = Anonymous

                                mDatabase.getReference().child("users").child(Objects.requireNonNull(mAuth.getUid())).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("TAG21555", "onFailure: " + e.getMessage());
                                    }
                                });

                                Log.d("TAG15154", "onComplete: Data " + " " + email + " " + displayName);
                                binding.spinKit.setVisibility(View.GONE);
                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginScreen.this, MainActivity.class));
                            } else {
                                binding.spinKit.setVisibility(View.GONE);
                                Toast.makeText(LoginScreen.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG41544", "onComplete: " + task.getException().getMessage());
                            }
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                            Toast.makeText(LoginScreen.this, "Check Internet Connection",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void LoginUser(String Email, String Password) {
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.spinKit.setVisibility(View.GONE);
                            Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginScreen.this, MainActivity.class));
                            finish();
                        } else {
                            binding.spinKit.setVisibility(View.GONE);
                            Log.w("TAG", "signInWithEmail:failure", task.getException());

                            Toast.makeText(LoginScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            binding.etEmail.setText(Email);
                            binding.etPassword.setText(Password);
                        }

                        binding.spinKit.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.spinKit.setVisibility(View.GONE);
                        Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (isNetworkConnected()) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                    binding.spinKit.setVisibility(View.GONE);
                } catch (ApiException e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG7777", "onComplete: " + task.getException().getMessage());
                    binding.spinKit.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(LoginScreen.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                binding.spinKit.setVisibility(View.GONE);
            }
        }

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
                    hideSoftKeyboard(LoginScreen.this);
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


    // For google Sign-In
    private void googleSignInAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("976272208049-sc02do6rt5g2fm1aadbbhqs5d1c3ghh8.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // Initialize values
    private void initView() {

        // Creating Instance of Firebase Authentication, RealTime Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        setupUI(binding.parentScreen);
        googleSignInAuth();

        binding.btnLogin.setOnClickListener(this);
        binding.tvSignUp.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.signupWithGoogle.setOnClickListener(this);

        mUser = mAuth.getCurrentUser();

        // Checking if the user has ever enter login info
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginScreen.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnLogin) {

            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();

            // Validating if the user fields are empty or filled
            if (email.isEmpty()) {
                binding.etEmail.setError("Please Enter the Email");
                binding.etEmail.requestFocus();
            } else if (password.isEmpty()) {
                binding.etPassword.setError("Please Enter the Password");
                binding.etPassword.requestFocus();
            } else {
                binding.spinKit.setVisibility(View.VISIBLE);
                // Login User Function Calling
                LoginUser(email, password);
            }
        } else if (view == binding.tvSignUp) {
            // textViews, if click, goto the requested Screens
            Intent intent = new Intent(LoginScreen.this, SignupScreen.class);
            startActivity(intent);

        } else if (view == binding.tvForgotPassword) {
            // textViews, if click, goto the requested Screens

            Intent intent = new Intent(LoginScreen.this, ForgetPassword.class);
            startActivity(intent);

        } else if (view == binding.signupWithGoogle) {
            // textViews, start authtication with Google
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
            binding.spinKit.setVisibility(View.VISIBLE);
        }
    }
}