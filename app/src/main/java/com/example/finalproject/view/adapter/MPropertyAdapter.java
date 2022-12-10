package com.example.finalproject.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ProppertyCardViewBinding;
import com.example.finalproject.models.Property;

import java.util.ArrayList;
import java.util.List;

public class MPropertyAdapter extends RecyclerView.Adapter<MPropertyAdapter.ViewHolder> {
    private final Context mContext;
    List<Property> propertyList;
    private OnItemClickListener mListener;

    public MPropertyAdapter(Context mContext, List<Property> propertyList, OnItemClickListener mListener) {
        this.mContext = mContext;
        this.propertyList = propertyList;
        this.mListener = mListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<Property> searchList){
        propertyList = searchList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MPropertyAdapter.OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MPropertyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProppertyCardViewBinding propertyBinding = ProppertyCardViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MPropertyAdapter.ViewHolder(mListener,propertyBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull MPropertyAdapter.ViewHolder holder, int position) {
        Property item = propertyList.get(position);
        holder.bind(item);

    }
    public interface OnItemClickListener {

        void onClickGoToDetailVerticalProperty(Property property);

    }

    @Override
    public int getItemCount() {
        if (propertyList != null) {
            return propertyList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MPropertyAdapter.OnItemClickListener onItemClickListener;
        private final ProppertyCardViewBinding propertyBinding;

        public ViewHolder(MPropertyAdapter.OnItemClickListener onItemClickListener, ProppertyCardViewBinding propertyBinding) {
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
            propertyBinding.propertyItem.setOnClickListener(v -> mListener.onClickGoToDetailVerticalProperty(property));
        }
    }
}

