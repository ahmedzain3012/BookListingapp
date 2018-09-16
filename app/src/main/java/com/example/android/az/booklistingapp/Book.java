package com.example.android.az.booklistingapp;

public class Book {
    private String mAuthors;
    private String mTitle;
    private String mUrl;


    public Book(String mAuthors, String mTitle,String mUrl) {
        this.mAuthors = mAuthors;
        this.mTitle = mTitle;
        this.mUrl = mUrl;
    }

    public String getmAuthors() {
        return mAuthors;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }
}
