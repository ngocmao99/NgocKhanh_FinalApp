package com.example.finalproject.view.activity;

import static com.example.finalproject.utils.Constants.FB_LINK;
import static com.example.finalproject.utils.Constants.GITHUB_LINK;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.finalproject.R;
import com.example.finalproject.base.BaseActivity;
import com.example.finalproject.databinding.ActivityAboutAppBinding;

public class AboutAppActivity extends BaseActivity {
    private ActivityAboutAppBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showAppInfo();
        handleBackButton();
    }

    private void showAppInfo() {
        binding.aboutPage.setName(getString(R.string.txt_name_dev));
        binding.aboutPage.setAppName(getString(R.string.fluffy_home));
        binding.aboutPage.setAppDescription(getString(R.string.app_description));
        binding.aboutPage.addEmailLink(getString(R.string.email_link));
        binding.aboutPage.addFacebookLink(FB_LINK);
        binding.aboutPage.addGitHubLink(GITHUB_LINK);
        binding.aboutPage.setAppIcon(R.drawable.ic_app_logo_teal);
        binding.aboutPage.setCover(R.drawable.firstpicture);
        binding.aboutPage.setDescription(getString(R.string.description_myself));
    }

    // Back button on tool bar
    private void handleBackButton(){
        binding.toolBar.toolBarBack.setOnClickListener(view -> {
            onBackPressed();
            Animatoo.animateSlideRight(this);
            onSupportNavigateUp();
        });
        binding.toolBar.toolbarTitle.setText(getString(R.string.txt_about_app));
        binding.toolBar.toolbarTitle.setVisibility(View.VISIBLE);
    }
}