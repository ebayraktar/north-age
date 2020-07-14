package com.nage.north_age.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nage.north_age.R;

import java.util.ArrayList;
import java.util.List;

import me.gilo.woodroid.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {
    List<Category> categories;
    List<Category> originalCategories;
    private OnCategoryListener onCategoryListener;

    public CategoryAdapter(OnCategoryListener onCategoryListener) {
        categories = new ArrayList<>();
        originalCategories = new ArrayList<>(categories);
        this.onCategoryListener = onCategoryListener;

    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        originalCategories = new ArrayList<>(categories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view, onCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category item = categories.get(position);
        holder.tvCategoryName.setText(item.getName());
        if (item.getImage() != null)
            Glide.with(holder.itemView)
                    .load(item.getImage().getSrc())
                    .fitCenter()
                    .placeholder(R.drawable.ic_outline_terrain_24)
                    .error(R.drawable.ic_outline_broken_image_24)
                    .into(holder.ivCategory);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public Filter getFilter() {
        return categoryFilter;
    }
    private Filter categoryFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Category> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(originalCategories);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Category item: originalCategories){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            categories.clear();
            categories.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView ivCategory;
        final TextView tvCategoryName;
        final CardView cvCategory;
        OnCategoryListener onCategoryListener;

        public ViewHolder(@NonNull View itemView, OnCategoryListener onCategoryListener) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            cvCategory = itemView.findViewById(R.id.cvCategory);
            cvCategory.setOnClickListener(this);
            this.onCategoryListener = onCategoryListener;
        }

        @Override
        public void onClick(View v) {
            onCategoryListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface OnCategoryListener {
        void onCategoryClick(int position);
    }
}
