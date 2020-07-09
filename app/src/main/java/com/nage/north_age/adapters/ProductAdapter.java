package com.nage.north_age.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.nage.north_age.R;

import java.util.ArrayList;
import java.util.List;

import me.gilo.woodroid.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<Product> products;
    private OnProductListener onProductListener;

    public ProductAdapter(OnProductListener onProductListener) {
        products = new ArrayList<>();
        this.onProductListener = onProductListener;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        return new ViewHolder(view, onProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product item = products.get(position);
        holder.tvProductName.setText(item.getName());
        if (item.getImages() != null)
            Glide.with(holder.itemView)
                    .load(item.getImages().get(0).getSrc())
                    .fitCenter()
                    .placeholder(R.drawable.ic_outline_terrain_24)
                    .error(R.drawable.ic_outline_broken_image_24)
                    .into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView ivProduct;
        final TextView tvProductName;
        final CardView cvProduct;
        OnProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            cvProduct = itemView.findViewById(R.id.cvProduct);
            cvProduct.setOnClickListener(this);
            this.onProductListener = onProductListener;
        }

        @Override
        public void onClick(View v) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductListener {
        void onProductClick(int position);
    }
}
