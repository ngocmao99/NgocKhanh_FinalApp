package com.example.finalproject.view.activity;

import static android.media.MediaFormat.KEY_PROFILE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityEditItemBinding;
import com.example.finalproject.databinding.FragmentPropertyBinding;
import com.example.finalproject.models.Item;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.activity.menu.Fragment_Property;
import com.example.finalproject.view.adapter.ItemAdapter;
import com.example.finalproject.view.adapter.ItemEditAdapter;
import com.example.finalproject.view.adapter.PropertyAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyActivity extends AppCompatActivity implements PropertyAdapter.OnItemClickListener {

    private FragmentPropertyBinding binding;
    PropertyAdapter propertyAdapter;
    List<Property> properties;
    RecyclerView recyclerView1, recyclerView;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    Button add;
    private String currentImage;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = FragmentPropertyBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Item");
        mStorage = FirebaseStorage.getInstance();
        binding.rcvListItem1.setHasFixedSize(true);
        binding.rcvListItem1.setLayoutManager(new GridLayoutManager(PropertyActivity.this, 2));
        properties = new ArrayList<Property>();
        propertyAdapter = new PropertyAdapter( PropertyActivity.this, properties, this);
        binding.rcvListItem1.setAdapter(propertyAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Property property = snapshot.getValue(Property.class);
                if (property!= null){
                    properties.add(property);
                    propertyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Property property = snapshot.getValue(Property.class);
                if(  properties==null|| properties.isEmpty()){
                    return;
                }
                for (int i=0;i<properties.size();i++){
                    if (property ==null ||property.getPropertyId() == properties.get(i).getPropertyId()){
                        properties.set(i,property);
                    }
                }
                propertyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //button
        binding.imgBCreateNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyActivity.this, AddActivity.class);
                startActivity(intent);


            }
        });


    }

    public void onItemEditClicked(Item item) {
        openDialogUpdateItem(item);
    }

    public void onItemDeleteClicked(Item item) {
        String itemId = item.getItemId();
        removeItem(itemId);
    }

    private void removeItem(String itemId) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Item");

        ref.orderByChild("itemId").equalTo(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.getRef().removeValue();
                    }
                    Toast.makeText(PropertyActivity.this, "Successful removed.",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PropertyActivity.this, Fragment_Property.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





    private void openDialogUpdateItem(Item item){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_edit_item);
        Window window =dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        RadioGroup FurnshingUpdate = dialog.findViewById(R.id.rgFurnishing);
        EditText edtUpdateName = dialog.findViewById(R.id.tiet_name_item_update);
        ImageView edtUpdateImg = dialog.findViewById(R.id.imgButton_update);
        EditText edtUpdateAdd = dialog.findViewById(R.id.tiet_address_item_update);
        EditText edtUpdatePrice = dialog.findViewById(R.id.tiet_price_item_update);
        EditText edtUpdateArea = dialog.findViewById(R.id.tiet_area_item_update);
        EditText edtUpdateDeposit = dialog.findViewById(R.id.tiet_deposit_item_update);
        EditText edtUpdateMaintain = dialog.findViewById(R.id.tiet_maintain_item_update);
        EditText edtUpdateDes = dialog.findViewById(R.id.tiet_des_item_update);
        EditText edtUpdateBath = dialog.findViewById(R.id.tiet_Bathrooms_item_update);
        EditText edtUpdateBed = dialog.findViewById(R.id.tiet_Bedrooms_item_update);
        EditText edtUpdateKitchen = dialog.findViewById(R.id.tiet_Kitchen_item_update);
        EditText edtUpdateLiv = dialog.findViewById(R.id.tiet_Living_room_item_update);
        EditText edtUpdateGara = dialog.findViewById(R.id.tiet_Garage_item_update);
        EditText edtUpdateBalcony = dialog.findViewById(R.id.tiet_Balcony_item_update);
        EditText edtUpdateType = dialog.findViewById(R.id.actv_property_type);

        Button btnCancel = dialog.findViewById(R.id.btnUpdateCancel);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        edtUpdateType.setText(item.getItemType());
        edtUpdateName.setText(item.getItemName());
        edtUpdatePrice.setText(item.getItemPrice());
        edtUpdateArea.setText(item.getItemArea());
        edtUpdateBalcony.setText(item.getItemBalcony());
        edtUpdateKitchen.setText(item.getItemKitchen());
        edtUpdateBed.setText(item.getItemBedrooms());
        edtUpdateBath.setText(item.getItemBathrooms());
        edtUpdateDes.setText(item.getItemDeposit());
        edtUpdateMaintain.setText(item.getItemMaintainence());
        edtUpdateGara.setText(item.getItemGarage());
        Picasso.get().load(item.getImage()).fit().centerCrop()
                .into(edtUpdateImg);



        currentImage = item.getImage();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Item");
                String newName = edtUpdateName.getText().toString().trim();
                item.setItemName(newName);
                myRef.child(String.valueOf(item.getItemId())).updateChildren(item.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(PropertyActivity.this,"Update data success",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();


    }


    @Override
    public void onClickGoToDetailProperty(Property property) {


        Intent intent = new Intent(PropertyActivity.this, PropertyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item property",property);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}





