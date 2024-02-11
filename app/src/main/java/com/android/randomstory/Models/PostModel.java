package com.android.randomstory.Models;

import java.io.Serializable;

public class PostModel implements Serializable {
    private String postID;
    private String postDescription;
    private String postedBy;
    private long postAt;
    private int postLike;
    private int commentCount;
    private String posterName;
    private String imageUrl;

    public PostModel(String postID, String postDescription, String postedBy, long postAt, int postLike, int commentCount, String posterName, String imageUrl) {
        this.postID = postID;
        this.postDescription = postDescription;
        this.postedBy = postedBy;
        this.postAt = postAt;
        this.postLike = postLike;
        this.commentCount = commentCount;
        this.posterName = posterName;
        this.imageUrl = imageUrl;
    }

    public PostModel() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }


    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getPostAt() {
        return postAt;
    }

    public void setPostAt(long postAt) {
        this.postAt = postAt;
    }


    @Override
    public String toString() {
        return "PostModel{" +
                "postID='" + postID + '\'' +
                ", postDescription='" + postDescription + '\'' +
                ", postedBy='" + postedBy + '\'' +
                ", postAt=" + postAt +
                ", postLike=" + postLike +
                ", commentCount=" + commentCount +
                ", posterName='" + posterName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
