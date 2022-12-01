package com.example.finalproject.view.activity;

import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityNotificationBinding;

public class NotificationActivity extends BaseActivity {
        private ActivityNotificationBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityNotificationBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            handleToolbar();
        }

        private void handleToolbar(){
            binding.toolBar.toolbarTitle.setText(getString(R.string.txt_notification));
            binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
            binding.toolBar.toolBarBack.setOnClickListener(view -> {
                onBackPressed();
                Animatoo.animateSlideRight(NotificationActivity.this);
                onNavigateUp();
            });
        }
    }
