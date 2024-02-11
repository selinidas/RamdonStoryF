package com.android.randomstory.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Adapters.CommentsAdapter;
import com.android.randomstory.Adapters.LikesAdapter;
import com.android.randomstory.Models.CommentModel;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.R;
import com.android.randomstory.Utils.Constant;
import com.android.randomstory.databinding.ActivityDetailPostBinding;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import io.paperdb.Paper;

public class DetailPostActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityDetailPostBinding binding;
    ArrayList<CommentModel> listComment = new ArrayList<>();
    ArrayList<String> likeList = new ArrayList<String>();

    // Firebase Auth and Database Object
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    int totalComments, totalLikes;
    PostModel postModel;
    CommentsAdapter commentsAdapter;

    String likedBy;
    String postID = "empty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();

        // If coming, after clicking the Notification
        postID = Paper.book().read("NotificationData", "nothing");

        if (postID.equals("nothing")) {

            // if Coming from the Notification History, get data from that Activity
            if (getIntent().getStringExtra("postId") != null) {
                postID = getIntent().getStringExtra("postId");
            } else {
                postID = "empty";
            }
        }


        Log.d("TAGsdsdsdf", "onCreate: " + postID);

        // get Data of the Post From Database (Firebase)
        if (!postID.equals("empty")) {
            mDatabase.getReference().child("posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    PostModel postModel1 = snapshot.getValue(PostModel.class);
                    binding.storytext.setText(postModel1.getPostDescription());
                    Paper.book().delete("NotificationData");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        binding.icBack.setOnClickListener(this);
        binding.likeBtn.setOnClickListener(this);
        binding.commentBtn.setOnClickListener(this);


        // If coming from my post Activity
        if (getIntent().getSerializableExtra("post_detail") != null) {
            postModel = (PostModel) getIntent().getSerializableExtra("post_detail");
            Log.d("TAGfddfg", "onCreate: " + postModel.toString());
            binding.storytext.setText(postModel.getPostDescription());
        }

    }

    private void initViews() {
// Creating Instance of Firebase Authentication, RealTime Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

    }

    // Bottom Sheet Button
    @SuppressLint("NotifyDataSetChanged")
    private void showBottomSheetDialog(PostModel postModel) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailPostActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        RecyclerView commentRv = bottomSheetDialog.findViewById(R.id.commentRv);

        // Finding Views of Bottom Sheet XML file
        ImageView ic_cross = bottomSheetDialog.findViewById(R.id.ic_cross);
        ImageView sendComment = bottomSheetDialog.findViewById(R.id.sendComment);
        ImageView avatar = bottomSheetDialog.findViewById(R.id.avatar);
        TextView total_comments = bottomSheetDialog.findViewById(R.id.total_comments);


        TextView addcomment = bottomSheetDialog.findViewById(R.id.addcomment);
        SpinKitView spin_kit = bottomSheetDialog.findViewById(R.id.spin_kit);


        // Show Avatar Static Function
        Constant.ShowDrawableImage(avatar);


        // Get data from Firebase Realtime Database
        mDatabase.getReference().child("posts")
                .child(postModel.getPostID()).child("comments").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // getting data from firebase of Each child from 'Post' node
                        listComment.clear(); // clear the Arraylist before adding data
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            totalComments = Math.toIntExact(snapshot.getChildrenCount());
                            CommentModel comment = dataSnapshot.getValue(CommentModel.class);
                            listComment.add(comment);
                        }
                        // Setting Text to 0 if there is no Child or no Comments Posted on a post
                        if (totalComments == 0) {
                            total_comments.setText("0 Comments");
                        } else {
                            total_comments.setText(String.valueOf(totalComments) + " Comments");
                        }

                        commentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        spin_kit.setVisibility(View.GONE);
                    }
                });


        // setting adapter with Arraylist that we have some data to show after Loops end

        commentsAdapter = new CommentsAdapter(listComment, this);
        commentRv.setAdapter(commentsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRv.setLayoutManager(linearLayoutManager);
        commentsAdapter.notifyDataSetChanged();
        spin_kit.setVisibility(View.GONE);


        // Hide the bottom Sheet
        ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        // Send button to post the comment
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // post the Comment and send the Content to Firebase
                CommentModel comment = new CommentModel();
                comment.setCommentBody(addcomment.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

                // sending comments to Firebase
                mDatabase.getReference().child("posts").child(postModel.getPostID()).child("comments").push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mDatabase.getReference().child("posts")
                                        .child(postModel.getPostedBy()).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                // Counting the total child of Comment count to show Total Comments
                                                int commentCount = 0;

                                                // Checking if any child exists or not
                                                if (snapshot.exists()) {
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                mDatabase.getReference().child("posts")
                                                        .child(postModel.getPostID()).child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                addcomment.setText("");
                                                                Toast.makeText(DetailPostActivity.this, "Commented", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });

                // Dismiss the bottom Sheet
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }

    private void showBottomSheetDialog2() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailPostActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        RecyclerView commentRv = bottomSheetDialog.findViewById(R.id.commentRv);

        ImageView ic_cross = bottomSheetDialog.findViewById(R.id.ic_cross);
        ImageView sendComment = bottomSheetDialog.findViewById(R.id.sendComment);
        ImageView avatar = bottomSheetDialog.findViewById(R.id.avatar);
        TextView total_comments = bottomSheetDialog.findViewById(R.id.total_comments);


        TextView addcomment = bottomSheetDialog.findViewById(R.id.addcomment);
        SpinKitView spin_kit = bottomSheetDialog.findViewById(R.id.spin_kit);


        Constant.ShowDrawableImage(avatar);


        mDatabase.getReference().child("posts")
                .child(postID).child("comments").addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listComment.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            totalComments = Math.toIntExact(snapshot.getChildrenCount());
                            CommentModel comment = dataSnapshot.getValue(CommentModel.class);
                            listComment.add(comment);
                        }
                        if (totalComments == 0) {
                            total_comments.setText("0 Comments");
                        } else {
                            total_comments.setText(String.valueOf(totalComments) + " Comments");
                        }

                        commentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        spin_kit.setVisibility(View.GONE);
                    }
                });


        commentsAdapter = new CommentsAdapter(listComment, this);
        commentRv.setAdapter(commentsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentRv.setLayoutManager(linearLayoutManager);
        commentsAdapter.notifyDataSetChanged();
        spin_kit.setVisibility(View.GONE);


        ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CommentModel comment = new CommentModel();
                comment.setCommentBody(addcomment.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

                mDatabase.getReference().child("posts").child(postID).child("comments").push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                                mDatabase.getReference().child("posts")
                                        .child(postID).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                int commentCount = 0;

                                                if (snapshot.exists()) {
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                mDatabase.getReference().child("posts")
                                                        .child(postID).child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                addcomment.setText("");
                                                                Toast.makeText(DetailPostActivity.this, "Commented", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }


    // Bottom Sheet Dialog for Likes
    private void showBottomSheetDialogLike() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.like_bottom_sheet_dialog);


        RecyclerView likeRv = bottomSheetDialog.findViewById(R.id.likeRv);
        ImageView ic_cross = bottomSheetDialog.findViewById(R.id.ic_cross);
        TextView countLikes = bottomSheetDialog.findViewById(R.id.countLikes);


        // Send the likes to Firebase Database
        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(postModel.getPostID()).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        likeList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            totalLikes = Math.toIntExact(snapshot.getChildrenCount());
                            likedBy = dataSnapshot.getKey();
                            likeList.add(likedBy);
                            Log.d("dgsgsfdgf", "onDataChange: " + likedBy);
                        }

                        if (totalLikes == 0) {
                            countLikes.setText("0 Likes");
                        } else {
                            countLikes.setText(String.valueOf(totalLikes) + " Likes");
                        }

                        LikesAdapter likesAdapter = new LikesAdapter(DetailPostActivity.this, likeList);
                        likeRv.setAdapter(likesAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailPostActivity.this);
                        likeRv.setLayoutManager(linearLayoutManager);
                        likesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }

    private void showBottomSheetDialogLike2() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.like_bottom_sheet_dialog);


        RecyclerView likeRv = bottomSheetDialog.findViewById(R.id.likeRv);
        ImageView ic_cross = bottomSheetDialog.findViewById(R.id.ic_cross);
        TextView countLikes = bottomSheetDialog.findViewById(R.id.countLikes);


        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(postID).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        likeList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            totalLikes = Math.toIntExact(snapshot.getChildrenCount());
                            likedBy = dataSnapshot.getKey();
                            likeList.add(likedBy);
                            Log.d("dgsgsfdgf", "onDataChange: " + totalLikes);
                        }

                        if (totalLikes == 0) {
                            countLikes.setText("0 Likes");
                        } else {
                            countLikes.setText(String.valueOf(totalLikes) + " Likes");
                        }
                        LikesAdapter likesAdapter = new LikesAdapter(DetailPostActivity.this, likeList);
                        likeRv.setAdapter(likesAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailPostActivity.this);
                        likeRv.setLayoutManager(linearLayoutManager);
                        likesAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        ic_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {

        if (view == binding.likeBtn) {
            // if the intent coming from another activity is empty then do following checks
            if (!postID.equals("empty")) {
                showBottomSheetDialogLike2();
            } else {
                showBottomSheetDialogLike();

            }

        } else if (view == binding.commentBtn) {

            if (!postID.equals("empty")) {
                showBottomSheetDialog2();

            } else {
                showBottomSheetDialog(postModel);

            }

        } else if (view == binding.icBack) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            // clearing the Stack
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}