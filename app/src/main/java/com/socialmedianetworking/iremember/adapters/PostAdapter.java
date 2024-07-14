package com.socialmedianetworking.iremember.adapters;

import static com.socialmedianetworking.iremember.util.Tools.createTextBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.model.Commentmodel;
import com.socialmedianetworking.iremember.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

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

        // Set up RecyclerView for commentmodels
        List<Commentmodel> commentmodels = post.getComments();
        int commentCount = commentmodels != null ? commentmodels.size() : 0;
        if (commentmodels != null) {
            CommentAdapter commentAdapter = new CommentAdapter(commentmodels, context, holder.showAllComments);
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

        holder.text_comment_count.setText(commentCount+" Comments");
        if(post.getContent_text().equals("")){
            holder.text_post_content.setText("Say something about this photo");
        }else{
        holder.text_post_content.setText(post.getContent_text());
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView content;
        TextView createdAt, showMore, text_comment_count,text_post_content;
        RecyclerView commentsRecyclerView;
        boolean showAllComments = false;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.image_post);
            createdAt = itemView.findViewById(R.id.text_timestamp);
            commentsRecyclerView = itemView.findViewById(R.id.comments_recycler_view);
            showMore = itemView.findViewById(R.id.showmore);
            text_comment_count = itemView.findViewById(R.id.text_comment_count);
            text_post_content = itemView.findViewById(R.id.text_post_content);
        }
    }
}
