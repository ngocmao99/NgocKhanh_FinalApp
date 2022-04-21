package com.example.finalproject.view.activity.createpropertyfragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateProperty3 extends BaseFragment {
    private View mView;
    private Button btnchooseimage;
    private Button btnuploadimage;
    private TextView alertimage;
    private ProgressDialog progressDialog;
    private static final int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private int upload_count = 0;
    public CreateProperty3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.create_property3, container, false);

        alertimage = mView.findViewById(R.id.alert_image);
        btnchooseimage = mView.findViewById(R.id.btn_choose_image);
        btnuploadimage = mView.findViewById(R.id.btn_upload_image);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Image uploading Please wait ...........");
        btnchooseimage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
        btnuploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                alertimage.setText("if loading take tool long please press the button again");
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
                for(upload_count=0;upload_count< ImageList.size();upload_count++){
                    Uri IndividualImage = ImageList.get(upload_count);
                    StorageReference ImageName = ImageFolder.child("Image"+IndividualImage.getLastPathSegment());
                    ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = String.valueOf(uri);
                                    StoreLink(url);
                                }
                            });
                        }
                    });
                }
            }
        });
        return mView;

    }

    private void StoreLink(String url) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ItemOne");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("ImgLink",url);
        databaseReference.push().setValue(hashMap);
        progressDialog.dismiss();
        alertimage.setText("Image Upload Successfully");
        btnuploadimage.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            if(resultCode==RESULT_OK){
                if(data.getClipData()!=null){
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSelect = 0;
                    while (currentImageSelect<countClipData){

                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSelect = currentImageSelect+1;


                    }
                    alertimage.setVisibility(View.VISIBLE);
                    alertimage.setText("you have Selected"+ ImageList.size()+" Images");
                    btnchooseimage.setVisibility(View.GONE);



                }else
                {
                    Toast.makeText(getContext(), "Please Select Multiple image", Toast.LENGTH_SHORT).show();
                }
            }
        }



    }
}
