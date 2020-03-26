package com.hubert.coronavirusUpdate.ui.search;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hubert.coronavirusUpdate.MainActivity;
import com.hubert.coronavirusUpdate.R;
import com.hubert.coronavirusUpdate.api.ApiClient;
import com.hubert.coronavirusUpdate.api.ApiService;
import com.hubert.coronavirusUpdate.model.Country;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class SearchViewModel extends AndroidViewModel {

    private MutableLiveData<List<Country>> countryList;
    private Context context;

    public SearchViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        countryList = new MutableLiveData<>();
        setData();
    }

    MutableLiveData<List<Country>> getCountryList() {
        return countryList;
    }

    private void setData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Observable<List<Country>> listObservable = apiService.getAllCountriesObservable();
        listObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(4, TimeUnit.MINUTES))
                .subscribe(new Observer<List<Country>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Country> cList) {
                        if(!(cList == null)){
                            countryList.setValue(cList);
                            updateCountryNames(cList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateCountryNames(List<Country> countryList){
       SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), MODE_PRIVATE);
       Set<String> countryNames = sharedPreferences.getStringSet(
               MainActivity.allCountryNames, new HashSet<>());
       if(countryList != null){
           if(countryList.size() != countryList.size()){
               Set<String> names = new HashSet<>();
               for(Country country: countryList){
                   names.add(country.getName());
               }
               sharedPreferences.edit()
                       .putStringSet(MainActivity.allCountryNames, names).apply();
           }
       }

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