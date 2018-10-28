//Adapter -> viewHolder -> alt + enter adapter to import holder -> override adapter
package com.example.jinny.mymusic.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jinny.mymusic.Activity.MainActivity;
import com.example.jinny.mymusic.Databases.MusicTypeModel;
import com.example.jinny.mymusic.Fragment.TopSongFragment;
import com.example.jinny.mymusic.R;
import com.example.jinny.mymusic.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicTypeAdapter extends RecyclerView.Adapter<MusicTypeAdapter.MusicTypeViewHolder> {
    List<MusicTypeModel> musicTypeModels = new ArrayList<>();
    Context context;

    public MusicTypeAdapter(List<MusicTypeModel> musicTypeModels, Context context) {
        this.musicTypeModels = musicTypeModels;
        this.context = context;
    }

    //tao item view
    @Override
    public MusicTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemview = layoutInflater.inflate(R.layout.item_music_type,parent,false);
        return new MusicTypeViewHolder(itemview);
    }

    //load data
    @Override
    public void onBindViewHolder(MusicTypeViewHolder holder, int position) {
        holder.setData(musicTypeModels.get(position));
    }

    @Override
    public int getItemCount() {
        return musicTypeModels.size();
    }

    public class MusicTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_music_type)
        ImageView ivMusicType;
        @BindView(R.id.tv_music_type)
        TextView tvMusicType;

        public MusicTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        //load Data ntn?
        public void setData(final MusicTypeModel musicTypeModel) {
            ivMusicType.setImageResource(musicTypeModel.imageID);
            //lazy load
            Picasso.get().load(musicTypeModel.imageID).into(ivMusicType);
            tvMusicType.setText(musicTypeModel.name);

            ivMusicType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TopSongFragment topSongFragment = new TopSongFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("music_type_model", musicTypeModel);
                    topSongFragment.setArguments(bundle);

                    Utils.openFragment(((MainActivity) context).getSupportFragmentManager(),
                                        R.id.ll_main, topSongFragment);
                }
            });
        }
    }
} 