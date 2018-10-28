package com.example.jinny.mymusic.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MusicService {
    @GET("api")
    Call<Subgenres> getListMusicType();

    @GET("https://itunes.apple.com/us/rss/topsongs/limit=50/genre={idMusicType}/explicit=true/json")
    Call<TopSongResponse> getTopSong(@Path("idMusicType") String idMusicType);

    @GET("http://services.techkids.vn/api/audio")
    Call<SearchSongResponse> getSearchSong(@Query("search_terms") String query);

    @GET("xml")
    Call<LocationResponse> getLocation(@Query("key1") String key);
} 