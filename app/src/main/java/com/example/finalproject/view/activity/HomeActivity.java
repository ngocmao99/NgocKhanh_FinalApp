package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.FM_HOME;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.example.finalproject.view.activity.menu.Fragment_Profile;
import com.example.finalproject.view.activity.menu.Fragment_Property;
import com.example.finalproject.view.activity.menu.Fragment_Search;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends BaseActivity {
    private ActivityHomeBinding binding;
    private int currentFragment = FM_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View binding instead of findViewById
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        binding.appBarMain.navBottom.setSelectedItemId(R.id.itemHome);
        replaceframent(new Fragment_Home());

        roundedBotNav();

        handleBottomNavigation();

    }

    private void handleBottomNavigation() {
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


    @Override
    protected void handleUserSignIn(FirebaseUser mUser) {
        super.handleUserSignIn(mUser);

        if (mUser == null) {
            //This is never happen, but just in case...
            throw new AssertionError("use must be non-null");
        }
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




