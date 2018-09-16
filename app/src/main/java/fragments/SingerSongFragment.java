package fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.OkHttpEngine;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SingerSongRecyclerAdapter;
import classcollection.Song;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SingerDetailActivity;
import jiekou.FragmentEntrust;
import okhttp3.Request;
import okhttp3.Response;
import service.MusicService;
import shouyeclass.singersong.Musics;

public class SingerSongFragment extends BaseFragment implements SingerSongRecyclerAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private static final String TAG="SingerSongFragment";
    private MusicService.MusicBinder musicBinder;
    @Override
    protected int setContentId() {
        return R.layout.singer_detail;
    }

    @Override
    protected void initView(View view) {
        recyclerView=(RecyclerView)view.findViewById(R.id.singer_recycler);

    }

    @Override
    protected void initData() {

        initRequest();

    }

    @Override
    public void onResume() {
        super.onResume();
        musicBinder=((SingerDetailActivity)getActivity()).getMusicBinder();
    }

    private void initRequest(){
        Log.e(TAG, "initData: " );
        MusicRequestUtil.getSingerSong(mContext, ((SingerDetailActivity) getActivity()).getSingerMid(), new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                Log.e(TAG, "onResponse: "+responseText );
                List<Musics> musicsList= Utility.handleSingerSongResponse(responseText);
                Log.e(TAG, "onResponse: "+musicsList.get(0).musciData.songname );
                LinearLayoutManager manager=new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(manager);
                SingerSongRecyclerAdapter adapter=new SingerSongRecyclerAdapter(musicsList);
                adapter.setOnItemClickListener(SingerSongFragment.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(mContext,"singersong error",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: " );
            }
        });
    }

    @Override
    public void onItemClick(int position, List<Song> musics) {
        musicBinder.playFromURL(position,musics);
    }
}
