package ecnu.ecnumusic;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import Utils.GlideImgManager;
import Utils.MusicUtil;
import adapter.DialogAdapter;
import classcollection.Music;
import classcollection.Song;
import service.MusicService;
import service.OnPlayerEventListener;
import widget.CircleView;

public class BaseActivity extends AppCompatActivity implements OnPlayerEventListener,View.OnClickListener,DialogAdapter.OnItemClickListener{

    private static final String TAG = "BaseActivity";
    private MusicService.MusicBinder musicBinder;
    private WindowManager windowManager;
    private FrameLayout container;
    private View playbarView;
    private ImageView playPause,playList,songImage;
    private TextView songName,songLyrics;
    private CircleView circleView;
    private Handler handler=new Handler();
    private DialogAdapter adapter;

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: " );
            connection(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: " );
        }
    };

    public void connection(IBinder service){
        musicBinder=(MusicService.MusicBinder)service;
      if (musicBinder.getMusicList().size()>0){
          playbarView.setVisibility(View.VISIBLE);
          Song song=musicBinder.getMusicList().get(musicBinder.getCurrentPosition());
          String url="https://y.gtimg.cn/music/photo_new/T002R300x300M000"+song.albummid+".jpg?max_age=2592000";
          songName.setText(song.songname);
          GlideImgManager.glideLoaderCircle(this,url,R.drawable.album,R.drawable.album,
                  songImage);
          if (musicBinder.getMediaPlayer().isPlaying()){
             setPlay();
          }else {
              setPause();
          }

      }else {
          playbarView.setVisibility(View.GONE);
      }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup decorView=(ViewGroup)getWindow().getDecorView();



        container=(FrameLayout)((ViewGroup)decorView.getChildAt(0)).getChildAt(1);
        playbarView= LayoutInflater.from(getBaseContext()).inflate(R.layout.play_bar,null);
        playPause=(ImageView)playbarView.findViewById(R.id.play_pause_bar);
        playList=(ImageView)playbarView.findViewById(R.id.play_list_bar);
        songImage=(ImageView)playbarView.findViewById(R.id.play_image_bar);
        songName=(TextView) playbarView.findViewById(R.id.playbar_song_name);
        songLyrics=(TextView)playbarView.findViewById(R.id.playbar_lyrics);
        circleView=(CircleView)playbarView.findViewById(R.id.circle_view);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity=Gravity.BOTTOM;
        container.addView(playbarView,layoutParams);
        playbarView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=new Intent();
        intent.setClass(this,MusicService.class);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        playPause.setOnClickListener(this);
        playList.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause_bar:
                onPlayPauseClick();
                break;
            case R.id.play_list_bar:
              //  onPlayListClick();
                showBottomDialog();
                break;
        }
    }
    public void onPlayPauseClick(){
        Log.e(TAG, "onPlayPauseClick: " );
        if (musicBinder.getMediaPlayer().isPlaying()){
            musicBinder.playPause();
            setPause();
        }else {

            musicBinder.playStart();
            setPlay();

        }
    }
    public void onPlayListClick(){
        Log.e(TAG, "onPlayListClick: " );
        musicBinder.playNext();
    }

    @Override
    public void onChange(Song song, Music music) {
        if (song!=null){
            String url="https://y.gtimg.cn/music/photo_new/T002R300x300M000"+song.albummid+".jpg?max_age=2592000";
            songName.setText(song.songname);
            GlideImgManager.glideLoaderCircle(this,url,R.drawable.album,R.drawable.album,
                    songImage);

        }else{
            Log.e(TAG, "localMusic onChange: "+music.getTitle() );
            songName.setText(music.getTitle());
            GlideImgManager.glideLoaderCircle(this, MusicUtil.getAlbumArt(this,music.getAlbum_id()),R.drawable.album,R.drawable.album,
                    songImage);
        }
        playbarView.setVisibility(View.VISIBLE);
        playPause.setBackground(getDrawable(R.drawable.play));
        circleView.setVisibility(View.VISIBLE);
        circleView.setCurrentAngle(0f);

    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onPlayerStart() {
        Log.e(TAG, "onPlayerStart: " );

        setPlay();
    }

    public void setPlay(){
        playPause.setBackground(getDrawable(R.drawable.play));
        circleView.setVisibility(View.VISIBLE);
        circleView.setDuration(musicBinder.getMediaPlayer().getDuration()/1000);
       // circleView.setBackColorTransparent(false);
        handler.post(progressBarRunable);
    }

    public void setPause(){
        playPause.setBackground(getDrawable(R.drawable.pause));
        circleView.setVisibility(View.VISIBLE);
        circleView.setDuration(musicBinder.getMediaPlayer().getDuration()/1000);
        circleView.setCurrentAngle(musicBinder.getMediaPlayer().getCurrentPosition()/1000);

        circleView.invalidate();
    }
    Runnable progressBarRunable=new Runnable() {
        @Override
        public void run() {
            circleView.setCurrentAngle(musicBinder.getMediaPlayer().getCurrentPosition()/1000);
            circleView.invalidate();
            if (musicBinder.getMediaPlayer().isPlaying()){
                handler.postDelayed(progressBarRunable,800);
            }
        }
    };

    private void showBottomDialog(){
        Dialog dialog=new Dialog(this,R.style.BottomDialog);
        View view=LayoutInflater.from(this).inflate(R.layout.dialog_musiclist,null);
        dialog.setContentView(view);
        RecyclerView recyclerView=view.findViewById(R.id.dialog_recycler);
        LinearLayoutManager manager=new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        adapter=new DialogAdapter(musicBinder.getMusicList(),musicBinder.getCurrentPosition());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height=(int)(getResources().getDisplayMetrics().heightPixels*0.6);
        view.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.show();
    }

    @Override
    public void onItemClick(int position, Song song) {
        musicBinder.setCurrentPosition(position);
        musicBinder.playFromURI(song);

    }


}
