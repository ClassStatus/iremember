package com.socialmedianetworking.iremember.model;


import java.util.List;

public class Post {
    private String postId;
    private String groupId;
    private String adminId;
    private String content;
    private String createdAt;
    private String content_text;
    private int isImage;
    private List<Commentmodel> comments;

    public Post(String postId, String groupId, String adminId, String content,String content_text,int isImage, String createdAt, List<Commentmodel> comments) {
        this.postId = postId;
        this.groupId = groupId;
        this.adminId = adminId;
        this.content = content;
        this.content_text = content_text;
        this.isImage = isImage;
        this.createdAt = createdAt;
        this.comments = comments;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public int getIsImage() {
        return isImage;
    }

    public void setIsImage(int isImage) {
        this.isImage = isImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Commentmodel> getComments() {
        return comments;
    }

    public void setComments(List<Commentmodel> comments) {
        this.comments = comments;
    }
}