package com.hubert.coronavirusUpdate.model;

import com.google.gson.annotations.SerializedName;

public class Total {

    @SerializedName("cases")
    private int cases;

    @SerializedName("deaths")
    private int deaths;

    @SerializedName("recovered")
    private int recovered;

    public Total() {
    }

    public Total(int cases, int deaths, int recovered) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    @Override
    public String toString() {
        return "Total{" +
                "cases=" + cases +
                ", deaths=" + deaths +
                ", recovered=" + recovered +
                '}';
    }

}
