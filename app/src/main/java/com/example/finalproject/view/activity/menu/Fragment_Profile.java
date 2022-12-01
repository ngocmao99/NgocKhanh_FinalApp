package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.BODY_MAIL;
import static com.example.finalproject.utils.Constants.DEBUG_LOG;
import static com.example.finalproject.utils.Constants.EMAIL_REGEX;
import static com.example.finalproject.utils.Constants.MAIL_TO;
import static com.example.finalproject.utils.Constants.NAME_REGEX1;
import static com.example.finalproject.utils.Constants.NAME_REGEX2;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.PHONE_REGEX;
import static com.example.finalproject.utils.Constants.SUBJECT;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.ProfilefragmentBinding;
import com.example.finalproject.models.User;
import com.example.finalproject.view.activity.AboutAppActivity;
import com.example.finalproject.view.activity.ChangePasswordActivity;
import com.example.finalproject.view.activity.EditProfileActivity;
import com.example.finalproject.view.activity.LoginActivity;
import com.example.finalproject.view.activity.NotificationActivity;
import com.example.finalproject.view.activity.SettingActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.Objects;

public class Fragment_Profile extends BaseFragment {
    private ProfilefragmentBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private FirebaseStorage storage;
    private KProgressHUD hud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ProfilefragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        //handle edit profile buttons - button account move to EditProfileActivity.class
        binding.btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
            Animatoo.animateSlideLeft(getActivity());

        });

        //handle change password button -> move to PasswordChangeFragment.class
        binding.btnChangePassword.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
            Animatoo.animateSlideLeft(getActivity());
        });

        //handle change password button -> move to PasswordChangeFragment.class
        binding.btnAboutApp.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), AboutAppActivity.class);
            startActivity(intent);
            Animatoo.animateSlideLeft(getActivity());
        });

        //handle button notification -> move to notification screen
        binding.btnNotification.setOnClickListener(view13 -> {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
            Animatoo.animateSlideLeft(getActivity());
        });

        //handle button setting -> move to setting screen
        binding.btnSetting.setOnClickListener(view14 -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
            Animatoo.animateSlideLeft(getActivity());
        });

        //handle button Contact Us -> show dialog Call or Mail for user
        binding.btnContactUs.setOnClickListener(view15 -> openContactUsDialog(Gravity.BOTTOM));

        // handle log out button -> log out and move to log in screen
        logOut();

        //display user info consists user name, email and avatar on profile header
        showUserInfo();

    }

    private void openContactUsDialog(int center) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogContactView = inflater.inflate(R.layout.custom_alert_dialog_three_option,null);
        dialogBuilder.setView(dialogContactView);
        final AlertDialog contactDialog = dialogBuilder.create();
        contactDialog.setOnShowListener(dialogInterface -> contactDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent));
        contactDialog.show();

        MaterialCardView callButton = dialogContactView.findViewById(R.id.btn_call);
        callButton.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel","0915457260",null)));
            Animatoo.animateSlideLeft(getActivity());
        });

        MaterialCardView sendMailBtn = dialogContactView.findViewById(R.id.btnSendMail);
        sendMailBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater1 = getLayoutInflater();
            View dialogEmailView = inflater1.inflate(R.layout.custom_email_alert, null);
            builder.setView(dialogEmailView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(dialogInterface -> alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent));
            alertDialog.show();

            TextInputEditText userName = dialogEmailView.findViewById(R.id.tiet_fullName);
            TextInputLayout tilUserName = dialogEmailView.findViewById(R.id.til_fullName);
            TextInputEditText emailAddress = dialogEmailView.findViewById(R.id.tietEmail);
            TextInputLayout tilEmail = dialogEmailView.findViewById(R.id.tilEmail);
            TextInputEditText phoneNumber = dialogEmailView.findViewById(R.id.tietPhoneNumber);
            TextInputLayout tilPhoneNumber = dialogEmailView.findViewById(R.id.tilPhoneNumber);
            TextInputEditText message = dialogEmailView.findViewById(R.id.tietMessage);
            TextInputLayout tilMessage = dialogEmailView.findViewById(R.id.tilMessage);
            MaterialCardView btnSend = dialogEmailView.findViewById(R.id.btnSendMail);
            MaterialCardView btnCancel = dialogEmailView.findViewById(R.id.btnCancelMail);
            String receiveAddress = "ngocmao99@gmail.com";

            btnSend.setOnClickListener(view1 -> {
                showToast("Clicked");
                String name = Objects.requireNonNull(userName.getText().toString().trim());
                String email = Objects.requireNonNull(emailAddress.getText().toString().trim());
                String phone = Objects.requireNonNull(phoneNumber.getText().toString().trim());
                String userMessage = Objects.requireNonNull(message.getText().toString().trim());
                if (name.isEmpty()) {
                    tilUserName.setError(getString(R.string.txt_empty_field));
                    tilUserName.setErrorEnabled(true);
                    tilUserName.requestFocus();
                } if (userMessage.isEmpty()) {
                    tilMessage.setError(getString(R.string.txt_empty_field));
                    tilMessage.setErrorEnabled(true);
                    tilMessage.requestFocus();
                } if (phone.length() > 11 && phone.matches(PHONE_REGEX)) {
                    tilPhoneNumber.setError(getString(R.string.error_not_match_pattern_phone));
                    tilPhoneNumber.setErrorEnabled(true);
                    tilPhoneNumber.requestFocus();
                } if (!email.matches(EMAIL_REGEX)) {
                    tilEmail.setError(getString(R.string.error_invalid_email));
                    tilEmail.setErrorEnabled(true);
                    tilEmail.requestFocus();
                }
                if(name.isEmpty() || userMessage.isEmpty()){
                    showToast("Kindly fill out the required fields!!!");
                }
                else{
                    String subject = "You have a message at Fluffy Home from " + name;
                    String bodyMail = subject+":"+"\nUsername: "+ name+"\nMail address: "+email+"\nPhone number: "+phone+"\nMessage: "+userMessage;
                    Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(MAIL_TO + receiveAddress + SUBJECT + subject
                            + BODY_MAIL + bodyMail));
                    try {
                        startActivity(Intent.createChooser(i, getString(R.string.txt_title_email_intent)));

                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(), getString(R.string.txt_error_intent_email), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnCancel.setOnClickListener(view12 -> alertDialog.dismiss());

            //text change watcher - user name
            userName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String name = editable.toString().trim();
                    if (editable.length() > 0) {
                        tilUserName.setErrorEnabled(false);
                    } else if (name.matches(NAME_REGEX1) || !name.matches(NAME_REGEX2)) {
                        tilUserName.setErrorEnabled(false);
                    }

                }
            });

            //text change watcher - user email
            emailAddress.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String email = editable.toString().trim();
                    if (email.matches(EMAIL_REGEX)) {
                        tilEmail.setErrorEnabled(false);
                    }

                }
            });

            //text change watcher - user phone
            phoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String phoneNumbers = editable.toString().trim();
                    if (editable.length() > 0 && editable.length() < 12 || !phoneNumbers.matches(PHONE_REGEX)) {
                        tilPhoneNumber.setErrorEnabled(false);
                    }

                }
            });

