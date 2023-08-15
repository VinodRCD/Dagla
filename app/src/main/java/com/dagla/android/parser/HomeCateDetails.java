package com.dagla.android.parser;


public class HomeCateDetails {

    private String cate_id;
    private String cate_name;

    private String image;
    
    public HomeCateDetails(String cate_id, String cate_name) {

        this.cate_id = cate_id;
        this.cate_name = cate_name;



    }



    public String getCateId() {
        return cate_id;
    }

    public void setCateId(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getCateName() {
        return cate_name;
    }

    public void setCateName(String cate_name) {
        this.cate_name = cate_name;
    }


}
