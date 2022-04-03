package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.FULLNAME;
import static com.example.finalproject.utils.Constants.PATH_USER;
import static com.example.finalproject.utils.Constants.USER_EMAIL;
import static com.example.finalproject.utils.Constants.USER_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseFragment;
import com.example.finalproject.databinding.ProfilefragmentBinding;
import com.example.finalproject.view.activity.InfoFragment;
import com.example.finalproject.view.activity.PasswordChangeFragment;
import com.example.finalproject.view.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Profile extends BaseFragment {
    private ProfilefragmentBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = ProfilefragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //handle TabLayout and ViewPager
        handleTabLayout();

        //Method getUserProfile - get username, user email, user id, user image;
        getUserProfile();

    }

    private void handleTabLayout() {
        binding.viewPager.beginFakeDrag();

        binding.viewPager.setAdapter( new TabAdapter(getChildFragmentManager(),binding.tabLayoutProfile.getTabCount()));

        binding.tabLayoutProfile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void getUserProfile() {
        /** Is user signed in
         *
         * if true => get user information
         */
        if (currentUserIsSignedIn()){
            mAuth = FirebaseAuth.getInstance();

            //get user Id
            String uId = mAuth.getCurrentUser().getUid();

            //Declare direct to user tree
            //PATH_USER import from Constants.java in utils package.
            mRef = FirebaseDatabase.getInstance().getReference(PATH_USER);

            //method read data of Firebase Real-time Database. Because in Declare mRef defined the path User, so does not defined directly child in this line
            //USER_ID import from Constants.java in utils package, that is a string value symbol for the userId fields in Users tree.
            mRef.orderByChild(USER_ID).equalTo(uId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //handle the user date get from database
                    if (snapshot.exists()){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            //get the data
                            String userName = dataSnapshot.child(FULLNAME).getValue(String.class);

                            //Loading data into the UI
                            binding.userName.setText(userName);
                        }
                    }else {
                        showToast("The data with "+uId+"doesn't existing");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    showToast(error.getMessage());
                }
            });

        }
        else {
            showToast("User is not signed");
        }

    }

}
