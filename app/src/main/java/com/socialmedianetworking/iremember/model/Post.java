package com.socialmedianetworking.iremember.model;


import java.util.List;

public class Post {
    private String postId;
    private String groupId;
    private String adminId;
    private String content;
    private String createdAt;
    private String content_text,post_uploader_profile_image,post_uploaded_fullname;
    private int isImage;
    private List<Commentmodel> comments;

    public Post(String postId, String groupId, String adminId, String content,String content_text,int isImage, String createdAt,String post_uploader_profile_image, String post_uploaded_fullname, List<Commentmodel> comments) {
        this.postId = postId;
        this.groupId = groupId;
        this.adminId = adminId;
        this.content = content;
        this.content_text = content_text;
        this.isImage = isImage;
        this.createdAt = createdAt;
        this.post_uploader_profile_image = post_uploader_profile_image;
        this.post_uploaded_fullname = post_uploaded_fullname;
        this.comments = comments;
    }

    public String getPost_uploaded_fullname() {
        return post_uploaded_fullname;
    }

    public void setPost_uploaded_fullname(String post_uploaded_fullname) {
        this.post_uploaded_fullname = post_uploaded_fullname;
    }

    public String getPost_uploader_profile_image() {
        return post_uploader_profile_image;
    }

    public void setPost_uploader_profile_image(String post_uploader_profile_image) {
        this.post_uploader_profile_image = post_uploader_profile_image;
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