//                 text change watcher - message
            message.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() > 0) {
                        tilMessage.setErrorEnabled(false);
                    }
                }
            });

        });

        MaterialCardView cancelBtn = dialogContactView.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(view -> contactDialog.dismiss());

    }



    private void init() {
        //init variables
        mRef = FirebaseDatabase.getInstance().getReference();
        //init firebase storage
        storage = FirebaseStorage.getInstance();

    }

    private void logOut() {
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupDialog.getInstance(getActivity())
                        .setStyle(Styles.STANDARD)
                        .setHeading("Log Out")
                        .setHeading("Are you sure you want to log out?")
                        .setPopupDialogIcon(R.drawable.warning_icon)
                        .setCancelable(false)
                        .setNegativeButtonTextColor(R.color.first)
                        .showDialog(new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                super.onPositiveClicked(dialog);
                                if (currentUserIsSignedIn()){
                                    FirebaseAuth.getInstance().signOut();
                                    showLoadingDialog();
                                    startActivity(new Intent(getActivity(),LoginActivity.class));
                                    Animatoo.animateSlideRight(getActivity());
                                }
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

    //get and display user image and user name
    private void showUserInfo() {
        if(getCurrentUser()){
            String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mRef.child(PATH_USER).orderByChild(USER_ID).equalTo(currentId).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot ds : snapshot.getChildren()){
                            User currentUser = ds.getValue(User.class);
                            //set and get username
                            binding.tvIntro.setText(getString(R.string.txt_hello)+currentUser.getFullName());
                            //get and set user image /avatar
                            String imageId = currentUser.getUserImgId().trim();
                            //declare reference storage
                            StorageReference storageRef = storage.getReference();
                            //
                            storageRef.child("Avatars/"+imageId).getDownloadUrl().addOnSuccessListener(uri -> {
                                //get url and use trim() to clear the blank.
                                String imageUrl = uri.toString().trim();
                                Glide.with(getActivity()).load(imageUrl).centerCrop().into(binding.headerProfile.userImg);
                            });
                        }
                    }
                    else{
                        Log.e(DEBUG_LOG,"No data found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            startActivity(new Intent(getActivity(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Animatoo.animateSlideRight(getActivity());
        }
    }

    private boolean getCurrentUser() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return mUser != null;
    }

    private void showLoadingDialog(){
        KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(() -> hud.dismiss(), 2000);
    }

}
