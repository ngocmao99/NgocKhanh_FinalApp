package com.example.finalproject.view.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Item;
import com.example.finalproject.my_interface.IClickItemListener;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    List<Item> itemList;
    private IClickItemListener iClickItemListener;

    public ItemAdapter(Context context, List<Item> itemList,IClickItemListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.iClickItemListener = listener;
    }

    public ItemAdapter(List<Item> itemList, IClickItemListener iClickItemListener) {
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {

        Item item= itemList.get(position);
        holder.tvF.setText(item.getItemName());
        holder.tvL.setText(item.getLocation());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemListener.onClickItem(item);
            }
        });
        String imageUri=null;
        imageUri=item.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        if(itemList!=null){
        return itemList.size();}
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layoutItem;
        ImageView imageView;
        TextView tvF,tvL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem=itemView.findViewById(R.id.layout_item);
            imageView=itemView.findViewById(R.id.itemImg);
            tvF=itemView.findViewById(R.id.itemN);
            tvL=itemView.findViewById(R.id.itemLocation);

        }
    }

}
