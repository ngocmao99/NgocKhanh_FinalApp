package com.example.finalproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.models.Item;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Item item = (Item) bundle.get("object item");


        TextView tvNameItem = findViewById(R.id.name_detail);
        tvNameItem.setText(item.getItemName());
        ImageView view = findViewById(R.id.img_detail);
        String imageUri=null;
        imageUri=item.getImage();
        Picasso.get().load(imageUri).into(view);
        TextView tvAreaItem = findViewById(R.id.tv_area_item);
        tvAreaItem.setText(item.getItemArea());
        TextView tvAddressItem = findViewById(R.id.address_detail);
        tvAddressItem.setText(item.getItemAddress());
        TextView tvPriceItem = findViewById(R.id.tv_rentprice_item);
        tvPriceItem.setText(item.getItemPrice());
        TextView tvDepositItem = findViewById(R.id.tv_deposite_item);
        tvDepositItem.setText(item.getItemDeposit());
        TextView tvMaintain = findViewById(R.id.tv_maintain_item);
        tvMaintain.setText(item.getItemMaintainence());
        TextView tvType = findViewById(R.id.tv_ptype_item);
        tvType.setText(item.getItemType());
        TextView tvbed = findViewById(R.id.tv_bedroom);
        tvbed.setText(item.getItemBedrooms());
        TextView tvbath = findViewById(R.id.tv_bathroom);
        tvbath.setText(item.getItemBathrooms());
        TextView tvkitchen = findViewById(R.id.tv_kitchen_item);
        tvkitchen.setText(item.getItemKitchen());
        TextView tvliv = findViewById(R.id.tv_livingroom_item);
        tvliv.setText(item.getItemLivingroom());

        




    }
}