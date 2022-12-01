package com.example.finalproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivitySettingBinding;

public class SettingActivity extends BaseActivity {
    private ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleToolbar();
    }

    private void handleToolbar(){
        binding.toolBar.toolbarTitle.setText(getString(R.string.txt_setting));
        binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
        binding.toolBar.toolBarBack.setOnClickListener(view -> {
            onBackPressed();
            Animatoo.animateSlideRight(SettingActivity.this);
            onNavigateUp();
        });
    }
}