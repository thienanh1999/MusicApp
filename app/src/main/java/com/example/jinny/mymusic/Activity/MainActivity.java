package com.example.jinny.mymusic.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jinny.mymusic.Adapter.ViewPagerAdapter;
import com.example.jinny.mymusic.Databases.TopSongModel;
import com.example.jinny.mymusic.Fragment.MainPlayerFragment;
import com.example.jinny.mymusic.R;
import com.example.jinny.mymusic.Utils.MusicHandle;
import com.example.jinny.mymusic.Utils.Utils;
import com.example.jinny.mymusic.events.OnClickTopSong;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_PERMISSON = 1;

    @BindView(R.id.tl_main)
    TabLayout tlMain;
    @BindView(R.id.vp_pager)

    ViewPager vpPager;
    @BindView(R.id.sb_play)
    SeekBar sbPlay;
    @BindView(R.id.iv_top_song)
    ImageView ivTopSong;
    @BindView(R.id.tv_top_song)
    TextView tvTopSong;
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    @BindView(R.id.fb_pause)
    FloatingActionButton fbPause;
    @BindView(R.id.rl_mini)
    RelativeLayout rlMini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupPermission();

        tlMain.getTabAt(0).getIcon().setAlpha(255);
        tlMain.getTabAt(1).getIcon().setAlpha(100);
        tlMain.getTabAt(2).getIcon().setAlpha(100);

        Utils.getListDownloadSong();

        EventBus.getDefault().register(this);

        tlMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(255);
                vpPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(100);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(viewPagerAdapter);
        vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlMain));

        sbPlay.setPadding(0, 0, 0, 0);
    }

    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSON);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSON) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Without permisson you can not use this app. Do you want to grant permisson?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSON);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        })
                        .show();
            }
        }
    }

    @Subscribe(sticky = true)
    public void onReceivedTopSong(OnClickTopSong onClickTopSong) {
        TopSongModel topSongModel = onClickTopSong.topSongModel;

        boolean check = Utils.isDownloaded(topSongModel);

        rlMini.setVisibility(View.VISIBLE);
        tvTopSong.setText(topSongModel.song);
        tvArtist.setText(topSongModel.artist);
        if (check) Picasso.get().load(R.drawable.meow).transform(new CropCircleTransformation()).into(ivTopSong);
        else
            Picasso.get().load(topSongModel.image).transform(new CropCircleTransformation()).into(ivTopSong);

        if (!check) MusicHandle.getSearchSong(topSongModel, this);
        else
        {
            topSongModel.url = Utils.getSongUrl(topSongModel);
            MusicHandle.playDonwloaded(topSongModel, this);
        }
        MusicHandle.updateRealtimeUI(sbPlay, fbPause,null,null, ivTopSong);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        } else {
            moveTaskToBack(true);
        }
    }

    @OnClick({R.id.fb_pause, R.id.rl_mini})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fb_pause:
                MusicHandle.playPause();
                break;
            case R.id.rl_mini:
                Utils.openFragment(getSupportFragmentManager(), R.id.rl_main, new MainPlayerFragment());
                break;
        }
    }
}
