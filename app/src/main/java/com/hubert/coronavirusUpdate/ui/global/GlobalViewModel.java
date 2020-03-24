package com.hubert.coronavirusUpdate.ui.global;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hubert.coronavirusUpdate.api.ApiClient;
import com.hubert.coronavirusUpdate.api.ApiService;
import com.hubert.coronavirusUpdate.model.Country;
import com.hubert.coronavirusUpdate.model.Total;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GlobalViewModel extends ViewModel {

    private MutableLiveData<String> cases;
    private MutableLiveData<String> deaths;
    private MutableLiveData<String> recovered;
    private static List<String> countryNames;
    private MutableLiveData<Country> currentCountry;
    static boolean totalError;



    public GlobalViewModel() {
        countryNames = new ArrayList<>();
        setCountryNames();
        cases = new MutableLiveData<>();
        deaths = new MutableLiveData<>();
        recovered = new MutableLiveData<>();
        currentCountry = new MutableLiveData<>();
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

    public void setCountryNames(){
        if(countryNames.size() == 0) {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<List<Country>> countryCall = apiService.getAllCountries();
            countryCall.enqueue(new Callback<List<Country>>() {
                @Override
                public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                    for (Country c : response.body()) {
                        countryNames.add(c.getName());
                    }
                }

                @Override
                public void onFailure(Call<List<Country>> call, Throwable t) {

                }
            });
        }

    }

    public MutableLiveData<Country> getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String country){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Country> countryCall = apiService.getCountry(country);
        countryCall.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                currentCountry.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {

            }
        });
    }

    public List<String> getCountryNames() {
        return countryNames;
    }

    public MutableLiveData<String> getCases() {
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