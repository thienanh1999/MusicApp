package com.example.jinny.mymusic.events;

    import com.example.jinny.mymusic.Databases.TopSongModel;

public class OnClickTopSong {
    public TopSongModel topSongModel;

    public OnClickTopSong(TopSongModel topSongModel) {
        this.topSongModel = topSongModel;
    }
}