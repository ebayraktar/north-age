package com.nage.north_age.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nage.north_age.R;

import java.util.List;

import me.gilo.woodroid.models.ProductReview;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    List<ProductReview> reviews;

    public ReviewAdapter(List<ProductReview> reviews) {
        this.reviews = reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductReview item = reviews.get(position);
        holder.rbRate.setNumStars(item.getRating());
        holder.tvUserName.setText(item.getReviewer());
        holder.tvDate.setText(item.getDate_created().toString());
        holder.tvMessage.setText(item.getReview());
        if (item.getReviewer_avatar_urls() != null)
            Glide.with(holder.itemView)
                    .load(item.getReviewer_avatar_urls().get("96"))
                    .fitCenter()
                    .placeholder(R.drawable.ic_outline_terrain_24)
                    .error(R.drawable.ic_outline_broken_image_24)
                    .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivAvatar;
        final RatingBar rbRate;
        final TextView tvUserName;
        final TextView tvDate;
        final TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            rbRate = itemView.findViewById(R.id.rbRate);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
