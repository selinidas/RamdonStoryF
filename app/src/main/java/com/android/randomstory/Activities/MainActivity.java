package com.android.randomstory.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.randomstory.Fragments.HomeFragment;
import com.android.randomstory.Fragments.ProfileFragment;
import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TOKEN_DATA = "TokenData";
    private static final String TOKEN_KEY = "token";
    private static final String NAN = "NAN";

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        saveToken();
        setInitialFragment();
        setFabClickListener();
        setBottomNavigationListener();
    }

    private void setInitialFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.main_layout) == null) {
            replaceFragment(new HomeFragment());
        }
    }

    private void setFabClickListener() {
        binding.fab.setOnClickListener(this);
    }

    private void setBottomNavigationListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.ic_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.ic_profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public void onClick(View view) {
        if (view == binding.fab) {
            startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
        }
    }

    private void saveToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(TOKEN_DATA, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, NAN);
        if (!NAN.equals(token)) {
            updateTokenOnFirebase(token);
        }
    }

    private void updateTokenOnFirebase(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Tokens").child(FirebaseAuth.getInstance().getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put(TOKEN_KEY, token);
        reference.updateChildren(map);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, fragment)
                .commit();
    }
}