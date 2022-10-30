package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.AVATAR;
import static com.example.finalproject.utils.Constants.DEBUG_LOG;
import static com.example.finalproject.utils.Constants.DEFAULT_VALUE;
import static com.example.finalproject.utils.Constants.DISTRICT;
import static com.example.finalproject.utils.Constants.DOB;
import static com.example.finalproject.utils.Constants.FEMALE;
import static com.example.finalproject.utils.Constants.FULLNAME;
import static com.example.finalproject.utils.Constants.GENDER;
import static com.example.finalproject.utils.Constants.HOUSE_NUMBER;
import static com.example.finalproject.utils.Constants.LATITUDE;
import static com.example.finalproject.utils.Constants.LONGITUDE;
import static com.example.finalproject.utils.Constants.MALE;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;
import static com.example.finalproject.utils.Constants.OTHER;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.PHONE_NUMBER;
import static com.example.finalproject.utils.Constants.PHONE_REGEX;
import static com.example.finalproject.utils.Constants.POSTAL_CODE;
import static com.example.finalproject.utils.Constants.PROVINCE;
import static com.example.finalproject.utils.Constants.RC_IMAGE;
import static com.example.finalproject.utils.Constants.USER_ID;
import static com.example.finalproject.utils.Constants.WARD;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityEditProfileBinding;
import com.example.finalproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;

    //Image Uri
    private Uri imageUri;

    //Image name
    private String imageName;

    private String uGender;

    //Declare a variable FirebaseStorage
    private StorageReference mStorageRef;

    //Declare a Database Reference variable
    private DatabaseReference mRef;

    //This variable contain the imageName that get from the Firebase Storage
    String userImageId;

    //alert dialog
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //init Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Init Database Ref
        mRef = FirebaseDatabase.getInstance().getReference();

        showUserInfo();
        selectAvatar();
        validate();
    }


    private void showUserInfo() {
        showLoading();
        //get current user
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mRef.child(PATH_USER).orderByChild(USER_ID).equalTo(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        //Declare a new user object to contain the data get from firebase
                        User user = ds.getValue(User.class);

                        //handle loading the data into screen
                        binding.tietFullName.setText(user.getFullName());
                        binding.tietEmail.setText(user.getEmail());
                        binding.mactvDob.setText(user.getDob());

                        //handle phone number
                        String userPhone = user.getPhoneNumber().trim();
                        if (!userPhone.isEmpty()) {
                            binding.tietPhoneNumber.setText(userPhone);
                        }

                        //handle user image
                        userImageId = user.getUserImgId().trim();
                        if (!userImageId.isEmpty()) {
                            mStorageRef.child("Avatars/" + userImageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString().trim();

                                    Glide.with(EditProfileActivity.this).load(imageUrl).centerCrop().into(binding.userImg);
                                }
                            });
                        } else {
                            Glide.with(EditProfileActivity.this)
                                    .load(R.drawable.ic_baseline_photo_camera_24)
                                    .centerInside()
                                    .into(binding.userImg);
                        }

                        //handle user gender
                        String uGender = user.getGender().trim();
                        if (uGender != null) {
                            switch (uGender) {
                                case FEMALE:
                                    binding.rgGender.check(R.id.female);
                                    break;
                                case MALE:
                                    binding.rgGender.check(R.id.male);
                                    break;
                                case OTHER:
                                    binding.rgGender.check(R.id.other);
                                    break;
                            }

                        } else {
                            binding.rgGender.check(R.id.other);
                        }
                        //handle address tab
                        //if latitude and longitude have value are 0.0d, then implements getUserLocation()
