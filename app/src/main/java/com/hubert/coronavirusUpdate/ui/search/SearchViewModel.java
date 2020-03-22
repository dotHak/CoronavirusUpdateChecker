package com.hubert.coronavirusUpdate.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hubert.coronavirusUpdate.api.ApiClient;
import com.hubert.coronavirusUpdate.api.ApiService;
import com.hubert.coronavirusUpdate.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<List<Country>> countryList;
    static boolean listError;

    public SearchViewModel() {
        countryList = new MutableLiveData<>();
        listError = false;
        setData();
    }

    public MutableLiveData<List<Country>> getCountryList() {
        setData();
        return countryList;
    }

    public void setData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Country>> countryCall = apiService.getAllCountries();
        countryCall.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                countryList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                listError = true;
            }
        });
    }
}