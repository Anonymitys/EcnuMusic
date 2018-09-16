package ecnu.ecnumusic;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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
import fragments.DayRecommnedFragment;
import jiekou.FragmentEntrust;
import okhttp3.Request;
import okhttp3.Response;
import shouyeclass.PlayList;

public class PlaylistFragment extends Fragment implements PlaylistAdapter.OnItemClickListener{

    public static final String TAG="PlaylistFragment";
    private RecyclerView playlistRecycler;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.activity_playlist,container,false);
        playlistRecycler=(RecyclerView)view.findViewById(R.id.playlist_recycler);
        playlistRecycler.setNestedScrollingEnabled(false);
        toolbar=(Toolbar)view.findViewById(R.id.playlist_toolbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolBar();
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        playlistRecycler.setLayoutManager(manager);
        getPlaylistRequest();
    }

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
         playlistRecycler=(RecyclerView)findViewById(R.id.playlist_recycler);
         playlistRecycler.setNestedScrollingEnabled(false);
         toolbar=(Toolbar)findViewById(R.id.playlist_toolbar);
         initToolBar();
        GridLayoutManager manager=new GridLayoutManager(getContext(),2);
        playlistRecycler.setLayoutManager(manager);
        getPlaylistRequest();
    }*/


    private void getPlaylistRequest(){
        MusicRequestUtil.getPlayList(getContext(), new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String text=response.body().string();
                List<PlayList> playLists= Utility.handlePlayListResponse(text);
                PlaylistAdapter adapter=new PlaylistAdapter(playLists);
                adapter.setOnItemClicklistener(PlaylistFragment.this);
                playlistRecycler.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(getContext(),"playlist failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(ImageView imageView, PlayList playList) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("playlist",playList);
        bundle.putString("tag","playlist");
        Intent intent=new Intent(getContext(),SongListDetailActivity.class);
        intent.putExtra("song",bundle);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),imageView,"sharedView");//与xml文件对应
        startActivity(intent,options.toBundle());
    }
    private void initToolBar(){
        setHasOptionsMenu(true);
        ( (AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ( (AppCompatActivity)getActivity()).getSupportActionBar().setTitle("歌单");
        ( (AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentEntrust)getActivity()).popFragment(PlaylistFragment.TAG);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
