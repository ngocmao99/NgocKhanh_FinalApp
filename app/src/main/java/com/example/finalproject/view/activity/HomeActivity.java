package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.FM_ABOUT_APP;
import static com.example.finalproject.utils.Constants.FM_HELP;
import static com.example.finalproject.utils.Constants.FM_HOME;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.databinding.HeaderDrawerBinding;
import com.example.finalproject.view.activity.menu.AboutAppFragment;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.example.finalproject.view.activity.menu.HelpFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int currentFragment = FM_HOME;
    private ActivityHomeBinding binding;
    private HeaderDrawerBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View binding instead of findViewById
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        //using view binding with header drawer
        View headerView = binding.navView.getHeaderView(0);
        headerBinding = HeaderDrawerBinding.bind(headerView);

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.navView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, binding.drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        replaceframent(new Fragment_Home());

        handleLogOutButton();
    }

    private void handleLogOutButton() {
        binding.btnLogOut.setOnClickListener(v -> {
            if (currentUserIsSignedIn()) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                Animatoo.animateSlideRight(HomeActivity.this);
            }
        });
    }

    @Override
    protected void handleUserSignIn(FirebaseUser mUser) {
        super.handleUserSignIn(mUser);

        if (mUser == null) {
            //This is never happen, but just in case...
            throw new AssertionError("use must be non-null");
        }

        //Update user info to reflect the use now signed in
        //get user info through Firebase User
        String userName = mUser.getDisplayName();
        String email = mUser.getEmail();

        //Set user name 
        headerBinding.userName.setText(userName);
        headerBinding.userEmail.setText(email);

        //TODO: get and set user avatar- this case will implement when profile update function done

    }


    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (FM_HOME != currentFragment) {
                replaceframent(new Fragment_Home());
                currentFragment = FM_HOME;
            }
        } else if (id == R.id.itemAboutApp) {
            if (FM_ABOUT_APP != currentFragment) {
                replaceframent(new AboutAppFragment());
                currentFragment = FM_ABOUT_APP;
                item.setChecked(true);
            }
        } else if (id == R.id.itemHelp) {
            if (FM_HELP != currentFragment) {
                replaceframent(new HelpFragment());
                currentFragment = FM_HELP;
                item.setChecked(true);
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceframent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
}




