package com.example.jinny.mymusic.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jinny.mymusic.Databases.TopSongModel;
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
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPlayerFragment extends Fragment {
    private static final String TAG = "MainPlayerFragment";

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_song)
    TextView tvSong;
    @BindView(R.id.tv_artist)
    TextView tvArtist;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    Unbinder unbinder;

    TopSongModel topSongModel;
    @BindView(R.id.sb_play)
    SeekBar sbPlay;
    @BindView(R.id.fb_play)
    FloatingActionButton fbPlay;

    public MainPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_player, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        sbPlay.setPadding(0,16,0,0);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.iv_download, R.id.iv_previous, R.id.iv_next, R.id.fb_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_download:
                if (!Utils.isDownloaded(topSongModel))
                {
                    Utils.Download(topSongModel);
                    ivDownload.setAlpha(255);
                }
                break;
            case R.id.iv_previous:
                break;
            case R.id.iv_next:
                break;
            case R.id.fb_play:
                MusicHandle.playPause();
                break;
        }
    }

    @Subscribe(sticky = true)
    public void onReceivedTopSong(OnClickTopSong onClickTopSong) {
        topSongModel = onClickTopSong.topSongModel;

        boolean check = Utils.isDownloaded(topSongModel);
        if (check) ivDownload.setAlpha(255);
        else ivDownload.setAlpha(100);
        tvSong.setText(topSongModel.song);
        tvArtist.setText(topSongModel.artist);
        if (check) Picasso.get().load(R.drawable.meow).transform(new CropCircleTransformation()).into(ivImage);
        else
            Picasso.get().load(topSongModel.image).transform(new CropCircleTransformation()).into(ivImage);

        MusicHandle.updateRealtimeUI(sbPlay, fbPlay, tvCurrentTime, tvDuration,ivImage);
    }
}
