package com.socialmedianetworking.iremember.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialmedianetworking.iremember.model.Commentmodel;
import com.socialmedianetworking.iremember.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Commentmodel> commentmodelList;
    private Context context;
    private boolean showAllComments;

    public CommentAdapter(List<Commentmodel> commentmodelList, Context context, boolean showAllComments) {
        this.commentmodelList = commentmodelList;
        this.context = context;
        this.showAllComments = showAllComments;
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

        holder.image_play_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_play_play.setVisibility(View.GONE);
                holder.image_play_pause.setVisibility(View.VISIBLE);
            }
        });
        holder.image_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.image_play_play.setVisibility(View.VISIBLE);
                holder.image_play_pause.setVisibility(View.GONE);
            }
        });

        holder.text_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Additional code to handle audio file if needed
    }

    @Override
    public int getItemCount() {
        if (showAllComments) {
            return commentmodelList.size();
        } else {
            return Math.min(commentmodelList.size(), 2);
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView commentCreatedAt,text_reply;
        ImageView image_play_play,image_play_pause;
        // Add additional views if needed

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text_username_reply);
            commentCreatedAt = itemView.findViewById(R.id.text_timestamp_reply);
            image_play_pause = itemView.findViewById(R.id.image_play_pause);
            image_play_play = itemView.findViewById(R.id.image_play_play);
            text_reply = itemView.findViewById(R.id.text_reply);
        }
    }
}