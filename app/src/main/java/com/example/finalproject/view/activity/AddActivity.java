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
    EditText edtfirst,edtlast;
    Button btnadditem;
    private static final int Gallery_code=1;
    Uri imageUrI=null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add);

        btn = findViewById(R.id.imageButton);
        edtfirst = findViewById(R.id.edttext);
        edtlast = findViewById(R.id.edttext2);

        btnadditem = findViewById(R.id.btnInsert);
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
        btnadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn=edtfirst.getText().toString().trim();
                String ln=edtlast.getText().toString().trim();

                if(!(fn.isEmpty()&& ln.isEmpty()&& imageUrI==null)){
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
                                    newPost.child("Name").setValue(fn);
                                    newPost.child("location").setValue(ln);
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


