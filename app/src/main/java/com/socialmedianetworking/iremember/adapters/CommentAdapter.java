package com.socialmedianetworking.iremember.adapters;

import static com.socialmedianetworking.iremember.util.Constant.UPLOAD_URL;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.socialmedianetworking.iremember.model.Commentmodel;
import com.socialmedianetworking.iremember.R;

import java.io.IOException;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Commentmodel> commentmodelList;
    private Context context;
    private boolean showAllComments;
    private MediaPlayer mediaPlayer;
    private int currentlyPlayingPosition = -1;
    private RecyclerView recyclerView;

    public CommentAdapter(List<Commentmodel> commentmodelList, Context context, boolean showAllComments, RecyclerView recyclerView) {
        this.commentmodelList = commentmodelList;
        this.context = context;
        this.showAllComments = showAllComments;
        this.recyclerView = recyclerView;
        this.mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Commentmodel commentmodel = commentmodelList.get(position);
        holder.username.setText(commentmodel.getUsername());
        holder.commentCreatedAt.setText(commentmodel.getCommentCreatedAt());
        Glide.with(context)
                .load(commentmodel.getPimage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.logo_small_round)
                .into(holder.image_profile_reply);

        // Handle the play and pause button visibility
        if (position == currentlyPlayingPosition) {
            holder.image_play_play.setVisibility(View.GONE);
            holder.image_play_pause.setVisibility(View.VISIBLE);
        } else {
            holder.image_play_play.setVisibility(View.VISIBLE);
            holder.image_play_pause.setVisibility(View.GONE);
        }

        // Handle play button click
        holder.image_play_play.setOnClickListener(v -> playAudio(commentmodel.getAudioFilePath(), holder, position));

        // Handle pause button click
        holder.image_play_pause.setOnClickListener(v -> stopAudio(holder, position));

        holder.text_reply.setOnClickListener(v -> {
            // Handle text reply click
        });
    }

    private void playAudio(String audioUrl, CommentViewHolder holder, int position) {
        // Stop any currently playing audio
        if (currentlyPlayingPosition != -1 && currentlyPlayingPosition != position) {
            CommentViewHolder currentHolder = (CommentViewHolder) recyclerView.findViewHolderForAdapterPosition(currentlyPlayingPosition);
            if (currentHolder != null) {
                stopAudio(currentHolder, currentlyPlayingPosition);
            }
        }

        Log.d("CommentAdapter", "Audio URL: " + audioUrl);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                currentlyPlayingPosition = position;
                notifyDataSetChanged(); // Notify all items to update their UI
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                String errorMsg = "Error playing audio. What: " + what + ", Extra: " + extra;
                Log.e("CommentAdapter", errorMsg);

                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_IO:
                        Log.e("CommentAdapter", "IO Error: Check file path and permissions.");
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        Log.e("CommentAdapter", "Malformed file: Ensure the file format is supported.");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        Log.e("CommentAdapter", "Unsupported format: Verify file format compatibility.");
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        Log.e("CommentAdapter", "Timeout: Check network connectivity if using a URL.");
                        break;
                    default:
                        Log.e("CommentAdapter", "Unknown error: Check file path, format, and permissions.");
                        break;
                }

                return true;
            });

        } catch (IOException e) {
            Log.e("CommentAdapter", "Error preparing media player", e);
        }
    }

    private void stopAudio(CommentViewHolder holder, int position) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        currentlyPlayingPosition = -1;

        // Update UI for the stopped state
        if (holder != null) {
            holder.image_play_play.setVisibility(View.VISIBLE);
            holder.image_play_pause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (showAllComments) {
            return commentmodelList.size();
        } else {
            return Math.min(commentmodelList.size(), 2);
        }
    }

    @Override
    public void onViewRecycled(@NonNull CommentViewHolder holder) {
        super.onViewRecycled(holder);
        // Ensure the MediaPlayer stops and releases resources when the view is recycled
        if (currentlyPlayingPosition == holder.getAdapterPosition()) {
            stopAudio(holder, holder.getAdapterPosition());
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView commentCreatedAt, text_reply;
        ImageView image_play_play, image_play_pause, image_profile_reply;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text_username_reply);
            commentCreatedAt = itemView.findViewById(R.id.text_timestamp_reply);
            image_play_pause = itemView.findViewById(R.id.image_play_pause);
            image_play_play = itemView.findViewById(R.id.image_play_play);
            text_reply = itemView.findViewById(R.id.text_reply);
            image_profile_reply = itemView.findViewById(R.id.image_profile_reply);
        }
    }
}