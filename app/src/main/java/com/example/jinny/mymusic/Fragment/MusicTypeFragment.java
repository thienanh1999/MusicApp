package com.example.jinny.mymusic.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinny.mymusic.Adapter.MusicTypeAdapter;
import com.example.jinny.mymusic.Databases.MusicTypeModel;
import com.example.jinny.mymusic.Network.MusicService;
import com.example.jinny.mymusic.Network.RetrofitInstance;
import com.example.jinny.mymusic.Network.Subgenres;
import com.example.jinny.mymusic.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicTypeFragment extends Fragment {
    private static final String TAG = "MusicTypeFragment";
    @BindView(R.id.rv_music_type)
    RecyclerView rvMusicType;
    Unbinder unbinder;

    MusicTypeAdapter musicTypeAdapter;
    List<MusicTypeModel> musicTypeModels = new ArrayList<>();
    Context context;

    public MusicTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_type, container, false);
        unbinder = ButterKnife.bind(this, view);

        musicTypeAdapter = new MusicTypeAdapter(musicTypeModels, getContext());
        rvMusicType.setAdapter(musicTypeAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                context,
                2,
                GridLayoutManager.VERTICAL,
                false
        );
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position % 3 == 0 ? 2 : 1;
                // if (position % 3 == 0) return 2; else return 1;
            }
        });
        rvMusicType.setLayoutManager(gridLayoutManager);

        context = getContext();

        loadData();

        return view;
    }

    private void loadData() {
        MusicService musicService = RetrofitInstance.getRetrofitInstance()
                .create(MusicService.class);
        musicService.getListMusicType().enqueue(new Callback<Subgenres>() {
            @Override
            public void onResponse(Call<Subgenres> call, Response<Subgenres> response) {
                Log.d(TAG, "onMusicTypeResponse" + response.body().subgenres.toString());

                List<Subgenres.MusicTypeJSON> musicTypeJSONs = response.body().subgenres;

                for (Subgenres.MusicTypeJSON musicTypeJSON : musicTypeJSONs) {
                    MusicTypeModel musicTypeModel = new MusicTypeModel(
                            musicTypeJSON.id,
                            musicTypeJSON.translation_key,
                            context.getResources().getIdentifier(
                                    "genre_x2_" + musicTypeJSON.id,
                                    "raw",
                                    context.getPackageName()
                            )
                    );
                    musicTypeModels.add(musicTypeModel);
                }
                musicTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Subgenres> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
