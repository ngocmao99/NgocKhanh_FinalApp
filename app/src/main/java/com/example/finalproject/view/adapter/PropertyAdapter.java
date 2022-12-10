package com.example.finalproject.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.PropertyItemCardBinding;
import com.example.finalproject.models.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder>{

    private final Context mContext;
    List<Property> properties;
    private OnItemClickListener listener;

    public PropertyAdapter(Context mContext, List<Property> properties, OnItemClickListener listener) {
        this.mContext = mContext;
        this.properties = properties;
        this.listener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<Property> searchList){
        properties=searchList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(PropertyAdapter.OnItemClickListener onItemClickListener) {
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
    public interface OnItemClickListener {

        void onClickGoToDetailProperty(Property property);

        void onClickEditProperty(Property property);

        void onClickRemoveProperty(Property property);


    }

    @Override
    public int getItemCount() {
        if (properties != null) {
            return properties.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final PropertyAdapter.OnItemClickListener onItemClickListener;
        private final PropertyItemCardBinding propertyBinding;

        public ViewHolder(PropertyAdapter.OnItemClickListener onItemClickListener, PropertyItemCardBinding propertyBinding) {
            super(propertyBinding.getRoot());
            this.onItemClickListener = onItemClickListener;
            this.propertyBinding = propertyBinding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Property property) {

            propertyBinding.tvNameProperty.setText(property.getPropertyName());
            propertyBinding.tvAddressProperty.setText(property.getHouseNumber() +
                    ", " + property.getProvince());
            Glide.with(mContext).load(property.getPropertyImage())
                    .centerCrop()
                    .into(propertyBinding.propertyImage);
            String price = String.valueOf(property.getPrice());
            propertyBinding.price.setText("$ " + price);
            propertyBinding.propertyItem.setOnClickListener(v -> listener.onClickGoToDetailProperty(property));
            propertyBinding.btnEdit.setOnClickListener(v -> listener.onClickEditProperty(property));
            propertyBinding.btnDelete.setOnClickListener(v -> listener.onClickRemoveProperty(property));
        }
    }


}
