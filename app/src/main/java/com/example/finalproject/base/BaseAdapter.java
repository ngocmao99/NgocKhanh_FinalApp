package com.example.finalproject.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mData = new ArrayList<>();
    public ClickListener<T> mListener;

    public BaseAdapter(ClickListener<T> mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<T> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public void appendData(List<T> newData) {
        int prevSize = mData.size();
        mData.addAll(newData);
        notifyItemRangeInserted(prevSize, newData.size());
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            return null;
        } else {
            return mData.get(position);
        }
    }

    public interface ClickListener<T> {
        void onItemClick(View view, T item, int position);
    }
}
