package com.dagla.android.parser;

/**
 * Created by sys on 07-Jun-18.
 */

public class CountriesDetails {

    private String country_id;
    private String country_code;
    private String country_name;
    private String country_name_ar;
    private String country_currency;
    private String country_flag;
    private boolean selected;

    public CountriesDetails(String country_id, String country_code, String country_name,String country_name_ar, String country_currency, String country_flag) {

        this.country_id = country_id;
        this.country_code = country_code;
        this.country_name = country_name;
        this.country_name_ar = country_name_ar;
        this.country_currency = country_currency;
        this.country_flag = country_flag;

    }

    public String getCountryId() {
        return country_id;
    }

    public void setCountryId(String country_id) {
        this.country_id = country_id;
    }

    public String getCountryCode() {
        return country_code;
    }

    public void setCountryCode(String country_code) {
        this.country_code = country_code;
    }

    public String getCountryName() {
        return country_name;
    }

    public void setCountryName(String country_name) {
        this.country_name = country_name;
    }


    public String getCountryNameAr() {
        return country_name_ar;
    }

    public void setCountryNameAr(String country_name_ar) {
        this.country_name_ar = country_name_ar;
    }


    public String getCountryCurrency() {
        return country_currency;
    }

    public void setCountryCurrency(String country_currency) {
        this.country_currency = country_currency;
    }

    public String getCountryFlag() {
        return country_flag;
    }

    public void setCountryFlag(String country_flag) {
        this.country_flag = country_flag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
