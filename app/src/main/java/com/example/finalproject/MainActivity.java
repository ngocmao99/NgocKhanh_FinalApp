package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private TextView tvSkip;
    private ViewPager viewPager;
    private RelativeLayout layoutBottom;
    private CircleIndicator circleIndicator;
    private LinearLayout layoutNext;
    private ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    initUI();
    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    viewPager.setAdapter(viewPagerAdapter);
    circleIndicator.setViewPager(viewPager);
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position == 2){
                tvSkip.setVisibility(View.GONE);
                layoutBottom.setVisibility(View.GONE);
            }else{
                tvSkip.setVisibility(View.VISIBLE);
                layoutBottom.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });

    }
    private void initUI(){
        tvSkip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.view_pager);
        layoutBottom = findViewById(R.id.layout_bottom);
        circleIndicator = findViewById(R.id.circle_indicator);
        layoutNext = findViewById(R.id.layout_next);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem()<2){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }
            }
        });
    }
}