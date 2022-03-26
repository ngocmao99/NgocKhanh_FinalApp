package com.example.finalproject.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {
    public boolean isPopBackStack = false;
    protected CompositeDisposable disposable = new CompositeDisposable();
    protected MutableLiveData<String> errorMessageObs = new MutableLiveData<>();
    protected MutableLiveData<Boolean> loadingObs = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    protected void showErrorMessage(String msg) {
        errorMessageObs.postValue(msg);
    }

    protected void showLoading() {
        loadingObs.postValue(true);
    }

    protected void hideLoading() {
        loadingObs.postValue(false);
    }
}
