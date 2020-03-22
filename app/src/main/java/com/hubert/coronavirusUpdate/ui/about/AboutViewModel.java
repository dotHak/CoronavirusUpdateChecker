package com.hubert.coronavirusUpdate.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static final String ABOUT = "A simple app to check coronavirus(COVID-19) data \n\n\nMade with ❤ by Hubert";

    public AboutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(ABOUT);
    }

    public LiveData<String> getText() {
        return mText;
    }
}