package com.example.finalproject.view.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    private TextView view;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public TextView getView(){
        return view;
    }
}
