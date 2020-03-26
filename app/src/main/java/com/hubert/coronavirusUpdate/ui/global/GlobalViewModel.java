package com.hubert.coronavirusUpdate.ui.global;

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
import com.hubert.coronavirusUpdate.model.Total;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class GlobalViewModel extends AndroidViewModel {

    private MutableLiveData<String> cases;
    private MutableLiveData<String> deaths;
    private MutableLiveData<String> recovered;
    private Set<String> countryNames;
    private MutableLiveData<Country> currentCountry;
    private SharedPreferences sharedPreferences;
    private Context context;
    private Total oldTotal;
    private Country oldCountry;



    public GlobalViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), MODE_PRIVATE);

        boolean countryNamesNeedUpdate = sharedPreferences.getBoolean(
                MainActivity.currentListNeedUpdate, true);
        if(countryNamesNeedUpdate){
            setCountryNames();
            sharedPreferences.edit()
                    .putBoolean(MainActivity.currentListNeedUpdate, false).apply();
        }
        setCountryNamesSet();

        cases = new MutableLiveData<>();
        deaths = new MutableLiveData<>();
        recovered = new MutableLiveData<>();
        currentCountry = new MutableLiveData<>();

        setOldTotal();
        setOldCountry();
        setCurrentCountry();
        setData();
    }

    private void setOldTotal(){
        int sCases = sharedPreferences.getInt(MainActivity.globalCases, 0);
        int sDeaths = sharedPreferences.getInt(MainActivity.globalDeaths, 0);
        int sRecovered = sharedPreferences.getInt(MainActivity.globalRecovered, 0);
        if(oldTotal == null) {
            oldTotal = new Total();
        }
        oldTotal.setCases(sCases);
        oldTotal.setDeaths(sDeaths);
        oldTotal.setRecovered(sRecovered);
    }

    private void setOldCountry(){
        String name = sharedPreferences.getString(MainActivity.currentCountryName, "Ghana");
        int cases = sharedPreferences.getInt(MainActivity.currentCountryCases, 0);
        int todayCases = sharedPreferences.getInt(MainActivity.currentCountryTodayCases, 0);
        int deaths = sharedPreferences.getInt(MainActivity.currentCountryDeaths, 0);
        int todayDeaths = sharedPreferences.getInt(MainActivity.currentCountryTodayDeaths, 0);
        int active = sharedPreferences.getInt(MainActivity.currentCountryActive, 0);
        int recovered = sharedPreferences.getInt(MainActivity.currentCountryRecovered, 0);
        int critical = sharedPreferences.getInt(MainActivity.currentCountryCritical, 0);

        if(oldCountry == null){
            oldCountry = new Country();
        }

        oldCountry.setName(name);
        oldCountry.setActive(active);
        oldCountry.setCases(cases);
        oldCountry.setCritical(critical);
        oldCountry.setDeaths(deaths);
        oldCountry.setRecovered(recovered);
        oldCountry.setTodayDeaths(todayDeaths);
        oldCountry.setTodayCases(todayCases);
    }

    private void updateOldCountry(Country country){
        sharedPreferences.edit()
                .putInt(MainActivity.currentCountryCritical, country.getCritical())
                .putInt(MainActivity.currentCountryCases, country.getCases())
                .putInt(MainActivity.currentCountryTodayCases, country.getTodayCases())
                .putInt(MainActivity.currentCountryTodayDeaths, country.getTodayDeaths())
                .putInt(MainActivity.currentCountryDeaths, country.getDeaths())
                .putInt(MainActivity.currentCountryRecovered, country.getRecovered())
                .putInt(MainActivity.currentCountryActive, country.getActive())
                .putString(MainActivity.currentCountryName, country.getName())
                .apply();
    }

    private void updateOldTotal(Total total){
        sharedPreferences.edit()
                .putInt(MainActivity.globalDeaths, total.getDeaths())
                .putInt(MainActivity.globalCases, total.getCases())
                .putInt(MainActivity.globalRecovered, total.getRecovered())
                .apply();
    }

    private void setCountryNamesSet(){
        countryNames = sharedPreferences.getStringSet(
                MainActivity.allCountryNames, new HashSet<>());
    }

    private Set<String> getCountryNameSet(){
        return sharedPreferences.getStringSet(
                MainActivity.allCountryNames, new HashSet<>());
    }

    private void updateCountryNames(Set<String> names){
        sharedPreferences.edit()
                .putStringSet(MainActivity.allCountryNames, names).apply();
    }

    private void updateData(){
        cases.setValue(NumberFormat.getInstance().format(oldTotal.getCases()));
        deaths.setValue(NumberFormat.getInstance().format(oldTotal.getDeaths()));
        recovered.setValue(NumberFormat.getInstance().format(oldTotal.getRecovered()));
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
                            updateOldTotal(total);
                        }
                        updateData();

                    }

                    @Override
                    public void onError(Throwable e) {
                        setOldTotal();
                        updateData();
                        showError();
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    private void setCountryNames(){
        Set<String> names = new HashSet<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Country>> countryCall = apiService.getAllCountries();
        countryCall.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                for (Country c : response.body()) {
                    names.add(c.getName());
                }
                if(names.size() != getCountryNameSet().size()){
                    countryNames = names;
                    updateCountryNames(names);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                setCountryNamesSet();
                showError();
            }
        });

    }

    MutableLiveData<Country> getCurrentCountry() {
        currentCountrySingle();
        return currentCountry;
    }

    private void currentCountrySingle(){
        String country = sharedPreferences
                .getString(MainActivity.newCurrentCountryName,"Ghana");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<Country> countryCall = apiService.getCountrySingleCall(country);
        countryCall.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                oldCountry = response.body();
                updateOldCountry(response.body());
                currentCountry.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                showError();
            }
        });
    }

    private void setCurrentCountry(){
        String country = sharedPreferences
                .getString(MainActivity.currentCountryName,"Ghana");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Observable<Country> countryObservable = apiService.getCountry(country);
        countryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(completed -> completed.delay(90, TimeUnit.SECONDS))
                .subscribe(new Observer<Country>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Country country) {
                        if(!country.equals(oldCountry)){
                            oldCountry = country;
                            updateOldCountry(country);
                        }
                        currentCountry.setValue(oldCountry);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setOldCountry();
                        currentCountry.setValue(oldCountry);
                        showError();
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

    Set<String> getCountryNames() {
        return countryNames;
    }

    MutableLiveData<String> getCases() {
        return cases;
    }

    MutableLiveData<String> getDeaths() {
        return deaths;
    }

    MutableLiveData<String> getRecovered() {
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