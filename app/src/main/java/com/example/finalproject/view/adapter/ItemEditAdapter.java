package com.example.finalproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.MainItemEditBinding;
import com.example.finalproject.models.Item;
import com.example.finalproject.view.activity.DetailActivity;
import com.example.finalproject.view.activity.menu.Fragment_Search;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemEditAdapter extends RecyclerView.Adapter<ItemEditAdapter.ViewHolder> {

    Context context;
    List<Item> itemList;
    private OnItemClickListener listener;
    List<Item> itemListOld;

    public ItemEditAdapter(Context context, List<Item> itemList,OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
        this.itemListOld = itemList;

    }
    public void removeItem(int position) {
        Item item = itemList.get(position);
        String Id = item.getItemId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Item");
        ref.child(Id).removeValue();
    }


    public interface OnItemClickListener{

        void onItemEditClicked(Item item);

        void onItemDeleteClicked(Item item);

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        listener = onItemClickListener;
    }


    @NonNull
    @Override
    public ItemEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainItemEditBinding itemEditBinding = MainItemEditBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(listener, itemEditBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemEditAdapter.ViewHolder holder, int position) {

        Item item = itemList.get(position);
        holder.bind(item);
    }



    private void onClickGoToDetail(Item item) {

            Intent intent = new Intent(context, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object item",item);
            intent.putExtras(bundle);
            context.startActivity(intent);
    }







    @Override
    public int getItemCount() {
        if(itemList!=null){
        return itemList.size();}
        return 0;
    }
    public void searchDataList(ArrayList<Item>searchList){
        itemList=searchList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final OnItemClickListener onItemClickListener;
        private final MainItemEditBinding itemEditBinding;

        public ViewHolder(OnItemClickListener onItemClickListener,MainItemEditBinding itemEditBinding) {
            super(itemEditBinding.getRoot());
            this.onItemClickListener = onItemClickListener;
            this.itemEditBinding = itemEditBinding;
        }
            public void bind(Item item) {

                itemEditBinding.tvNameItem.setText(item.getItemName());
                itemEditBinding.tvAddressItem.setText(item.getItemAddress());
                itemEditBinding.tvPriceItem.setText(item.getItemPrice());
                itemEditBinding.layoutItemEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onClickGoToDetail(item);
                    }

                });

                itemEditBinding.imgBEditItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemEditClicked(item);
                    }
                });

                itemEditBinding.imgBDeleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemDeleteClicked(item);
                    }
                });


                String imageUri=null;
                imageUri=item.getImage();
                Picasso.get().load(imageUri).into(itemEditBinding.itemImg);



            }


    }


}

