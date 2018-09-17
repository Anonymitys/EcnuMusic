package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SingerAlbumRecyclerAdapter;
import adapter.SingerSongRecyclerAdapter;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SingerDetailActivity;
import ecnu.ecnumusic.SongListDetailActivity;
import okhttp3.Request;
import okhttp3.Response;
import service.MusicService;
import shouyeclass.Album;
import shouyeclass.singersong.Musics;

public class SingerAlbumFragment extends BaseFragment implements SingerAlbumRecyclerAdapter.OnItemClickListener{
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



    private void initRequest(){
        Log.e(TAG, "initData: " );
        MusicRequestUtil.getSingerAlbum(mContext, ((SingerDetailActivity) getActivity()).getSingerMid(), new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                List<Album> albumList=Utility.handleSingerAlbumResponse(responseText);
                Log.e(TAG, "onResponse: "+albumList.get(0).album_name );
                LinearLayoutManager manager=new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(manager);
                SingerAlbumRecyclerAdapter adapter=new SingerAlbumRecyclerAdapter(albumList);
                adapter.setOnItemClickListener(SingerAlbumFragment.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {

            }
        });
    }

    @Override
    public void onItemClick(ImageView imageView,Album album) {
        Bundle bundle=new Bundle();
        bundle.putString("tag","albumlist");
        bundle.putSerializable("album",album);

        Intent intent=new Intent(getContext(), SongListDetailActivity.class);
        intent.putExtra("song",bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),imageView,"sharedView");//与xml文件对应
        startActivity(intent,options.toBundle());
    }
}
