package com.dagla.android.parser;

public class FilterSizesDetails {

    private String size_id;
    private String size_name;
    private boolean selected;

    public FilterSizesDetails(String size_id, String size_name) {

        this.size_id = size_id;
        this.size_name = size_name;

    }


    public String getSizeId() {
        return size_id;
    }

    public void setSizeId(String size_id) {
        this.size_id = size_id;
    }



    public String getSizeName() {
        return size_name;
    }

    public void setSizeName(String size_name) {
        this.size_name = size_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
