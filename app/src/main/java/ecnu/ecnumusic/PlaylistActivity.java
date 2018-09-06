package ecnu.ecnumusic;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.MusicUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.PlaylistAdapter;
import okhttp3.Request;
import okhttp3.Response;
import shouyeclass.PlayList;

public class PlaylistActivity extends AppCompatActivity implements PlaylistAdapter.OnItemClickListener{

    private static final String TAG="PlaylistActivity";
    private RecyclerView playlistRecycler;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
         playlistRecycler=(RecyclerView)findViewById(R.id.playlist_recycler);
         playlistRecycler.setNestedScrollingEnabled(false);
         toolbar=(Toolbar)findViewById(R.id.playlist_toolbar);
         initToolBar();
        GridLayoutManager manager=new GridLayoutManager(PlaylistActivity.this,2);
        playlistRecycler.setLayoutManager(manager);
        getPlaylistRequest();
    }


    private void getPlaylistRequest(){
        MusicRequestUtil.getPlayList(this, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String text=response.body().string();
                List<PlayList> playLists= Utility.handlePlayListResponse(text);
                PlaylistAdapter adapter=new PlaylistAdapter(playLists);
                adapter.setOnItemClicklistener(PlaylistActivity.this);
                playlistRecycler.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(PlaylistActivity.this,"playlist failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(ImageView imageView, PlayList playList) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("playlist",playList);
        bundle.putString("tag","playlist");
        Intent intent=new Intent(this,SongListDetailActivity.class);
        intent.putExtra("song",bundle);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,imageView,"sharedView");//与xml文件对应
        startActivity(intent,options.toBundle());
    }
    private void initToolBar(){
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("歌单");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
