package com.mogal.classes;

import java.util.ArrayList;

public class Section<T> {
    private String title;
    private ArrayList<T> arrayList;

    public Section(String title, ArrayList<T> arrayList){
        this.title = title;
        this.arrayList = arrayList;
    }

    public Section(String title){
        this.title = title;
        this.arrayList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<T> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<T> arrayList) {
        this.arrayList = arrayList;
    }
}
