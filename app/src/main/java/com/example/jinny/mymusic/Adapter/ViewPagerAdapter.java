package com.example.jinny.mymusic.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jinny.mymusic.Fragment.DownloadFragment;
import com.example.jinny.mymusic.Fragment.FavouriteFragment;
import com.example.jinny.mymusic.Fragment.MusicTypeFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new MusicTypeFragment();
            case 1: return new FavouriteFragment();
            case 2: return new DownloadFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}