package com.example.finalproject.view.activity.menu;

import static com.example.finalproject.utils.Constants.FB_LINK;
import static com.example.finalproject.utils.Constants.GITHUB_LINK;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentAboutAppBinding;
import com.example.finalproject.databinding.HomefragmentBinding;

import mehdi.sakout.aboutpage.AboutPage;

public class AboutAppFragment extends Fragment {
    private FragmentAboutAppBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutAppBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
}