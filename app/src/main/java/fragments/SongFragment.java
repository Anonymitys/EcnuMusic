package fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import classcollection.Music;
import Utils.MusicUtil;
import adapter.LocalMusicRecyclerViewAdapter;
import ecnu.ecnumusic.R;

public class SongFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public static List<Music> mLocalMusics;
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.song,container,false);
       mRecyclerView=(RecyclerView)view.findViewById(R.id.song_recyclerview);
        mLocalMusics= MusicUtil.getMusic(container.getContext());
        mediaPlayer=new MediaPlayer();
       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new LocalMusicRecyclerViewAdapter(mLocalMusics,mediaPlayer));



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
