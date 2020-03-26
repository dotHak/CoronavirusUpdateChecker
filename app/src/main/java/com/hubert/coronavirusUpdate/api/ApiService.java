package com.hubert.coronavirusUpdate.api;

import com.hubert.coronavirusUpdate.model.Country;
import com.hubert.coronavirusUpdate.model.Total;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("all")
    Observable<Total> getAllTotal();

    @GET("countries/{countryName}")
    Observable<Country> getCountry(@Path("countryName") String countryName);

    @GET("countries")
    Call<List<Country>> getAllCountries();

}
