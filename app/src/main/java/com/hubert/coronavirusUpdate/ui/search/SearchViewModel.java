package com.hubert.coronavirusUpdate.ui.search;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hubert.coronavirusUpdate.api.ApiClient;
import com.hubert.coronavirusUpdate.api.ApiService;
import com.hubert.coronavirusUpdate.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private MutableLiveData<List<Country>> countryList;
    private Context context;

    public SearchViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        countryList = new MutableLiveData<>();
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
                showError();
            }
        });
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void showError(){
        if (haveNetworkConnection()) {
            Toast.makeText(context, "Oops an error occurred!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Please check your internet connectivity!",
                    Toast.LENGTH_SHORT).show();
        }
    }

}