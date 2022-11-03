package com.example.finalproject.view.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.R;

import com.example.finalproject.models.Item;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    EditText edtN,edtAd,edtP,edtA;
    Button btnAddItem;
    private static final int Gallery_code=1;
    Uri imageUrI=null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add);

        btn = findViewById(R.id.imgButton);
        edtN = findViewById(R.id.tiet_name_item_input);
        edtAd = findViewById(R.id.tiet_address_item_input);
        edtA = findViewById(R.id.tiet_area_item_input);
        edtP = findViewById(R.id.tiet_price_item_input);
        btnAddItem = findViewById(R.id.btnInsert);
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference().child("Item");
        mStorage=FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_code );
            }
        });

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
                String itemName=edtN.getText().toString().trim();
                String itemAddress=edtAd.getText().toString().trim();
                String itemArea=edtA.getText().toString().trim();
                String itemPrice=edtP.getText().toString().trim();

                if(!(itemName.isEmpty()&&itemArea.isEmpty()&&itemAddress.isEmpty()&& itemPrice.isEmpty()&& imageUrI==null)){
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
                                    newPost.child("Name").setValue(itemName);
                                    newPost.child("location").setValue(itemAddress);
                                    newPost.child("Price").setValue(itemPrice);
                                    newPost.child("Area").setValue(itemArea);
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


