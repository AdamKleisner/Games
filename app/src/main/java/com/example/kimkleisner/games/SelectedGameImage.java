package com.example.kimkleisner.games;


import java.util.ArrayList;

public class SelectedGameImage {

    ArrayList<String> url;
    String developer;
    String publisher;

    public SelectedGameImage (ArrayList<String> _url, String _publisher, String _developer){
        url = _url;
        developer = _developer;
        publisher = _publisher;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

}
