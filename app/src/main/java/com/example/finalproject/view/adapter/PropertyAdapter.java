package com.example.finalproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.PropertyItemCardBinding;
import com.example.finalproject.models.Item;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.activity.DetailActivity;
import com.example.finalproject.view.activity.PropertyActivity;
import com.example.finalproject.view.activity.PropertyDetailActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder>{

    private Context mContext;
    List<Property> properties;
    private OnItemClickListener listener;

    public PropertyAdapter(Context mContext, List<Property> properties, OnItemClickListener listener) {
        this.mContext = mContext;
        this.properties = properties;
        this.listener = listener;
    }

    public void setOnItemClickListener(PropertyAdapter.OnItemClickListener onItemClickListener){
        listener = onItemClickListener;
    }

    @NonNull
    @Override
    public PropertyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PropertyItemCardBinding propertyBinding = PropertyItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PropertyAdapter.ViewHolder(listener, propertyBinding);

    }


    @Override
    public void onBindViewHolder(@NonNull PropertyAdapter.ViewHolder holder, int position) {
        Property item = properties.get(position);
        holder.bind(item);

    }
    public interface OnItemClickListener{

        void onClickGoToDetailProperty(Property property);



    }

    @Override
    public int getItemCount() {
        if(properties!=null){
            return properties.size();}
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final PropertyAdapter.OnItemClickListener onItemClickListener;
        private final PropertyItemCardBinding propertyBinding;

        public ViewHolder(PropertyAdapter.OnItemClickListener onItemClickListener, PropertyItemCardBinding propertyBinding) {
            super(propertyBinding.getRoot());
            this.onItemClickListener = onItemClickListener;
            this.propertyBinding = propertyBinding;
        }
            public void bind(Property property) {

                propertyBinding.tvNameProperty.setText(property.getPropertyName());
                propertyBinding.tvAddressProperty.setText(property.getPropertyLocation());
                propertyBinding.propertyItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        listener.onClickGoToDetailProperty(property);
                    }

                });




                String imageUri=null;
                imageUri=property.getPropertyImage();
                Picasso.get().load(imageUri).into(propertyBinding.propertyImage);



            }


    }



}
