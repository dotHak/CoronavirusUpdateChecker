package com.hubert.coronavirusUpdate.model;

import com.google.gson.annotations.SerializedName;

public class CountryInfo {
    @SerializedName("iso2")
    private String iso;
    @SerializedName("flag")
    private String flag;

    public CountryInfo(String iso, String flag) {
        this.iso = iso;
        this.flag = flag;
    }

    public CountryInfo() {
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
