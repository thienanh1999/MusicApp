package com.example.jinny.mymusic.Databases;

public class TopSongModel {
    public String url;
    public String image;
    public String song;
    public String artist;

    public TopSongModel(String url, String image, String song, String artist) {
        this.url = url;
        this.image = image;
        this.song = song;
        this.artist = artist;
    }
}