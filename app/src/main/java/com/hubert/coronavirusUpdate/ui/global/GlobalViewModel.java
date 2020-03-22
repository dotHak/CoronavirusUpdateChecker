package com.hubert.coronavirusUpdate.ui.global;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hubert.coronavirusUpdate.api.ApiClient;
import com.hubert.coronavirusUpdate.api.ApiService;
import com.hubert.coronavirusUpdate.model.Total;

import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GlobalViewModel extends ViewModel {

    private MutableLiveData<String> cases;
    private MutableLiveData<String> deaths;
    private MutableLiveData<String> recovered;
    static boolean totalError;


    public GlobalViewModel() {
        cases = new MutableLiveData<>();
        deaths = new MutableLiveData<>();
        recovered = new MutableLiveData<>();
        totalError = false;
        setData();
    }

    public void setData(){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<Total> totalCall = apiService.getAllTotal();
        totalCall.enqueue(new Callback<Total>() {
            @Override
            public void onResponse(Call<Total> call, Response<Total> response) {
                Total total = response.body();
                cases.setValue(NumberFormat.getInstance().format(total.getCases()));
                deaths.setValue(NumberFormat.getInstance().format(total.getDeaths()));
                recovered.setValue(NumberFormat.getInstance().format(total.getRecovered()));
            }

            @Override
            public void onFailure(Call<Total> call, Throwable t) {
                totalError = true;
            }
        });


    }



    public LiveData<String> getCases() {
        setData();
        return cases;
    }

    public MutableLiveData<String> getDeaths() {
        return deaths;
    }

    public MutableLiveData<String> getRecovered() {
        return recovered;
    }

}