//                        if (user.getLatitude() != DEFAULT_RED_CODE && user.getLongitude() != DEFAULT_RED_CODE) {
//                            binding.mactvProvince.setText(user.getProvince().trim());
//                            binding.mactvDist.setText(user.getDistrict().trim());
//                            binding.mactvWard.setText(user.getWard().trim());
//                            binding.tietHNumber.setText(user.getHouseNumber().trim());
//                        }else{
//                            getUserLocation();
//                        }
                    }
                    hideLoading();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_LOG, "No data found." );
            }
        });
    }

    //Validate information before upload them, to expect the issue missing important info.
    private void validate() {

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();

//                //handle main validate
//                //first user full name
//                String fullName = Objects.requireNonNull(binding.tietFullName.getText().toString().trim());
//                String phoneNumber = Objects.requireNonNull(binding.tietPhoneNumber.getText().toString().trim());
//
//                if (TextUtils.isEmpty(fullName)) {
//                    binding.tilFullName.setErrorEnabled(true);
//                    binding.tilFullName.setError(getString(R.string.error_name_signup));
//                    binding.tilFullName.requestFocus();
//                }
//
//                //phone number
//                if (TextUtils.isEmpty(phoneNumber)) {
//                    binding.tilPhoneNumber.setErrorEnabled(true);
//                    binding.tilPhoneNumber.setError(getString(R.string.error_empty_phoneNumber));
//                    binding.tilPhoneNumber.requestFocus();
//                } else if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber)) {
//                    showToast(getString(R.string.error_blank_required_fields));
//                    binding.tietPhoneNumber.requestFocus();
//                } else if ((!fullName.matches(NAME_REGEX1) || fullName.matches(NAME_REGEX2)) || fullName.length() >= 100
//                        || (phoneNumber.length() > 0 && phoneNumber.length() < 10) || phoneNumber.matches(PHONE_REGEX)) {
//                    showToast(getString(R.string.error_invalid_format));
//                    binding.tilFullName.requestFocus();
//
//                } else {
                    PopupDialog.getInstance(EditProfileActivity.this)
                            .setStyle(Styles.STANDARD)
                            .setHeading("Update information" )
                            .setHeading("Are you sure you want to update these information?" )
                            .setPopupDialogIcon(R.drawable.warning_icon)
                            .setCancelable(false)
                            .showDialog(new OnDialogButtonClickListener() {
                                @Override
                                public void onPositiveClicked(Dialog dialog) {
                                    super.onPositiveClicked(dialog);
                                    updateProfile();
                                }

                                @Override
                                public void onNegativeClicked(Dialog dialog) {
                                    super.onNegativeClicked(dialog);
                                    dialog.dismiss();
                                }
                            });

                }
        });
    }

    private void updateProfile() {
        if (imageUri != null && !userImageId.equals(imageName)) {
            //method upload avatar and info if user provide imageUri
            putImageInStorage();
            // when user provide an new imageUri, the old imageUri that formatted and assigned the old imageName variable- put on Firebase Storage
            //previous will delete and update new imageName
            deletePreviousAvatar();
            hideLoading();
            Log.d(DEBUG_LOG, "ImageUri is existing!" );
        } else {
            //method upload avatar and info if users don't provide imageUri
            uploadInfoWithoutImage();
            hideLoading();
            Log.d(DEBUG_LOG, "ImageUri doesn't contain any value" );
        }
    }

    //method upload avatar and info if users don't provide imageUri
    private void uploadInfoWithoutImage() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> uUser = new HashMap<>();
        uUser.put(FULLNAME, binding.tietFullName.getText().toString().trim());
        uUser.put(DOB, binding.mactvDob.getText().toString().trim());
        uUser.put(PHONE_NUMBER, binding.tietPhoneNumber.getText().toString().trim());
        uUser.put(GENDER, getUserGender());
        uUser.put(AVATAR, userImageId);
        uUser.put(LATITUDE, DEFAULT_VALUE);
        uUser.put(LONGITUDE, DEFAULT_VALUE);
        uUser.put(PROVINCE, binding.mactvProvince.getText().toString().trim());
        uUser.put(POSTAL_CODE, DEFAULT_VALUE);
        uUser.put(DISTRICT, binding.mactvDist.getText().toString().trim());
        uUser.put(WARD, binding.mactvWard.getText().toString().trim());
        uUser.put(HOUSE_NUMBER, binding.tietHNumber.getText().toString().trim());

        mRef.child(mUser.getUid()).updateChildren(uUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoading();
                    showToast("Updated successful...!" );
                } else {
                    hideLoading();
                    showToast("Update failed..!" );
                }
            }
        });


    }

    private void deletePreviousAvatar() {
        StorageReference delRef = mStorageRef.child("Avatars/" + userImageId);

        delRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(DEBUG_LOG, "Old image was deleted from storage successful." );
                } else {
                    Log.d(DEBUG_LOG, "Delete Old Image is failed!" );
                }
            }
        });

    }

    // push data to firebase realtime
    private void putUser() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> uUser = new HashMap<>();
        uUser.put(FULLNAME, binding.tietFullName.getText().toString().trim());
        uUser.put(DOB, binding.mactvDob.getText().toString().trim());
        uUser.put(PHONE_NUMBER, binding.tietPhoneNumber.getText().toString().trim());
        uUser.put(GENDER, getUserGender());
        uUser.put(AVATAR, imageName);
        uUser.put(LATITUDE, DEFAULT_VALUE);
        uUser.put(LONGITUDE, DEFAULT_VALUE);
        uUser.put(PROVINCE, binding.mactvProvince.getText().toString().trim());
        uUser.put(POSTAL_CODE, DEFAULT_VALUE);
        uUser.put(DISTRICT, binding.mactvDist.getText().toString().trim());
        uUser.put(WARD, binding.mactvWard.getText().toString().trim());
        uUser.put(HOUSE_NUMBER, binding.tietHNumber.getText().toString().trim());

        mRef.child(mUser.getUid()).updateChildren(uUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoading();
                    showToast("Updated successful...!" );
                } else {
                    hideLoading();
                    showToast("Update failed..!" );
                }
            }
        });

    }

    private String getUserGender() {
        String gender = "";
        //Declare a int variable to contain gender Item Id
        int genderItemId = binding.rgGender.getCheckedRadioButtonId();
        if (genderItemId != -1) {
            //Declare a radio button variable => prepared to get text of the radio button
            RadioButton rb = findViewById(genderItemId);
            //get text checked radio button
            gender = rb.getText().toString();
            return gender;
        } else {
            return OTHER;
        }
    }

    //
