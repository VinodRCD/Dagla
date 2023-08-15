package com.dagla.android.parser;

public class FilterColorsDetails {

    private String color_id;
    private String color_code;
    private String color_name;
    private boolean selected;

    public FilterColorsDetails(String color_id, String color_code, String color_name) {

        this.color_id = color_id;
        this.color_code = color_code;
        this.color_name = color_name;

    }


    public String getColorId() {
        return color_id;
    }

    public void setColorId(String color_id) {
        this.color_id = color_id;
    }

    public String getColorCode() {
        return color_code;
    }

    public void setColorCode(String color_code) {
        this.color_code = color_code;
    }


    public String getColorName() {
        return color_name;
    }

    public void setColorName(String color_name) {
        this.color_name = color_name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
