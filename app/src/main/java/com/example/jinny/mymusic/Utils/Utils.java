package com.example.jinny.mymusic.Utils;

import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.jinny.mymusic.Databases.TopSongModel;
import com.example.jinny.mymusic.Network.LocationResponse;
import com.example.jinny.mymusic.Network.MusicService;
import com.example.jinny.mymusic.Network.RetrofitInstance;
import com.example.jinny.mymusic.Network.SearchSongResponse;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {
    private static final String TAG = "Utils";
    public static String foldername = "MyMusic";
    public static Uri downloadUri;
    public static Uri destinationUri;
    public static ThinDownloadManager downloadManager;

    public static void openFragment(FragmentManager fragmentManager,
                                    int layoutID, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(layoutID, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static String formatTime(int time) {
        int min = time / 60000;
        int sec = (time % 60000) / 1000;
        return String.format("%02d:%02d", min, sec);
    }

    public static void rotateImage(ImageView imageView, boolean isPlaying) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(10000);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        if (isPlaying) {
            if (imageView.getAnimation() == null) {
                imageView.startAnimation(rotateAnimation);
            }
        } else {
            imageView.setAnimation(null);
        }
    }

    public static void Download(final TopSongModel topSongModel) {
        //1. Creat new folder to save music
        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root, foldername);
        folder.mkdirs();
        destinationUri = Uri.parse(folder.toString() +"/"+topSongModel.song + "-" + topSongModel.artist + ".mp4");

        //2. get Song Location
        final MusicService musicService = RetrofitInstance.getRetrofitInstance().create(MusicService.class);
        musicService.getSearchSong(topSongModel.song + " " + topSongModel.artist).enqueue(new Callback<SearchSongResponse>() {
            @Override
            public void onResponse(Call<SearchSongResponse> call, Response<SearchSongResponse> response) {
                String url = response.body().data.url;

                MusicService musicService1 = RetrofitInstance.getRetrofitXML().create(MusicService.class);
                musicService1.getLocation(url.split("=")[1]).enqueue(new Callback<LocationResponse>() {
                    @Override
                    public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                        topSongModel.url = destinationUri.toString();
                        downloadUri = Uri.parse(response.body().trackXML.location.trim());
                        SaveMusic();
                    }

                    @Override
                    public void onFailure(Call<LocationResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<SearchSongResponse> call, Throwable t) {

            }
        });
    }

    public static void SaveMusic() {
        Log.d(TAG, "onDownloadStart: ");
        Log.d(TAG, "onDownloadDes: " + destinationUri.toString());
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .addCustomHeader("Auth-Token", "YourTokenApiKey")
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        Log.d(TAG, "onDownloadComplete: " + id);
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Log.d(TAG, "onDownloadFailed: " + errorMessage);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
                    }
                });
        downloadManager = new ThinDownloadManager();
        downloadManager.add(downloadRequest);
    }

    public static List<TopSongModel> getListDownloadSong()
    {
        List<TopSongModel> topSongModels = new ArrayList<>();

        File folder = new File(Environment.getExternalStorageDirectory().toString(), foldername);
        File[] listSong = folder.listFiles();

        if (listSong != null) {
            for (int i = 0; i < listSong.length; i++) {
                String path = listSong[i].getAbsolutePath();
                String name = listSong[i].getName();
                String song = name.split("-")[0];
                String artist = name.split("-")[1];
                artist = artist.substring(0,artist.length()-4);

                TopSongModel topSongModel = new TopSongModel(path,null, song, artist);
                topSongModels.add(topSongModel);
            }
        }
        return topSongModels;
    }

    public static boolean isDownloaded(TopSongModel topSongModel) {
        List<TopSongModel> topSongModels = getListDownloadSong();
        for (int i = 0; i < topSongModels.size(); i++)
        {
            if (topSongModel.song.equals(topSongModels.get(i).song) && topSongModel.artist.equals(topSongModels.get(i).artist))
                return true;
        }
        return false;
    }

    public static String getSongUrl(TopSongModel topSongModel)
    {
        List<TopSongModel> topSongModels = getListDownloadSong();
        for (int i = 0; i < topSongModels.size(); i++)
        {
            if (topSongModel.song.equals(topSongModels.get(i).song) && topSongModel.artist.equals(topSongModels.get(i).artist))
                return topSongModels.get(i).url;
        }
        return null;
    }
} 