//    //validate phone numbers
//    private void validatePhone() {
//        binding.tietPhoneNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String phoneNumbers = s.toString().trim();
//                if (s.length() > 0) {
//                    binding.tilPhoneNumber.setErrorEnabled(false);
//                } else if (s.length() > 0 && s.length() < 12 || !phoneNumbers.matches(PHONE_REGEX)) {
//                    binding.tilPhoneNumber.setErrorEnabled(false);
//                }
//            }
//        });
//    }
//
//
//    //Validate gender radio button group
//    private void validateGender() {
//        binding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
//            TextView tv_error = findViewById(R.id.tv_error);
//            if (binding.rgGender.getCheckedRadioButtonId() == -1) {
//                tv_error.setText(getString(R.string.rbtn_nonselect));
//                tv_error.setVisibility(View.VISIBLE);
//            } else {
//
//                switch (checkedId) {
//                    case R.id.female:
//                        tv_error.setVisibility(View.GONE);
//                        break;
//
//                    case R.id.male:
//                        tv_error.setVisibility(View.GONE);
//                        break;
//
//                    case R.id.other:
//                        tv_error.setVisibility(View.GONE);
//                        break;
//
//                }
//            }
//        });
//    }
//
//    //validate dob
//    private void validateDob() {
//        binding.mactvDob.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int month = calendar.get(Calendar.MONTH);
//            int year = calendar.get(Calendar.YEAR);
//            DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, (datePicker, i, i1, i2) -> {
//                calendar.set(i, i1, i2);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
//                binding.mactvDob.setText(simpleDateFormat.format(calendar.getTime()));
//            }, year, month, day);
//            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//            datePickerDialog.show();
//        });
//
//
//    }
//
//    //validate Full name
//    private void validateFullName() {
//        binding.tietFullName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String name = s.toString().trim();
//                if (s.length() > 0) {
//                    binding.tilFullName.setErrorEnabled(false);
//                } else if (name.matches(NAME_REGEX1) || !name.matches(NAME_REGEX2)) {
//                    binding.tilFullName.setErrorEnabled(false);
//                } else if (s.length() <= 100) {
//                    binding.tilFullName.setErrorEnabled(false);
//                }
//            }
//        });
//    }
//
    //upload Avatar feature
    private void selectAvatar() {
        binding.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Select Image feature
                //Create a intent to access gallery of device
                Intent intent = new Intent();
                intent.setType("image/*" );
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, RC_IMAGE);
            }
        });

    }

    //startActivityForResult(intent,RC_IMAGE);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_IMAGE && data != null && data.getData() != null) {
            //Set imageUri is getData()
            imageUri = data.getData();
            //set imageUri to the ImageView
            Glide.with(this).load(imageUri).centerCrop().into(binding.userImg);

        }
    }

    //put image to Firebase Storage feature
    private void putImageInStorage() {
        //Create formatter to apply into image name
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        imageName = formatter.format(now);
        //put image process using Storage Ref
        mStorageRef.child("Avatars/" + imageName).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = Objects.requireNonNull(uri.toString());
                            //Set avatar by using Glide
                            Glide.with(EditProfileActivity.this).load(imageUrl).centerCrop().into(binding.userImg);
                            showToast("Successful uploaded avatar with name: " + imageName + "\n DownloadURL: " + imageUrl);
                            //If the user avatar upload on the Storage successful, the process edit information will implement
                            putUser();
                        }
                    });
                } else {
                    hideLoading();
                    showToast("Failed upload...!" );
                }
            }
        });
    }

    //Back button on tool bar
    private void handleBackButton() {
        binding.toolBar.toolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Animatoo.animateSlideRight(EditProfileActivity.this);
                onSupportNavigateUp();
            }
        });
    }
}