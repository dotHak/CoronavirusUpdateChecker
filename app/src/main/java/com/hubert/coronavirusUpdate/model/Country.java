package com.hubert.coronavirusUpdate.model;

import com.google.gson.annotations.SerializedName;

public class Country {

    @SerializedName("country")
    private String name;

    @SerializedName("cases")
    private int cases;

    @SerializedName("todayCases")
    private int todayCases;

    @SerializedName("deaths")
    private int deaths;

    @SerializedName("todayDeaths")
    private int todayDeaths;

    @SerializedName("recovered")
    private int recovered;

    @SerializedName("active")
    private int active;

    @SerializedName("critical")
    private int critical;

    @SerializedName("countryInfo")
    private CountryInfo countryInfo;

    public Country() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(int todayCases) {
        this.todayCases = todayCases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }
}
