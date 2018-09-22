package ecnu.ecnumusic;

import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Method;
import java.util.List;

import Utils.MusicUtil;
import adapter.LocalMusicRecyclerViewAdapter;
import classcollection.Music;
import classcollection.Song;
import service.MusicService;

public class LocalMusic extends BaseActivity implements LocalMusicRecyclerViewAdapter.OnLocalMusicItemClickListener{

   private RecyclerView recyclerView;
   private List<Music> musicList;
   private MusicService.MusicBinder musicBinder;
   private static final String TAG="LocalMusic";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music);
       recyclerView=(RecyclerView)findViewById(R.id.song_recyclerview);
       musicList= MusicUtil.getMusic(this);
        initRecyclerView();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.local_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
           actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void connection(IBinder service) {
        super.connection(service);
        musicBinder=(MusicService.MusicBinder)service;
    }

    @Override
    public void onPlayerStart() {
        super.onPlayerStart();
    }

    @Override
    public void onChange(Song song,Music music) {
        super.onChange(song,music);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

       }
        return super.onMenuOpened(featureId, menu);
    }

    private void initRecyclerView(){
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        LocalMusicRecyclerViewAdapter adapter=new LocalMusicRecyclerViewAdapter(musicList);
        adapter.setLocalMusicItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(Music music) {
        Log.e(TAG, "onItemClick: "+music.getPath() );
        musicBinder.playLocalSong(music);
    }
}

