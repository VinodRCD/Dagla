package com.dagla.android.parser;

public class SortModel {

    private String id;   //Display data
    private String name;   //Display data
    private String sortLetters;  //Data display Pinyin initials

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
