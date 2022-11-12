package com.example.finalproject.view.activity;


import static com.example.finalproject.utils.Constants.DEFAULT_VALUE;
import static com.example.finalproject.utils.Constants.OTHER;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import com.example.finalproject.models.Item;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddActivity  extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageView btn;
    private String userId;
    EditText edtName,edtAddress,edtPrice,edtArea,edtBedrooms,edtBathroom,
            edtDeposit,edtMaintain,
            edtLivingroom,edtBalcony,edtGarage,edtKitchen;
    Button btnAddItem;
    RadioGroup rgFurnishing;
    CheckBox cbGym,cbCarParking,cbFuelStation,cbPark;
    private static final int Gallery_code=1;
    Uri imageUrI=null;
    ProgressDialog progressDialog;
    AutoCompleteTextView propertyType,floor;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add);
        floor =findViewById(R.id.actv_floor);
        propertyType =findViewById(R.id.actv_property_type);
        btn             = findViewById(R.id.imgButton);
        edtName         = findViewById(R.id.tiet_name_item_input);
        edtAddress      = findViewById(R.id.tiet_address_item_input);
        edtArea         = findViewById(R.id.tiet_area_item_input);
        edtPrice        = findViewById(R.id.tiet_price_item_input);
        edtBedrooms     = findViewById(R.id.tiet_Bedrooms_item);
        edtBalcony      = findViewById(R.id.tiet_Balcony_item);
        edtLivingroom   = findViewById(R.id.tiet_Living_room_item);
        edtKitchen      = findViewById(R.id.tiet_Kitchen_item);
        edtGarage       = findViewById(R.id.tiet_Garage_item);
        edtBathroom     = findViewById(R.id.tiet_Bathrooms_item);
        edtMaintain     = findViewById(R.id.tiet_maintain_item_input);
        edtDeposit      = findViewById(R.id.tiet_deposit_item_input);
        cbGym           = findViewById(R.id.checkbox_gym_item);
        cbCarParking    = findViewById(R.id.checkbox_car_parking_item);
        cbFuelStation   = findViewById(R.id.checkbox_fuel_station_item);
        cbPark          = findViewById(R.id.checkbox_park_item);
        rgFurnishing    = findViewById(R.id.rgFurnishing);
        btnAddItem = findViewById(R.id.btnInsert);
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference().child("Item");
        mStorage=FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        rgFurnishing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                if (checkedId == R.id.rb_full_furn_item) {
                    //do work when radioButton1 is active
                    Toast.makeText(AddActivity.this,
                            rb.getText(), Toast.LENGTH_SHORT).show();
                } else  if (checkedId == R.id.rb_semi_furn_item) {
                    //do work when radioButton2 is active
                } else  if (checkedId == R.id.rb_un_furn_item) {
                    //do work when radioButton3 is active
                }

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_code );
            }
        });
        String[] propertyTypes = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(AddActivity.this,R.layout.item_dropdownlist,propertyTypes);
        propertyType.setAdapter(provinceAdapter);
        propertyType.setThreshold(1);
        propertyType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddActivity.this, "Item"+item, Toast.LENGTH_SHORT).show();
            }
        });
        String[] floors = getResources().getStringArray(R.array.floor);
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(AddActivity.this,R.layout.item_dropdownlist,floors);
        floor.setAdapter(floorAdapter);
        floor.setThreshold(1);
        floor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(AddActivity.this, "Item"+item, Toast.LENGTH_SHORT).show();
            }
        });

    }
            //Furn validate


    private String getUserId() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            userId = mUser.getUid();
        }

        return userId;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_code && resultCode==RESULT_OK);
        {
            imageUrI=data.getData();
            btn.setImageURI(imageUrI);
        }
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = mRef.push().getKey();
                String itemName=edtName.getText().toString().trim();
                String itemAddress=edtAddress.getText().toString().trim();
                String itemArea=edtArea.getText().toString().trim();
                String itemPrice=edtPrice.getText().toString().trim();
                String itemType=propertyType.getText().toString().trim();
                String itemFloor= floor.getText().toString().trim();
                String itemBedrooms =edtBedrooms.getText().toString().trim();
                String itemKitchen = edtKitchen.getText().toString().trim();
                String itemBathroom = edtBathroom.getText().toString().trim();
                String itemBalcony = edtBalcony.getText().toString().trim();
                String itemGarage = edtGarage.getText().toString().trim();
                String itemLivingroom = edtLivingroom.getText().toString().trim();
                String itemMaintain = edtMaintain.getText().toString().trim();
                String itemDeposit = edtDeposit.getText().toString().trim();
                String itemGym = cbGym.getText().toString().trim();
                String itemPark = cbPark.getText().toString().trim();
                String itemCarParking = cbCarParking.getText().toString().trim();
                String itemFuelStation = cbFuelStation.getText().toString().trim();
                String itemFurnishing =
                        ((RadioButton)findViewById(rgFurnishing.getCheckedRadioButtonId()))
                                .getText().toString();
                if(!(itemName.isEmpty()&&itemArea.isEmpty()&&itemAddress.isEmpty()&& itemType.isEmpty()&& itemPrice.isEmpty()&& imageUrI==null)){
                    progressDialog.setTitle("uploading....");
                    progressDialog.show();

                    StorageReference filepath=mStorage.getReference().child("imagePost").child(imageUrI.getLastPathSegment());
                    filepath.putFile(imageUrI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrI=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t=task.getResult().toString();
                                    DatabaseReference newPost=mRef.push();
                                    newPost.child("itemName").setValue(itemName);
                                    newPost.child("itemAddress").setValue(itemAddress);
                                    newPost.child("itemPrice").setValue(itemPrice);
                                    newPost.child("itemArea").setValue(itemArea);
                                    newPost.child("itemLivingroom").setValue(itemLivingroom);
                                    newPost.child("itemType").setValue(propertyType);
                                    newPost.child("UserId").setValue(userId);
                                    newPost.child("itemId").setValue(itemId);
                                    newPost.child("itemFloor").setValue(itemFloor);
                                    newPost.child("itemFurnishing").setValue(itemFurnishing);
                                    newPost.child("itemGym").setValue(itemGym);
                                    newPost.child("itemFuelstation").setValue(itemFuelStation);
                                    newPost.child("itemCarparking").setValue(itemCarParking);
                                    newPost.child("itemPark").setValue(itemPark);
                                    newPost.child("itemDeposit").setValue(itemDeposit);
                                    newPost.child("itemMaintainence").setValue(itemMaintain);
                                    newPost.child("itemBedrooms").setValue(itemBedrooms);
                                    newPost.child("itemBathrooms").setValue(itemBathroom);
                                    newPost.child("itemKitchen").setValue(itemKitchen);
                                    newPost.child("itemBalcony").setValue(itemBalcony);
                                    newPost.child("itemGarage").setValue(itemGarage);
                                    newPost.child("image").setValue(task.getResult().toString());
                                    progressDialog.dismiss();
                                    Intent intent= new Intent(AddActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}


