package com.example.myapplication6.models;

public class CountryItem {
    private final String name;
    private final String flag;

    public CountryItem(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return flag + "  " + name;
    }
}