package com.example.rgome.bikestripapp.AppContent;

//Class to get all info related the trip
public class ImageUpload {

    public String locStart;
    public String locFinish;
    public String date;
    public String title;
    public String description;
    public String url;

    public String getLocStart(){
        return locStart;
    }
    public String getLocFinish(){
        return locFinish;
    }
    public String getDate(){
        return date;
    }
    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getUrl(){
        return url;
    }

    public ImageUpload(String locStart, String locFinish, String date, String title, String description, String url){
        this.locStart = locStart;
        this.locFinish = locFinish;
        this.date = date;
        this.title = title;
        this.description = description;
        this.url = url;
    }
}
