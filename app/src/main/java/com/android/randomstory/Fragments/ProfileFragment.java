package com.android.randomstory.Fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.randomstory.Activities.FavouriteListActivity;
import com.android.randomstory.Activities.LoginScreen;
import com.android.randomstory.Activities.NotificationActivity;
import com.android.randomstory.Activities.SearchActivity;
import com.android.randomstory.Activities.SettingsActivity;
import com.android.randomstory.Activities.UpdateAvatarActivity;
import com.android.randomstory.Adapters.MyPostsAdapter;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.R;
import com.android.randomstory.databinding.FragmentProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.paperdb.Paper;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    FragmentProfileBinding binding;
    MyPostsAdapter myPostsAdapter;

    // Objects of Firebase Auth, Database
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    String indexValue;
    ArrayList<PostModel> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);


        initView();

        // Get ours Post data from Firebase
        mDatabase.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the ArrayList Before Adding Data
                list.clear();

                // Iteration to get data from each node
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // get the values and set on the post model
                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    post.setPostID(dataSnapshot.getKey());

                    // check if this is posted by me and then add to the ArrayList
                    if (post.getPostedBy().equals(mAuth.getUid())) {
                        list.add(post);

                    }

                }

                myPostsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Notifying adapter that we have some data to show after Loops end
        myPostsAdapter = new MyPostsAdapter(getContext(), list);
        binding.myPostsRecyclerView.setAdapter(myPostsAdapter);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.myPostsRecyclerView.setLayoutManager(linearLayoutManager);
        myPostsAdapter.notifyDataSetChanged();


        return binding.getRoot();
    }


    private void initView() {
        // Creating Instance of Firebase Auth, Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        Paper.init(getActivity());

        binding.avatar.setOnClickListener(this);
        binding.Username.setOnClickListener(this);
        binding.icPencil.setOnClickListener(this);
        binding.icMenu.setOnClickListener(this);


        String index = Paper.book().read("MyDp", "");

        // setting Profile Picture
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

        // setting Profile Picture based on a value get from Intent
        if (getActivity().getIntent().getStringExtra("img") != null) {

            indexValue = getActivity().getIntent().getStringExtra("img");

            switch (indexValue) {
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

            Paper.book().write("MyDp", indexValue);

        } else {
            indexValue = "";
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        String myName = Paper.book().read("name", "");

        // set Values after read from Local DB
        binding.Username.setText(myName);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.avatar) {
            Intent intent = new Intent(getContext(), UpdateAvatarActivity.class);
            startActivity(intent);
        } else if (view == binding.Username || view == binding.icPencil) {
            ChangeUserNameDialog();
        } else if (view == binding.icMenu) {

            //Popup Menu clicks
            PopupMenu popupMenu = new PopupMenu(getActivity(), binding.icMenu);

            popupMenu.getMenuInflater().inflate(R.menu.top_menu_items, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.ic_fav:
                            // Jump to another activity
                            startActivity(new Intent(getActivity(), FavouriteListActivity.class));
                            break;

                        case R.id.ic_setting:
                            // Jump to another activity
                            startActivity(new Intent(getActivity(), SettingsActivity.class));
                            break;

                        case R.id.ic_notification:
                            // Jump to another activity
                            startActivity(new Intent(getActivity(), NotificationActivity.class));
                            break;

                        case R.id.ic_logout:
                            // Logout the current user
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();
                            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });

                            // ask every time for to select google Account
                            mGoogleSignInClient.revokeAccess()
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mAuth.signOut();
                                            Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), LoginScreen.class);
                                            // Clear the stack and remove all the activities
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);

                                        }
                                    });
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
    }

    // Dialoge to change the userName
    void ChangeUserNameDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.change_name_dialog))
                .setExpanded(false)
                .setGravity(Gravity.CENTER)
                .create();
        View view = dialogPlus.getHolderView();

        Paper.book().read("name");
        EditText name_et = view.findViewById(R.id.name_et);
        name_et.setText(Paper.book().read("name"));
        Button button = view.findViewById(R.id.submitName);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserName = name_et.getText().toString();
                if (UserName.equals("")) {
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> objectHashMap = new HashMap<>();
                    objectHashMap.put("name", UserName);
                    Paper.book().write("name", UserName);

                    // update the user name on firebase
                    mDatabase.getReference().child("users").child(Objects.requireNonNull(mAuth.getUid()))
                            .updateChildren(objectHashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Name Updated", Toast.LENGTH_LONG).show();
                                    dialogPlus.dismiss();
                                    Paper.book().read("name");
                                    binding.Username.setText(UserName);
                                    // hide the keyboard
                                    try {
                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });


        dialogPlus.show();

    }


}