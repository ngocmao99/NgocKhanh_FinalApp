package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.DETAIL_KEY;
import static com.example.finalproject.utils.Constants.PATH_PROPERTIES;
import static com.example.finalproject.utils.Constants.P_CREATOR;
import static com.example.finalproject.utils.Constants.P_ID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityPropertyBinding;
import com.example.finalproject.models.Property;
import com.example.finalproject.view.adapter.PropertyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PropertyActivity extends BaseActivity implements PropertyAdapter.OnItemClickListener{
    private PropertyAdapter propertyAdapter;
    private List<Property> properties;
    private RecyclerView recyclerView1, recyclerView;
    private DatabaseReference mRef;
    Button add;
    private String currentImage;
    private SearchView searchView;
    private ActivityPropertyBinding binding;
    private FirebaseUser mUser;
    private String currentUserId;
    private String propertyID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // init path database mRef
        mRef = FirebaseDatabase.getInstance().getReference().child(PATH_PROPERTIES);

        //init firebase user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //get current User Id
        currentUserId = mUser.getUid();

        //config recycle view
        binding.rcvListItem1.setHasFixedSize(true);
        binding.rcvListItem1.setLayoutManager(new GridLayoutManager(PropertyActivity.this, 2));
        //init property list
        properties = new ArrayList<Property>();
        propertyAdapter = new PropertyAdapter( PropertyActivity.this, properties, this);
        binding.rcvListItem1.setAdapter(propertyAdapter);

        //get data from path properties on firebase
        mRef.orderByChild(P_CREATOR).equalTo(currentUserId).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Property property = snapshot.getValue(Property.class);
                if (property!= null){
                    properties.add(property);
                    propertyAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Property property = snapshot.getValue(Property.class);
                if(  properties==null|| properties.isEmpty()){
                    return;
                }
                for (int i=0;i<properties.size();i++){
                    if (property ==null || property.getPropertyId().equals(properties.get(i).getPropertyId())){
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
        //handle create new button
        binding.imgBCreateNewItem.setOnClickListener(v -> {
            Intent intent = new Intent(PropertyActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClickGoToDetailProperty(Property property) {
        Intent intent = new Intent(PropertyActivity.this, PropertyDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DETAIL_KEY,property);
        intent.putExtras(bundle);
        startActivity(intent);
        Animatoo.animateSlideLeft(PropertyActivity.this);
    }

    @Override
    public void onClickEditProperty(Property property) {
        openDialogUpdateProperty(property);

    }

    //open a dialog to update property information
    private void openDialogUpdateProperty(Property property) {
        final Dialog updateDialog = new Dialog(this);
        updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateDialog.setContentView(R.layout.activity_edit_item);
        Window window = updateDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.setCancelable(false);

    }

    @Override
    public void onClickRemoveProperty(Property property) {
        PopupDialog.getInstance(this)
                .setStyle(Styles.STANDARD)
                .setHeading(getString(R.string.txt_title_popup_remove))
                .setHeading(getString(R.string.txt_popup_dialog_subtitle_delete))
                .setPopupDialogIcon(R.drawable.warning_icon)
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.txt_yes))
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onPositiveClicked(Dialog dialog) {
                        super.onPositiveClicked(dialog);
                        showLoading();
                        String propertyId = property.getPropertyId();
                        mRef.orderByChild(P_ID).equalTo(propertyId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    showLoading();
                                    for (DataSnapshot sn : snapshot.getChildren()){
                                        sn.getRef().removeValue();
                                    }
                                    Toasty.success(PropertyActivity.this,getString(R.string.txt_successful_remove_propety),Toasty.LENGTH_LONG,true).show();
                                    startActivity(new Intent(PropertyActivity.this, PropertyActivity.class));
                                    Animatoo.animateFade(PropertyActivity.this);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNegativeClicked(Dialog dialog) {
                        super.onNegativeClicked(dialog);
                        dialog.dismiss();
                    }
                });




    }

    public void showPopup(View v){

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}





