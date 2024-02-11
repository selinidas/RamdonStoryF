package com.android.randomstory.Activities;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivitySignupScreenBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import io.paperdb.Paper;

public class SignupScreen extends AppCompatActivity implements View.OnClickListener {

    ActivitySignupScreenBinding binding;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    private static final int RC_SIGN_IN = 65;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser mUser;
    String selectedGender, index, selectedVisibility;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize Firebase Sdk
        FirebaseApp.initializeApp(SignupScreen.this);


        // Getting the intent Data and set Drawables
        if (getIntent().getStringExtra("img") != null) {

            index = getIntent().getStringExtra("img");

            switch (index) {
                case "1":
                    binding.avatar.setBackgroundResource(R.drawable.img_1);
                    break;
                case "2":
                    binding.avatar.setBackgroundResource(R.drawable.img_2);
                    break;
                case "3":
                    binding.avatar.setBackgroundResource(R.drawable.img_3);
                    break;
                case "4":
                    binding.avatar.setBackgroundResource(R.drawable.img_4);
                    break;
                case "5":
                    binding.avatar.setBackgroundResource(R.drawable.img_5);
                    break;
                case "6":
                    binding.avatar.setBackgroundResource(R.drawable.img_6);
                    break;
                case "7":
                    binding.avatar.setBackgroundResource(R.drawable.img_7);
                    break;
                case "8":
                    binding.avatar.setBackgroundResource(R.drawable.img_8);
                    break;
                case "9":
                    binding.avatar.setBackgroundResource(R.drawable.img_9);
                    break;
            }

            Paper.book().write("MyDp", index);

        } else {
            index = "";
        }

        // Function Calling
        readDataFromPaperDb();

        initViews();

        binding.avatar.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);


        googleSignInAuth();
    }

    // Read data from Local Database
    private void readDataFromPaperDb() {
        String name = Paper.book().read("name", "");
        String email = Paper.book().read("email", "");
        String password = Paper.book().read("password", "");
        String confirmPassword = Paper.book().read("confirmPassword", "");

        binding.etUsername.setText(name);
        binding.etEmail.setText(email);
        binding.etPassword.setText(password);
        binding.etRePassword.setText(confirmPassword);
    }

    private void initViews() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UsersData/");
        setupUI(binding.parentScreen);

        binding.avatar.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        binding.btnSignUp.setOnClickListener(this);
        binding.signupWithGoogle.setOnClickListener(this);


        // Spinner for gender Selection
        final String[] gender = getResources().getStringArray(R.array.gender);
        ArrayAdapter genderAdapter = (ArrayAdapter) binding.genderSpinner.getAdapter();
        genderAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.genderSpinner.setAdapter(genderAdapter);
        binding.genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = gender[position];
                Log.d("actibtsig", selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Spinner for Profile visibility Selection
        final String[] visibility = getResources().getStringArray(R.array.visibility);
        ArrayAdapter visibilityAdapter = (ArrayAdapter) binding.visibiltySpinner.getAdapter();
        visibilityAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.visibiltySpinner.setAdapter(visibilityAdapter);
        binding.visibiltySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVisibility = visibility[position];
                Log.d("actibtsig", selectedVisibility);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == binding.avatar) {
            String name = binding.etUsername.getText().toString();
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            String confirmPassword = binding.etRePassword.getText().toString();

            // Store user values to PaperDb Local Database
            Intent intent = new Intent(SignupScreen.this, ChooseAvatarActivity.class);
            Paper.book().write("name", name);
            Paper.book().write("email", email);
            Paper.book().write("password", password);
            Paper.book().write("confirmPassword", confirmPassword);

            startActivity(intent);
        } else if (view == binding.tvLogin) {
            Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (view == binding.btnSignUp) {
            String name = binding.etUsername.getText().toString();
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            String confirmPassword = binding.etRePassword.getText().toString();


            if (name.isEmpty()) {
                binding.etUsername.setError("Please Enter the Name");
                binding.etUsername.requestFocus();
            } else if (email.isEmpty()) {
                binding.etEmail.setError("Please Enter the Email");
                binding.etEmail.requestFocus();
            } else if (password.isEmpty()) {
                binding.etPassword.setError("Please Enter the Password");
                binding.etPassword.requestFocus();
            } else if (index == null || index.equals("")) {
                Toast.makeText(getApplicationContext(), "Please select a profile picture", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                binding.etPassword.setError("Password not matched");
                binding.etPassword.requestFocus();

                binding.etRePassword.setError("Password not matched");
                binding.etRePassword.requestFocus();
            } else {
                //create account with email & password
                createUserAccount(email, password);

                //set visibility of progress bar to Visible
                binding.spinKit.setVisibility(View.VISIBLE);
            }
            // Signup with Google
        } else if (view == binding.signupWithGoogle) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

            //set visibility of progress bar to Visible
            binding.spinKit.setVisibility(View.VISIBLE);
        }

    }

    // For continue with Google
    private void googleSignInAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("976272208049-sc02do6rt5g2fm1aadbbhqs5d1c3ghh8.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // For registration with Email and Password
    private void createUserAccount(String Email, String Password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.spinKit.setVisibility(View.GONE);
                            String name = binding.etUsername.getText().toString();
                            String email = binding.etEmail.getText().toString();
                            String password = binding.etPassword.getText().toString();
                            String UserId = mAuth.getUid();

                            String id = task.getResult().getUser().getUid();

                            Paper.book().write("name", name);

                            // Check the visibility of the profile
                            if (selectedVisibility.equals("Private")) {
                                selectedVisibility = "1";
                            } else {
                                selectedVisibility = "0";
                            }

                            // write data to paperDb (Local Database)
                            Paper.book().write("visibility", selectedVisibility);


                            UserModel userModel = new UserModel(name, email,
                                    password, selectedGender, UserId, index, selectedVisibility);

                            mDatabase.getReference().child("users").child(id).setValue(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupScreen.this, e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("TAG51851", "onFailure: " + e.getMessage());
                                        }
                                    });


                            //show Successful Toast Message
                            Toast.makeText(SignupScreen.this, "" + "Account is created successfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupScreen.this, MainActivity.class);

                            // clear Stack or remove all activities from stack
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish(); // remove the previous activity

                        } else {
                            Toast.makeText(SignupScreen.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            binding.spinKit.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //check Internet Connectivity
    @SuppressLint("MissingPermission")
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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
                    Log.d("TAG9999", "onComplete: " + e.getMessage());
                    binding.spinKit.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(SignupScreen.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                binding.spinKit.setVisibility(View.GONE);
            }
        }

    }
    // Continue with google
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

                                UserModel userModel = new UserModel(displayName, email,
                                        "withGoogle", "Male", UserId, "1", "0");
                                // 0 Means Public
                                // 1 Means Private = Anonymous

                                Paper.book().write("MyDp", "1");
                                Paper.book().write("name", displayName);
                                mDatabase.getReference().child("users").child(Objects.requireNonNull(mAuth.getUid())).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("TAG21555", "onFailure: " + e.getMessage());
                                    }
                                });

                                Log.d("TAG15154", "onComplete: Data " + " " + email + " " + displayName);
                                binding.spinKit.setVisibility(View.GONE);
                                Toast.makeText(SignupScreen.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupScreen.this, MainActivity.class));
                            } else {
                                binding.spinKit.setVisibility(View.GONE);
                                Toast.makeText(SignupScreen.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG41544", "onComplete: " + task.getException().getMessage());
                            }
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(SignupScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                            Toast.makeText(SignupScreen.this, "Check Internet Connection",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    // Hiding the Keyboard
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

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SignupScreen.this);
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
}