package com.example.finalproject.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.finalproject.view.activity.menu.Fragment_Feed;
import com.example.finalproject.view.activity.menu.Fragment_Home;
import com.example.finalproject.view.activity.menu.Fragment_Profile;
import com.example.finalproject.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private RecyclerView recyclerView;


    private static final int Fragment_Home =1;
    private static final int Fragment_Feed=2;
    private static final int Fragment_Profile=3;
    private static final int Fragment_Favorite=4;
    private int currentFragment = Fragment_Home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        replaceframent(new Fragment_Home());
        }


    













    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    int id =  item.getItemId();
        if (id == R.id.nav_home){
         if(Fragment_Home!= currentFragment){
             replaceframent(new Fragment_Home());
             currentFragment = Fragment_Home;
         }
        }else if(id == R.id.nav_Feed){
            if(Fragment_Feed!= currentFragment){
                replaceframent(new Fragment_Feed());
                currentFragment = Fragment_Feed;
            }
        }else if(id == R.id.nav_Profile){
            if(Fragment_Profile!= currentFragment){
                replaceframent(new Fragment_Profile());
                currentFragment = Fragment_Profile;
            }
        }else if(id == R.id.nav_Favorite){
            if(Fragment_Favorite!= currentFragment){
                replaceframent(new Fragment_Profile());
                currentFragment = Fragment_Favorite;
            }
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        drawerLayout.closeDrawer(GravityCompat.START);






        return true;
    }
    private void replaceframent (Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
}


