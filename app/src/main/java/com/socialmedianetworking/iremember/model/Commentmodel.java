package com.socialmedianetworking.iremember.model;

public class Commentmodel {

    private String commentId;
    private String userId;


    private String username;
    private String audioFileId;
    private String audioFilePath;
    private String commentCreatedAt;
    private String audioUploadedAt;

    public Commentmodel(String commentId, String userId, String username, String audioFileId, String audioFilePath, String commentCreatedAt, String audioUploadedAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.audioFileId = audioFileId;
        this.audioFilePath = audioFilePath;
        this.commentCreatedAt = commentCreatedAt;
        this.audioUploadedAt = audioUploadedAt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAudioFileId() {
        return audioFileId;
    }

    public void setAudioFileId(String audioFileId) {
        this.audioFileId = audioFileId;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public String getCommentCreatedAt() {
        return commentCreatedAt;
    }

    public void setCommentCreatedAt(String commentCreatedAt) {
        this.commentCreatedAt = commentCreatedAt;
    }

    public String getAudioUploadedAt() {
        return audioUploadedAt;
    }

    public void setAudioUploadedAt(String audioUploadedAt) {
        this.audioUploadedAt = audioUploadedAt;
    }
}
