package com.example.finalproject.base;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

public abstract class BaseMVVMActivity<V extends ViewBinding, VM extends BaseViewModel> extends BaseActivity {
    private V binding;
    private VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getLayoutBinding();
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(getViewModelClass());
        /*Turn off header bar of android*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        registerBaseViewModelObs();
        registerViewEvent();
        initialize();
        initTitle();
        initViewPager();
    }

    protected V getViewBinding() {
        return binding;
    }

    protected VM getViewModel() {
        return viewModel;
    }

    protected abstract Class<VM> getViewModelClass();

    protected abstract V getLayoutBinding();

    protected abstract void initialize();

    protected abstract void initTitle();

    protected abstract void initViewPager();

    protected abstract void registerViewEvent();


    private void registerBaseViewModelObs() {
        viewModel.errorMessageObs.observe(this, msg -> showToast(msg));

        viewModel.loadingObs.observe(this, isLoading -> {
            if (isLoading) {
                showLoading();
            } else {
                hideLoading();
            }
        });
    }
}