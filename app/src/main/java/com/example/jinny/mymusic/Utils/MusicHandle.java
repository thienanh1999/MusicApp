package com.example.jinny.mymusic.Utils;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jinny.mymusic.Databases.TopSongModel;
import com.example.jinny.mymusic.Network.LocationResponse;
import com.example.jinny.mymusic.Network.MusicService;
import com.example.jinny.mymusic.Network.RetrofitInstance;
import com.example.jinny.mymusic.Network.SearchSongResponse;
import com.example.jinny.mymusic.R;

import java.util.logging.Handler;

import hybridmediaplayer.HybridMediaPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicHandle {
    private static final String TAG = "MusicHandle";
    private static HybridMediaPlayer hybridMediaPlayer;
    private static boolean keepUpdate = true;

    public static void getSearchSong(TopSongModel topSongModel, final Context context) {
        MusicService musicService = RetrofitInstance.getRetrofitInstance().create(MusicService.class);
        musicService.getSearchSong(topSongModel.song + " " + topSongModel.artist).enqueue(new Callback<SearchSongResponse>() {
            @Override
            public void onResponse(Call<SearchSongResponse> call, Response<SearchSongResponse> response) {
                String url = response.body().data.url;
                getLocationSong(url, context);
            }

            @Override
            public void onFailure(Call<SearchSongResponse> call, Throwable t) {

            }
        });
    }

    public static void getLocationSong(String url, final Context context) {
        MusicService musicService = RetrofitInstance.getRetrofitXML().create(MusicService.class);
        musicService.getLocation(url.split("=")[1]).enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {

                if (hybridMediaPlayer != null) {
                    if (hybridMediaPlayer.isPlaying()) {
                        hybridMediaPlayer.pause();
                    }
                    hybridMediaPlayer.release();
                }

                hybridMediaPlayer = HybridMediaPlayer.getInstance(context);
                hybridMediaPlayer.setDataSource(response.body().trackXML.location.trim());
                hybridMediaPlayer.prepare();
                hybridMediaPlayer.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(HybridMediaPlayer hybridMediaPlayer) {
                        hybridMediaPlayer.play();
                    }
                });
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {

            }
        });
    }

    public static void playDonwloaded(TopSongModel topSongModel, Context context) {
        if (hybridMediaPlayer != null) {
            if (hybridMediaPlayer.isPlaying()) {
                hybridMediaPlayer.pause();
            }
            hybridMediaPlayer.release();
        }

        hybridMediaPlayer = HybridMediaPlayer.getInstance(context);
        hybridMediaPlayer.setDataSource(topSongModel.url);
        hybridMediaPlayer.prepare();
        hybridMediaPlayer.setOnPreparedListener(new HybridMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(HybridMediaPlayer hybridMediaPlayer) {
                hybridMediaPlayer.play();
            }
        });
    }

    public static void playPause() {
        if (hybridMediaPlayer != null) {
            if (hybridMediaPlayer.isPlaying())
            {
                hybridMediaPlayer.pause();
            } else {
                hybridMediaPlayer.play();
            }
        }
    }

    public static void updateRealtimeUI(final SeekBar seekBar, final FloatingActionButton floatingActionButton,
                                        final TextView tvCurrent, final TextView tvDuration, final ImageView imageView) {
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            //update
            if (hybridMediaPlayer != null && keepUpdate == true) {
                seekBar.setMax(hybridMediaPlayer.getDuration());
                seekBar.setProgress(hybridMediaPlayer.getCurrentPosition());

                if (hybridMediaPlayer.isPlaying()) {
                    floatingActionButton.setImageResource(R.drawable.exo_controls_pause);
                } else {
                    floatingActionButton.setImageResource(R.drawable.exo_controls_play);
                }

                if (tvCurrent != null) {
                    tvCurrent.setText(Utils.formatTime(hybridMediaPlayer.getCurrentPosition()));
                    tvDuration.setText(Utils.formatTime(hybridMediaPlayer.getDuration()));
                }

                Utils.rotateImage(imageView, hybridMediaPlayer.isPlaying());
            }
            //100ms run code
            handler.postDelayed(this, 100);
            }
        };
        runnable.run();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                keepUpdate = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hybridMediaPlayer.seekTo(seekBar.getProgress());
                keepUpdate = true;
            }
        });
    }
} 