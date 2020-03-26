package com.hubert.coronavirusUpdate.ui.global;

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
import com.hubert.coronavirusUpdate.model.Total;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GlobalViewModel extends AndroidViewModel {

    private MutableLiveData<String> cases;
    private MutableLiveData<String> deaths;
    private MutableLiveData<String> recovered;
    private static List<String> countryNames;
    private MutableLiveData<Country> currentCountry;
    private Context context;
    private Total oldTotal;
    private Country oldCountry;



    public GlobalViewModel(Application application) {
        super(application);
        countryNames = new ArrayList<>();
        setCountryNames();
        context = application.getApplicationContext();
        cases = new MutableLiveData<>();
        deaths = new MutableLiveData<>();
        recovered = new MutableLiveData<>();
        currentCountry = new MutableLiveData<>();

        setData();
    }

    private void setData(){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Observable<Total> countryObservable = apiService.getAllTotal();
        countryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(1, TimeUnit.MINUTES))
                .subscribe(new Observer<Total>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Total total) {
                        if(!total.equals(oldTotal)){
                            oldTotal = total;
                        }
                        cases.setValue(NumberFormat.getInstance().format(oldTotal.getCases()));
                        deaths.setValue(NumberFormat.getInstance().format(oldTotal.getDeaths()));
                        recovered.setValue(NumberFormat.getInstance().format(oldTotal.getRecovered()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                        if(!(oldTotal == null)){
                            cases.setValue(NumberFormat.getInstance().format(oldTotal.getCases()));
                            deaths.setValue(NumberFormat.getInstance().format(oldTotal.getDeaths()));
                            recovered.setValue(NumberFormat.getInstance().format(oldTotal.getRecovered()));
                        }
                    }

                    @Override
                    public void onComplete() {
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
                    showError();
                }
            });
        }

    }

    public MutableLiveData<Country> getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String country){
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Observable<Country> countryObservable = apiService.getCountry(country);
        countryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(1, TimeUnit.MINUTES))
                .subscribe(new Observer<Country>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Country country) {
                        if(!country.equals(oldCountry)){
                            oldCountry = country;
                        }
                        currentCountry.setValue(oldCountry);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                        if(!(oldCountry==null)){
                            currentCountry.setValue(oldCountry);
                        }
                    }

                    @Override
                    public void onComplete() {

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