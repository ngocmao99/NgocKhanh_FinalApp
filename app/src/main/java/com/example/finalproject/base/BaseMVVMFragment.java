package com.example.finalproject.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewbinding.ViewBinding;

public abstract class BaseMVVMFragment<V extends ViewBinding, VM extends BaseViewModel> extends BaseFragment {
    private V binding;
    private VM viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getViewModelProviderOwner()).get(getViewModelClass());
        viewModel.isPopBackStack = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = getLayoutBinding();
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!viewModel.isPopBackStack) {
            registerViewModelObs();
            registerBaseViewModelObs();
            registerViewEvent();
            initialize();
        }
        viewModel.isPopBackStack = true;
    }

    protected V getViewBinding() {
        return binding;
    }

    protected VM getViewModel() {
        return viewModel;
    }

    protected ViewModelStoreOwner getViewModelProviderOwner() {
        return this;
    }

    protected abstract Class<VM> getViewModelClass();

    protected abstract V getLayoutBinding();

    protected abstract void initialize();

    protected abstract void registerViewEvent();

    protected abstract void registerViewModelObs();

    private void registerBaseViewModelObs() {
        viewModel.errorMessageObs.observe(getViewLifecycleOwner(), this::showToast);

        viewModel.loadingObs.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                ((BaseActivity) getActivity()).showLoading();
            } else {
                ((BaseActivity) getActivity()).hideLoading();
            }
        });
    }
}