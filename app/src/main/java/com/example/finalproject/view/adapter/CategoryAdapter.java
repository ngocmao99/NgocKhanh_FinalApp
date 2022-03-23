package com.example.finalproject.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.ItemCategoryBinding;
import com.example.finalproject.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mListCategories;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Category> mList){
        this.mListCategories = mList;
        notifyDataSetChanged();
    }

    //using viewBinding in recyclerview.adapter
    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding itemCategoryBinding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new CategoryViewHolder(itemCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategories.get(position);
        if (category ==null){
            return;
        }
        holder.categoryImg.setImageResource(category.getCategoryImage());
        holder.categoryName.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        if (mListCategories!=null){
            return mListCategories.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        private final ImageView categoryImg;
        private final TextView categoryName;

        public CategoryViewHolder(@NonNull ItemCategoryBinding itemView) {
            super(itemView.getRoot());
            categoryImg = itemView.categoryImage;
            categoryName = itemView.txtCategoryName;

        }
    }
}


