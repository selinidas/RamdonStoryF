package com.android.randomstory.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.randomstory.Activities.OtherUserProfileActivity;
import com.android.randomstory.Models.CommentModel;
import com.android.randomstory.Models.NotificationModel;
import com.android.randomstory.Models.PostModel;
import com.android.randomstory.Models.UserModel;
import com.android.randomstory.Notification.APIService;
import com.android.randomstory.Notification.Client;
import com.android.randomstory.Notification.Data;
import com.android.randomstory.Notification.MyResponse;
import com.android.randomstory.Notification.Sender;
import com.android.randomstory.Notification.Token;
import com.android.randomstory.R;
import com.android.randomstory.Utils.Constant;
import com.android.randomstory.databinding.StorySingleItemLayoutBinding;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    FirebaseAuth auth;
    FirebaseDatabase database;
    Context context;
    ArrayList<PostModel> list;
    CommentsAdapter commentsAdapter;
    APIService apiService;
    UserModel userModel;
    ArrayList<CommentModel> listComment = new ArrayList<>();
    int totalComments;

    String username;

    public PostAdapter(Context context, ArrayList<PostModel> list) {
        this.list = list;
        this.context = context;
        Paper.init(context);
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_single_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {


        // Getting all the data and show on the recyclerView from Firebase
        PostModel postModel = list.get(position);
        holder.binding.postDescription.setText(postModel.getPostDescription());
        holder.binding.posterName.setText(postModel.getPosterName());

        String profilePicture = postModel.getImageUrl();

        switch (Objects.requireNonNull(profilePicture)) {
            case "1":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_1);
                break;
            case "2":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_2);
                break;
            case "3":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_3);
                break;
            case "4":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_4);
                break;
            case "5":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_5);
                break;
            case "6":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_6);
                break;
            case "7":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_7);
                break;
            case "8":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_8);
                break;
            case "9":
                holder.binding.avatar.setBackgroundResource(R.drawable.img_9);
                break;
        }

        holder.binding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra("PostModel", postModel);
                context.startActivity(intent);
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        Paper.init(context);
        username = Paper.book().read("name", "");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        holder.binding.postDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.binding.postDescription.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        holder.binding.postDescription.setMovementMethod(new ScrollingMovementMethod());


        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(postModel.getPostID()).child("likes")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_heart_red, 0, 0, 0);
                            holder.binding.likeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference().child("posts").child(postModel.getPostID())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference().child("posts").child(postModel.getPostID())
                                                            .child("postLike").setValue(postModel.getPostLike() - 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_heart, 0, 0, 0);

                                                                }
                                                            });

                                                }
                                            });
                                }
                            });


                        } else {
                            holder.binding.likeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FirebaseDatabase.getInstance().getReference().child("posts").child(postModel.getPostID())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference().child("posts").child(postModel.getPostID())
                                                            .child("postLike").setValue(postModel.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.likeBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_heart_red, 0, 0, 0);

                                                                    NotificationModel notificationModel = new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                                    notificationModel.setPostID(postModel.getPostID());
                                                                    notificationModel.setPostedBy(postModel.getPostedBy());
                                                                    notificationModel.setType("Story liked");

                                                                    if (!postModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid())) {

                                                                        FirebaseDatabase.getInstance().getReference().child("notification")
                                                                                .child(postModel.getPostedBy()).push().setValue(notificationModel);
                                                                    }


                                                                    if (!notificationModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid())) {
                                                                        sendNotifiaction(notificationModel.getPostedBy(), username + " Liked your post", notificationModel.getType(), postModel.getPostID());
                                                                    }


                                                                }
                                                            });

                                                }
                                            });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(postModel);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        StorySingleItemLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = StorySingleItemLayoutBinding.bind(itemView);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showBottomSheetDialog(PostModel postModel) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        RecyclerView commentRv = bottomSheetDialog.findViewById(R.id.commentRv);

        ImageView ic_cross = bottomSheetDialog.findViewById(R.id.ic_cross);
        ImageView sendComment = bottomSheetDialog.findViewById(R.id.sendComment);
        ImageView avatar = bottomSheetDialog.findViewById(R.id.avatar);
        TextView total_comments = bottomSheetDialog.findViewById(R.id.total_comments);


        TextView addcomment = bottomSheetDialog.findViewById(R.id.addcomment);
        SpinKitView spin_kit = bottomSheetDialog.findViewById(R.id.spin_kit);


        FirebaseDatabase.getInstance().getReference()
                .child("users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        userModel = snapshot.getValue(UserModel.class);
                        String profilePicture = userModel.getImageUrl();

                        switch (Objects.requireNonNull(profilePicture)) {
                            case "1":
                                avatar.setBackgroundResource(R.drawable.img_1);
                                break;
                            case "2":
                                avatar.setBackgroundResource(R.drawable.img_2);
                                break;
                            case "3":
                                avatar.setBackgroundResource(R.drawable.img_3);
                                break;
                            case "4":
                                avatar.setBackgroundResource(R.drawable.img_4);
                                break;
                            case "5":
                                avatar.setBackgroundResource(R.drawable.img_5);
                                break;
                            case "6":
                                avatar.setBackgroundResource(R.drawable.img_6);
                                break;
                            case "7":
                                avatar.setBackgroundResource(R.drawable.img_7);
                                break;
                            case "8":
                                avatar.setBackgroundResource(R.drawable.img_8);
                                break;
                            case "9":
                                avatar.setBackgroundResource(R.drawable.img_9);
                                break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Constant.ShowDrawableImage(avatar);

        database.getReference().child("posts")
                .child(postModel.getPostID()).child("comments").addValueEventListener(new ValueEventListener() {
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


        commentsAdapter = new CommentsAdapter(listComment, context);
        commentRv.setAdapter(commentsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
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

                database.getReference().child("posts").child(postModel.getPostID()).child("comments").push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                                database.getReference().child("posts")
                                        .child(postModel.getPostedBy()).child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                int commentCount = 0;

                                                if (snapshot.exists()) {
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                database.getReference().child("posts")
                                                        .child(postModel.getPostID()).child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                addcomment.setText("");
                                                                Toast.makeText(context, "Commented", Toast.LENGTH_SHORT).show();

                                                                NotificationModel notificationModel = new NotificationModel();
                                                                notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notificationModel.setNotificationAt(new Date().getTime());
                                                                notificationModel.setPostID(postModel.getPostID());
                                                                notificationModel.setPostedBy(postModel.getPostedBy());
                                                                notificationModel.setType("Story Commented");

                                                                if (!postModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid())) {

                                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                                            .child(postModel.getPostedBy()).push().setValue(notificationModel);
                                                                }


                                                                if (!notificationModel.getPostedBy().equals(FirebaseAuth.getInstance().getUid())) {
                                                                    sendNotifiaction(notificationModel.getPostedBy(), username + " Comment on your post", notificationModel.getType(), postModel.getPostID());
                                                                }


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

    private void sendNotifiaction(String receiver, final String username, final String type, String ID) {


        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");


        Log.d("TAGee", "sendNotifiaction: " + tokens);

        Query query = tokens.orderByKey().equalTo(receiver);

        Log.d("TAGee", "sendNotifiaction: " + query);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(FirebaseAuth.getInstance().getUid(), R.mipmap.ic_launcher_round, username, type,
                            receiver, ID);

                    Log.d("TAGdata", "onDataChange: " + data.toString());
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
