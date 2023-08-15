package com.dagla.android.parser;

import java.util.ArrayList;

public class HomeBannerDetails {

    private String banner_id;
    private String home_banner_cat_id;
    private String home_banner_cat_name;
    private String banner_cat_id;
    private String banner_sub_cat_id;
    private String banner_product_id;
    private String banner_brand_id;
    private String image;

    ArrayList<HomeCateDetails> homeCateProductsDetailsArrayList;

    public HomeBannerDetails(String banner_id, String home_banner_cat_id, String home_banner_cat_name, String banner_cat_id,
                             String banner_sub_cat_id, String banner_product_id, String banner_brand_id,
                             String image, ArrayList<HomeCateDetails> homeCateProductsDetailsArrayList) {

        this.banner_id = banner_id;
        this.home_banner_cat_id = home_banner_cat_id;
        this.home_banner_cat_name = home_banner_cat_name;
        this.banner_cat_id = banner_cat_id;
        this.banner_sub_cat_id = banner_sub_cat_id;
        this.banner_product_id = banner_product_id;
        this.banner_brand_id = banner_brand_id;
        this.image = image;

        this.homeCateProductsDetailsArrayList = homeCateProductsDetailsArrayList;

    }

    public HomeBannerDetails(String banner_id, String home_banner_cat_id, String home_banner_cat_name, String banner_cat_id,
                             String banner_sub_cat_id, String banner_product_id, String banner_brand_id,
                             String image) {

        this.banner_id = banner_id;
        this.home_banner_cat_id = home_banner_cat_id;
        this.home_banner_cat_name = home_banner_cat_name;
        this.banner_cat_id = banner_cat_id;
        this.banner_sub_cat_id = banner_sub_cat_id;
        this.banner_product_id = banner_product_id;
        this.banner_brand_id = banner_brand_id;
        this.image = image;

    }





    public String getBannerId() {
        return banner_id;
    }

    public void setBannerId(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getHomeBannerCatId() {
        return home_banner_cat_id;
    }

    public void setHomeBannerCatId(String home_banner_cat_id) {
        this.home_banner_cat_id = home_banner_cat_id;
    }


    public String getHomeBannerCatName() {
        return home_banner_cat_name;
    }

    public void setHomeBannerCatName(String home_banner_cat_name) {
        this.home_banner_cat_name = home_banner_cat_name;
    }

    public String getBannerCatId() {
        return banner_cat_id;
    }

    public void setBannerCatId(String banner_cat_id) {
        this.banner_cat_id = banner_cat_id;
    }

    public String getBannerSubCatId() {
        return banner_sub_cat_id;
    }

    public void setBannerSubCatId(String banner_sub_cat_id) {
        this.banner_sub_cat_id = banner_sub_cat_id;
    }

    public String getBannerProductId() {
        return banner_product_id;
    }

    public void setBannerProductId(String banner_product_id) {
        this.banner_product_id = banner_product_id;
    }

    public String getBannerBrandId() {
        return banner_brand_id;
    }

    public void setBannerBrandId(String banner_brand_id) {
        this.banner_brand_id = banner_brand_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public ArrayList<HomeCateDetails> getHomeCateProducts() {
        return homeCateProductsDetailsArrayList;
    }

//    public void setImage(String image) {
//        this.image = image;
//    }


}
