package com.example.finalproject.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.ItemPropertyBinding;
import com.example.finalproject.models.Property;

import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {
    private List<Property> mListProperty;

    public void setData(List<Property> mList){
        this.mListProperty = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PropertyAdapter.PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPropertyBinding itemPropertyBinding = ItemPropertyBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new PropertyViewHolder(itemPropertyBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = mListProperty.get(position);
        if (property == null){
            return;
        }
        holder.propertyImage.setImageResource(property.getPropertyImage());
        holder.propertyName.setText(property.getPropertyName());

    }


    @Override
    public int getItemCount() {
        if (mListProperty!=null){
            return mListProperty.size();
        }
        return 0;
    }

    public class PropertyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView propertyImage;
        private final TextView propertyName;

        public PropertyViewHolder(@NonNull ItemPropertyBinding itemView) {
            super(itemView.getRoot());
            propertyImage = itemView.imgProperty;
            propertyName = itemView.propertyName;
        }
    }
}
