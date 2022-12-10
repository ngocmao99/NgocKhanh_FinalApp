package com.example.finalproject.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.MainItemBinding;
import com.example.finalproject.models.Property;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context mContext;
    List<Property> propertyList;
    private ItemAdapter.OnItemClickListener mListener;

    public ItemAdapter(Context mContext, List<Property> propertyList, ItemAdapter.OnItemClickListener mListener) {
        this.mContext = mContext;
        this.propertyList = propertyList;
        this.mListener = mListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void searchDataList(ArrayList<Property> searchList){
        propertyList = searchList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemAdapter.OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainItemBinding propertyBinding = MainItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ItemAdapter.ViewHolder(mListener,propertyBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Property item = propertyList.get(position);
        holder.bind(item);

    }
    public interface OnItemClickListener {

        void onClickGoToDetailProperty(Property property);

    }

    @Override
    public int getItemCount() {
        if (propertyList != null) {
            return propertyList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdapter.OnItemClickListener onItemClickListener;
        private final MainItemBinding propertyBinding;

        public ViewHolder(ItemAdapter.OnItemClickListener onItemClickListener, MainItemBinding propertyBinding) {
            super(propertyBinding.getRoot());
            this.onItemClickListener = onItemClickListener;
            this.propertyBinding = propertyBinding;
        }


        @SuppressLint("SetTextI18n")
        public void bind(Property property) {
            propertyBinding.tvNameItem.setText(property.getPropertyName());
            propertyBinding.tvAddressItem.setText(property.getHouseNumber() +
                    ", " + property.getProvince());
            Glide.with(mContext).load(property.getPropertyImage())
                    .centerCrop()
                    .into(propertyBinding.itemImg);
            String price = String.valueOf(property.getPrice());
            propertyBinding.tvPriceItem.setText("$ " + price);
            propertyBinding.layoutItem.setOnClickListener(v -> mListener.onClickGoToDetailProperty(property));
        }
    }
}
