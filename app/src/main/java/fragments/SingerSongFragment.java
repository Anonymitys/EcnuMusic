package fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SingerSongRecyclerAdapter;
import classcollection.Song;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SingerDetailActivity;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;
import service.MusicService;
import shouyeclass.singersong.Musics;

public class SingerSongFragment extends BaseFragment implements SingerSongRecyclerAdapter.OnItemClickListener {
    private XRecyclerView recyclerView;
    private static final String TAG = "SingerSongFragment";
    private MusicService.MusicBinder musicBinder;

    @Override
    protected int setContentId() {
        return R.layout.singer_detail;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.singer_recycler);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.clearFooter();
        View view1=View.inflate(getContext(),R.layout.footer_space,null);
        recyclerView.addFootView(view1,true);
        recyclerView.noMoreLoading();
    }

    @Override
    protected void initData() {
        initRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        musicBinder = ((SingerDetailActivity) getActivity()).getMusicBinder();
    }

    private void initRequest() {
        MusicRequestUtil.getSingerSong(mContext, ((SingerDetailActivity) getActivity()).getSingerMid(), new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText = response.body().string();
                List<Musics> musicsList = Utility.handleSingerSongResponse(responseText);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(manager);
                SingerSongRecyclerAdapter adapter = new SingerSongRecyclerAdapter(musicsList);
                adapter.setOnItemClickListener(SingerSongFragment.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(mContext, "singersong error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: ");
            }
        });
    }

    @Override
    public void onItemClick(int position, List<Song> musics) {
        musicBinder.playFromvkey(position, musics);
    }
}
