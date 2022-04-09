package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.DATE_FORMAT;
import static com.example.finalproject.utils.Constants.FEMALE;
import static com.example.finalproject.utils.Constants.MALE;
import static com.example.finalproject.utils.Constants.MAX_RESULT;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;
import static com.example.finalproject.utils.Constants.OTHER;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.PHONE_REGEX;
import static com.example.finalproject.utils.Constants.POSITION_ADDRESS;
import static com.example.finalproject.utils.Constants.RC_IMAGE;
import static com.example.finalproject.utils.Constants.RC_REPERMISSION;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityEditProfileBinding;
import com.example.finalproject.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private DatabaseReference mPath;

    //This variable contain the imageName that get from the Firebase Storage
    String userImageId;

    //Declare FusedLocationProviderClient
    private FusedLocationProviderClient fLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        //init Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Init Database Ref
        mPath = FirebaseDatabase.getInstance().getReference(PATH_USER);

        //Init FusedLocationProviderClient
        fLocation = LocationServices.getFusedLocationProviderClient(EditProfileActivity.this);

        setContentView(binding.getRoot());

        handleBackButton();

        handleUploadAvatar();

        showUserInfo();

        validate();

        validateFullName();

        validateDob();

        validateGender();

        validatePhone();

        getCurrentLocation();

    }


    private void getCurrentLocation() {
        binding.btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    getUserLocation();
                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            RC_REPERMISSION);
                }

            }

    });
}

    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        fLocation.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {

                    hideLoading();
                    //Init Location
                    Location userLocation = task.getResult();

                    try {
                        //Init GeoCoder
                        Geocoder gc = new Geocoder(EditProfileActivity.this, Locale.US);

                        //Init address list
                        List<Address> addressList = gc.getFromLocation(userLocation.getLatitude(), userLocation.getLongitude(), MAX_RESULT);

                        showToast("Latitude: " + addressList.get(POSITION_ADDRESS).getLatitude()
                                + "\nLongitude: " + addressList.get(POSITION_ADDRESS).getLongitude() +
                                "\nZipcode: " + addressList.get(POSITION_ADDRESS).getPostalCode()
                                + "\nPovince/City: " + addressList.get(POSITION_ADDRESS).getAdminArea());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    hideLoading();
                    showToast("Can access your current location, please check GPS permission again...!");
                }


            }
        });


    }

    private boolean isGPSEnable() {
        LocationManager locationManager = null;
        boolean isEnable = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

    private void showUserInfo() {
        showLoading();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mPath.orderByChild(USER_ID).equalTo(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        //Declare a new user object to contain the data get from firebase
                        User cUser = ds.getValue(User.class);

                        //handle loading the data into screen
                        binding.tietFullName.setText(cUser.getFullName());
                        binding.tietEmail.setText(cUser.getEmail());
                        binding.mactvDob.setText(cUser.getDob());

                        //handle phone number
                        String userPhone = cUser.getPhoneNumber().trim();
                        if (!userPhone.isEmpty()) {
                            binding.tietPhoneNumber.setText(userPhone);
                        }

                        //handle user image
                        userImageId = cUser.getUserImgId().trim();
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
                        String uGender = cUser.getGender().trim();
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
                    }

                    hideLoading();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Validate information before upload theme, to expect the issue missing important info.
    private void validate() {

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //handle main validate
                //first user full name
                String fullName = Objects.requireNonNull(binding.tietFullName.getText().toString().trim());
                String phoneNumber = Objects.requireNonNull(binding.tietPhoneNumber.getText().toString().trim());

                if (TextUtils.isEmpty(fullName)) {
                    binding.tilFullName.setErrorEnabled(true);
                    binding.tilFullName.setError(getString(R.string.error_name_signup));
                    binding.tilFullName.requestFocus();
                }

                //phone number
                if (TextUtils.isEmpty(phoneNumber)) {
                    binding.tilPhoneNumber.setErrorEnabled(true);
                    binding.tilPhoneNumber.setError(getString(R.string.error_empty_phoneNumber));
                    binding.tilPhoneNumber.requestFocus();
                } else if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber)) {
                    showToast("Please fill out the required fields");
                    binding.tietPhoneNumber.requestFocus();
                } else if ((!fullName.matches(NAME_REGEX1) || fullName.matches(NAME_REGEX2)) || fullName.length() >= 100
                        || (phoneNumber.length() > 0 && phoneNumber.length() < 10) || phoneNumber.matches(PHONE_REGEX)) {
                    showToast("Your input is invalid.");
                    binding.tilFullName.requestFocus();

                } else {
                    showToast("Successful validate");
                    showLoading();
                    handleUploadUserInfo();
                }

            }
        });
    }

    private void handleUploadUserInfo() {
        if (imageUri != null && !userImageId.equals(imageName)) {
            putImageInStorage();
            deletePreviousAvatar();
            showToast("Image uri existing");
        } else {
            hideLoading();
            showToast("Image uri isn't existing");
        }
    }

    private void deletePreviousAvatar() {
        StorageReference delRef = mStorageRef.child("Avatars/" + userImageId);

        delRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    showToast("Deleted Successful");
                } else {
                    showToast("Delete Failure");
                }
            }
        });

    }


    private void putUserInRTD() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> uUser = new HashMap<>();
        uUser.put("fullName", binding.tietFullName.getText().toString().trim());
        uUser.put("dob", binding.mactvDob.getText().toString().trim());
        uUser.put("phoneNumber", binding.tietPhoneNumber.getText().toString().trim());
        uUser.put("gender", getUserGender());
        uUser.put("userImgId", imageName);

        mPath.child(mUser.getUid()).updateChildren(uUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoading();
                    showToast("Updated successful...!");
                } else {
                    hideLoading();
                    showToast("Update failed..!");
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


    //validate phone numbers
    private void validatePhone() {
        binding.tietPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumbers = s.toString().trim();
                if (s.length() == 0) {
                    binding.tilPhoneNumber.setErrorEnabled(true);
                    binding.tilPhoneNumber.setError(getString(R.string.error_empty_phoneNumber));
                    binding.tilPhoneNumber.requestFocus();
                } else if (s.length() > 0 && s.length() < 10 || phoneNumbers.matches(PHONE_REGEX)) {
                    binding.tilPhoneNumber.setErrorEnabled(true);
                    binding.tilPhoneNumber.setError(getString(R.string.error_not_match_pattern_phone));
                    binding.tilPhoneNumber.requestFocus();
                } else {
                    binding.tilPhoneNumber.setErrorEnabled(false);
                }

            }
        });
    }


    //Validate gender radio button group
    private void validateGender() {
        binding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            TextView tv_error = findViewById(R.id.tv_error);
            if (binding.rgGender.getCheckedRadioButtonId() == -1) {
                tv_error.setText(getString(R.string.rbtn_nonselect));
                tv_error.setVisibility(View.VISIBLE);
            } else {

                switch (checkedId) {
                    case R.id.female:
                        tv_error.setVisibility(View.GONE);
                        break;

                    case R.id.male:
                        tv_error.setVisibility(View.GONE);
                        break;

                    case R.id.other:
                        tv_error.setVisibility(View.GONE);
                        break;

                }
            }
        });
    }

    //validate dob
    private void validateDob() {
        binding.mactvDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, (datePicker, i, i1, i2) -> {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                binding.mactvDob.setText(simpleDateFormat.format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });


    }

    //validate Full name
    private void validateFullName() {
        binding.tietFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString().trim();
                if (s.length() == 0) {
                    binding.tilFullName.setErrorEnabled(true);
                    binding.tilFullName.setError(getString(R.string.error_name_signup));
                    binding.tilFullName.requestFocus();
                } else if (!name.matches(NAME_REGEX1) || name.matches(NAME_REGEX2)) {
                    binding.tilFullName.setErrorEnabled(true);
                    binding.tilFullName.setError(getString(R.string.error_name_unique_signup));
                    binding.tilFullName.requestFocus();
                } else if (s.length() > 100) {
                    binding.tilFullName.setErrorEnabled(true);
                    binding.tilFullName.setError(getString(R.string.error_over_character));
                } else {
                    binding.tilFullName.setErrorEnabled(false);
                }

            }
        });
    }

    //upload Avatar
    private void handleUploadAvatar() {
        binding.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Select Image
                selectImage();
            }
        });

    }

    private void selectImage() {

        //Create a intent to access gallery of device
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RC_IMAGE);


    }

    //handle startActivityForResult(intent,RC_IMAGE);
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

    //handle put image in Firebase Storage
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
                            putUserInRTD();
                        }
                    });

                } else {
                    hideLoading();
                    showToast("Failed upload...!");
                }
            }
        });
    }


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