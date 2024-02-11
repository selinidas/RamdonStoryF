package com.android.randomstory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.randomstory.Adapters.MyPostsAdapter;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.Models.UserModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.ActivityOtherUserProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import io.paperdb.Paper;

public class OtherUserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityOtherUserProfileBinding binding;
    FirebaseDatabase mDatabase;
    MyPostsAdapter myPostsAdapter;
    FirebaseAuth mAuth;
    PostModel postModel;
    String PosterName;
    String Anonymous;

    //ArrayLists
    ArrayList<PostModel> favItems = new ArrayList<>();
    ArrayList<PostModel> list;
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserProfileBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        // Read Data from Paper DB
        favItems = Paper.book().read("FavList", new ArrayList<>());

        // Function Calling
        initViews();
        Log.d("TAGfav", "onClick: " + Paper.book().read("FavList"));

        // Check If we get something from Previous Activity through Intent
        if (getIntent().getSerializableExtra("UserModel") != null) {
            userModel = (UserModel) getIntent().getSerializableExtra("UserModel");

            // Set values
            checkAnonymous(userModel.getUserId());
            getDataOfTheUser(userModel.getUserId());
            Log.d("TAGuserID", "UserModel: " + userModel.getUserId());
            Log.d("TAG555", "onCreate: " + userModel.toString());
        }

        if (getIntent().getSerializableExtra("PostModel") != null) {
            postModel = (PostModel) getIntent().getSerializableExtra("PostModel");
            Log.d("TAGpostMode", "onCreate: " + postModel.toString());

            Log.d("TAGuserID", "PostModel: " + postModel.getPostedBy());
            checkAnonymous(postModel.getPostedBy());
            getDataOfTheUser(postModel.getPostedBy());

        }


    }

    // Get Data from Firebase from the node named Users
    private void checkAnonymous(String UserID) {
        mDatabase.getReference().child("users").child(UserID).child("visibility").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Anonymous = String.valueOf(snapshot.getValue());
                Log.d("TAGanonymous", "An: " + Anonymous);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Get Data from Firebase from the node named Posts
    private void getDataOfTheUser(String UserID) {
        mDatabase.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the ArrayList Before Adding Data
                list.clear();

                // Iteration to get data from each node
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    post.setPostID(dataSnapshot.getKey());

                    if (post.getPostedBy().equals(UserID)) {
                        Log.d("TAGffsd", "onDataChange: " + UserID);

                        // checking if the Profile Visibility is Public or Private
                        if (Anonymous.equals("0")) {
                            list.add(post);
                            PosterName = post.getPosterName();
                        } else {
                            binding.AnonymousProfile.setVisibility(View.VISIBLE);
                        }

                    }

                }

                // settting profile Picture
                if (getIntent().getSerializableExtra("PostModel") != null) {
                    String profilePicture = postModel.getImageUrl();

                    switch (Objects.requireNonNull(profilePicture)) {
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

                    binding.Username.setText(PosterName);

                    myPostsAdapter.notifyDataSetChanged();
                }

                // settting profile Picture
                else if (getIntent().getSerializableExtra("UserModel") != null) {
                    String profilePicture = userModel.getImageUrl();

                    switch (Objects.requireNonNull(profilePicture)) {
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

                    binding.Username.setText(userModel.getName());

                    myPostsAdapter.notifyDataSetChanged();

                }

                binding.spinKit.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.spinKit.setVisibility(View.GONE);
            }
        });



        // Notifying adapter that we have some data to show after Loops end
        myPostsAdapter = new MyPostsAdapter(OtherUserProfileActivity.this, list);
        binding.myPostsRecyclerView.setAdapter(myPostsAdapter);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(OtherUserProfileActivity.this, 3);
        binding.myPostsRecyclerView.setLayoutManager(linearLayoutManager);
        myPostsAdapter.notifyDataSetChanged();

    }

    // Initialize All Views
    private void initViews() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        binding.icMenu.setOnClickListener(this);
        binding.favBtn.setOnClickListener(this);
        binding.icBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == binding.icMenu) {

            // show Menu of Top Navigation
            PopupMenu popupMenu = new PopupMenu(OtherUserProfileActivity.this, binding.icMenu);

            popupMenu.getMenuInflater().inflate(R.menu.fav_tab_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.favList:
                            startActivity(new Intent(OtherUserProfileActivity.this, FavouriteListActivity.class));
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        } else if (view == binding.favBtn) {

            // get data through Intent
            if (getIntent().getSerializableExtra("UserModel") != null) {
                boolean isContain = false;
                for (int i = 0; i < favItems.size(); i++) {
                    // To check if we have the ID In the ArrayList
                    if (favItems.get(i).getPostedBy().equalsIgnoreCase(userModel.getUserId())) {
                        isContain = true;
                        break;
                    }
                    Log.d("TAG3365465", "addToPaper :-->" + i + " " + favItems.get(i));
                }

                // Checking the profile Visibility Public Or Private
                if (!Anonymous.equals("1")) {
                    if (!isContain) {
                        PostModel postModel = new PostModel();
                        // Setting values in the model
                        postModel.setImageUrl(userModel.getImageUrl());
                        postModel.setPostedBy(userModel.getUserId());
                        postModel.setPosterName(userModel.getName());
                        favItems.add(postModel);
                        Paper.book().write("FavList", favItems);

                        // Sending Data to Firebase Database
                        mDatabase.getReference().child("users").child(mAuth.getUid()).child("favLists").setValue(favItems).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OtherUserProfileActivity.this, "Added To Favourites", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(OtherUserProfileActivity.this, "Already Added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtherUserProfileActivity.this, "This profile is private", Toast.LENGTH_SHORT).show();
                }

                // get data through Intent
            } else if (getIntent().getSerializableExtra("PostModel") != null) {
                boolean isContain = false;
                // To check if we have the ID In the ArrayList
                for (int i = 0; i < favItems.size(); i++) {
                    if (favItems.get(i).getPostedBy().equalsIgnoreCase(postModel.getPostedBy())) {
                        isContain = true;
                        break;
                    }
                    Log.d("TAG3365465", "addToPaper :-->" + i + " " + favItems.get(i));
                }

                // Checking the profile Visibility Public Or Private
                if (!Anonymous.equals("1")) {
                    if (!isContain) {
                        favItems.add(postModel);
                        Paper.book().write("FavList", favItems);
                        // Sending Data to Firebase Database
                        mDatabase.getReference().child("users").child(mAuth.getUid()).child("favLists").setValue(favItems).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(OtherUserProfileActivity.this, "Added To Favourites", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(OtherUserProfileActivity.this, "Already Added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OtherUserProfileActivity.this, "This profile is private", Toast.LENGTH_SHORT).show();
                }
            }


        } else if (view == binding.icBack) {
            finish();
        }
    }
}