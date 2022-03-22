package com.example.finalproject.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Category;
import com.example.finalproject.models.Category2;

import java.util.List;

public class Category2Adapter extends RecyclerView.Adapter<Category2Adapter.Category2ViewHolder> {

    private List<Category2> mListCategory2;
    public void setData(List<Category2>list){
        this.mListCategory2=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Category2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category2,parent,false);
        return new Category2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category2ViewHolder holder, int position) {
    Category2 category2=mListCategory2.get(position);
    if(category2==null){
        return;
    }
    holder.imgCategory.setImageResource(category2.getResourceId());
    holder.tvTitle.setText(category2.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mListCategory2!=null){
            return mListCategory2.size();
        }
        return 0;
    }

    public class Category2ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgCategory;
        private TextView tvTitle;
        public Category2ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCategory = itemView.findViewById(R.id.img_category2);
            tvTitle = itemView.findViewById(R.id.tv_title2);
        }
    }
}
