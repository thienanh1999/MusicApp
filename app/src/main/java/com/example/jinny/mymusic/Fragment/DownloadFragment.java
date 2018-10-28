package com.example.jinny.mymusic.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinny.mymusic.Adapter.DownloadedAdapter;
import com.example.jinny.mymusic.Databases.TopSongModel;
import com.example.jinny.mymusic.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    DownloadedAdapter downloadedAdapter;
    @BindView(R.id.rv_downloaded)
    RecyclerView rvDownloaded;
    Unbinder unbinder;

    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        unbinder = ButterKnife.bind(this, view);
        downloadedAdapter = new DownloadedAdapter();
        rvDownloaded.setAdapter(downloadedAdapter);
        rvDownloaded.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDownloaded.setItemAnimator(new SlideInRightAnimator());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
