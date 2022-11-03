package com.example.finalproject.view.adapter;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.finalproject.R;
import com.example.finalproject.models.Item;
import com.example.finalproject.my_interface.IClickItemListener;

import com.example.finalproject.view.activity.DetailActivity;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    Context context;
    List<Item> itemList;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    
    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
        ;
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
        viewBinderHelper.bind(holder.layoutItem,String.valueOf(item.getItemId()));
        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.tvName.setText(item.getItemName());
        holder.tvAddress.setText(item.getItemAddress());
        holder.tvArea.setText(item.getItemArea());
        holder.tvPrice.setText(item.getItemPrice());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickGoToDetail(item);
            }

            private void onClickGoToDetail(Item item) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object item",item);
                intent.putExtras(bundle);
                context.startActivity(intent);
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
        private SwipeRevealLayout layoutItem;
        ImageView imageView;
        private LinearLayout layoutDelete;
        TextView tvName,tvAddress,tvPrice,tvArea;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutDelete=itemView.findViewById(R.id.layout_delete);
            layoutItem=itemView.findViewById(R.id.layout_item);
            imageView=itemView.findViewById(R.id.itemImg);
            tvName=itemView.findViewById(R.id.tv_name_item);
            tvAddress=itemView.findViewById(R.id.tv_address_item);
            tvPrice=itemView.findViewById(R.id.tv_price_item);
            tvArea=itemView.findViewById(R.id.tv_area_item);

        }
    }

}
