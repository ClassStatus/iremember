package com.socialmedianetworking.iremember.adapters;

import static com.socialmedianetworking.iremember.util.Tools.createTextBitmap;
import static com.socialmedianetworking.iremember.util.Tools.generateFileName;
import static com.socialmedianetworking.iremember.util.Tools.uploadFile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.socialmedianetworking.iremember.MainActivity;
import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.activity.ProfileActivity;
import com.socialmedianetworking.iremember.model.Commentmodel;
import com.socialmedianetworking.iremember.model.Post;
import com.socialmedianetworking.iremember.util.UserSession;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String fileName;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private UserSession session;
    private HashMap<String, String> user;
    String user_id,name,email,mobile,image;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        session = new UserSession(context);
        session.isLoggedIn();
        user = session.getUserDetails();
        user_id = user.get(UserSession.KEY_USER_ID);
        name = user.get(UserSession.KEY_FULL_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_PHONE_NUMBER);
        image = user.get(UserSession.KEY_P_IMAGE);

        if (post.getIsImage() == 1) {
            Glide.with(context)
                    .load(post.getContent())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.content);

        } else {
            String text = post.getContent(); // Replace with your actual text
            Bitmap bitmap = createTextBitmap(text, context, 300);
            Glide.with(context)
                    .load(bitmap)
                    .into(holder.content);
        }

        //String relativeTime = Util.getRelativeTime(post.getCreatedAt());
        holder.createdAt.setText(post.getCreatedAt());
        holder.post_text_username.setText(post.getPost_uploaded_fullname());
        Glide.with(context)
                .load(post.getPost_uploader_profile_image())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.logo_small_round)
                .into(holder.post_image_profile);


        // Set up RecyclerView for commentmodels
        List<Commentmodel> commentmodels = post.getComments();
        int commentCount = commentmodels != null ? commentmodels.size() : 0;
        if (commentmodels != null) {
            CommentAdapter commentAdapter = new CommentAdapter(commentmodels, context, holder.showAllComments,holder.commentsRecyclerView);
            holder.commentsRecyclerView.setAdapter(commentAdapter);
        }

        holder.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Handle the "Show more" functionality
        holder.showMore.setOnClickListener(v -> {
            holder.showAllComments = !holder.showAllComments;
            notifyItemChanged(position);
        });
        if (holder.showAllComments) {
            holder.showMore.setText("Show less");
        } else {
            holder.showMore.setText("Show more");
        }

        if(commentCount > 2){
            holder.showMore.setVisibility(View.VISIBLE);
        }else{
            holder.showMore.setVisibility(View.GONE);
        }

        holder.text_comment_count.setText(commentCount+" Comments");
        if(post.getContent_text().equals("")){
            holder.text_post_content.setText("Say something about this photo");
        }else{
        holder.text_post_content.setText(post.getContent_text());
        }

        holder.login_user_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.recording_section.setVisibility(View.VISIBLE);
            }
        });

        holder.recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording(holder);
            } else {
                startRecording(holder, position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> deleteRecording(holder));
        Glide.with(context)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.logo_small_round)
                .into(holder.login_user_icon);

        //remove when
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProfileActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        holder.playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopPlaying(holder);
            } else {
                startPlaying(holder);
            }
        });
        holder.stopPlayingButton.setOnClickListener(v -> stopPlaying(holder));
        holder.btnUploadRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(fileName);
                //Log.d("file", String.valueOf(file));
                if (file.exists()) {
                    //uploadAudioFileNew(context, file, post.getPostId(), user_id);
                    uploadFile(context,fileName, post.getPostId(), user_id);
                    deleteRecording(holder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView content,post_image_profile,login_user_icon;
        TextView createdAt, showMore, text_comment_count,text_post_content,post_text_username;
        View login_user_comment;
        LinearLayout recording_section;
        RecyclerView commentsRecyclerView;
        boolean showAllComments = false;
        Button recordButton,btnUploadRecording, playButton, stopPlayingButton;
        ImageView deleteButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.image_post);
            createdAt = itemView.findViewById(R.id.text_timestamp);
            commentsRecyclerView = itemView.findViewById(R.id.comments_recycler_view);
            showMore = itemView.findViewById(R.id.showmore);
            text_comment_count = itemView.findViewById(R.id.text_comment_count);
            text_post_content = itemView.findViewById(R.id.text_post_content);
            login_user_comment = itemView.findViewById(R.id.login_user_comment);
            recording_section = itemView.findViewById(R.id.recording_section);
            recordButton = itemView.findViewById(R.id.btnStartRecording);
            deleteButton = itemView.findViewById(R.id.deleteRecordingIcon);
            btnUploadRecording = itemView.findViewById(R.id.btnUploadRecording);
            playButton = itemView.findViewById(R.id.btnPlayRecording);
            stopPlayingButton = itemView.findViewById(R.id.btnStopPlayingRecording);

            post_image_profile = itemView.findViewById(R.id.post_image_profile);
            post_text_username = itemView.findViewById(R.id.post_text_username);
            login_user_icon = itemView.findViewById(R.id.login_user_icon);
        }
    }



    private void startRecording(PostViewHolder holder, int position) {
        Log.d("QQQ","BBB");
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("QQQ","CCC");
//            ActivityCompat.requestPermissions((MainActivity) context, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
//        } else {
//            Log.d("QQQ","AAA");
            holder.deleteButton.setVisibility(View.VISIBLE);
            fileName = context.getExternalCacheDir().getAbsolutePath() + "/audiorecord_" + position+"_"+postList.get(position).getPostId() +"_"+user_id +"_"+generateFileName() +".3gp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(fileName);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
                holder.recordButton.setText("Stop");
                holder.btnUploadRecording.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }

    private void stopRecording(PostViewHolder holder) {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
        holder.recordButton.setText("Record");
        holder.playButton.setVisibility(View.VISIBLE);
    }

    private void deleteRecording(PostViewHolder holder) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            Toast.makeText(context, "Recording deleted", Toast.LENGTH_SHORT).show();
        }
        holder.deleteButton.setVisibility(View.GONE);
        holder.playButton.setVisibility(View.GONE);
        holder.stopPlayingButton.setVisibility(View.GONE);
        holder.recordButton.setText("Start Recording");
        holder.btnUploadRecording.setVisibility(View.GONE);
    }
    private void startPlaying(PostViewHolder holder) {
        Log.d("ZZZZZ",fileName);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            holder.playButton.setText("Pause");
            holder.stopPlayingButton.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(mp -> stopPlaying(holder));
    }

    private void stopPlaying(PostViewHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            holder.playButton.setText("Play");
            holder.stopPlayingButton.setVisibility(View.GONE);
        }
    }



}
