package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.FM_ABOUT_APP;
import static com.example.finalproject.utils.Constants.FM_HELP;
import static com.example.finalproject.utils.Constants.FM_HOME;
import static com.example.finalproject.utils.Constants.RB_V;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

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
import com.example.finalproject.view.activity.menu.Fragment_Search;
import com.example.finalproject.view.activity.menu.Fragment_Property;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.example.finalproject.view.activity.menu.Fragment_Profile;
import com.example.finalproject.view.activity.menu.HelpFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends BaseActivity{
    private ActivityHomeBinding binding;
    private HeaderDrawerBinding headerBinding;
    private int currentFragment = FM_HOME;

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

        binding.appBarMain.navBottom.setSelectedItemId(R.id.itemHome);
        replaceframent(new Fragment_Home());

        handleLogOutButton();

        roundedBotNav();

        handleBottomNavigation();

        handleNavDrawer();


    }

    private void handleNavDrawer() {
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemAboutApp:
                        replaceframent(new AboutAppFragment());
                        currentFragment = FM_ABOUT_APP;
                        break;

                    case R.id.itemHelp:
                        replaceframent(new HelpFragment());
                        currentFragment = FM_HELP;
                        break;
                }

                unCheckAllMenuItems(binding.appBarMain.navBottom.getMenu());
                return true;
            }
        });
    }

    private void handleBottomNavigation() {
        tenantRole();
        headerBinding.role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == RB_V) {
                    headerBinding.tenant.setChecked(true);
                    tenantRole();
                }
                switch (checkedId) {
                    case R.id.tenant:
                        tenantRole();
                        break;

                    case R.id.owner:
                        ownerRole();
                        break;

                }
            }
        });
    }

    private void ownerRole() {
        binding.appBarMain.navBottom.getMenu().clear();
        binding.appBarMain.navBottom.inflateMenu(R.menu.menu_nav_bot_owner);
        binding.appBarMain.navBottom.getMenu().findItem(R.id.itemProperty).setChecked(true);
        replaceframent(new PropertyFragment());
        binding.appBarMain.navBottom.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.itemProperty:
                    fragment = new PropertyFragment();
                    break;

                case R.id.itemProf:
                    fragment = new Fragment_Profile();
                    break;

            }
            replaceframent(fragment);
            unCheckAllMenuItems(binding.navView.getMenu());
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            return true;
        });

    }

    private void tenantRole() {
        binding.appBarMain.navBottom.getMenu().clear();
        binding.appBarMain.navBottom.inflateMenu(R.menu.nav_bottom_menu);
        binding.appBarMain.navBottom.getMenu().findItem(R.id.itemHome).setChecked(true);
        replaceframent(new Fragment_Home());

        binding.appBarMain.navBottom.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.itemHome:
                    fragment = new Fragment_Home();
                    break;

                case R.id.itemFavorite:
                    fragment = new Fragment_Property();
                    break;

                case R.id.itemChat:
                    fragment = new Fragment_Search();
                    break;

                case R.id.itemProf:
                    fragment = new Fragment_Profile();
                    break;

            }
            replaceframent(fragment);
            unCheckAllMenuItems(binding.navView.getMenu());
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            return true;
        });
    }

    private void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    private void roundedBotNav() {
        //Corner radius
        float radius = 40.0F;

        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) binding.appBarMain.navBottom.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, radius)
                        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                        .build());
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

    private void replaceframent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }


}




