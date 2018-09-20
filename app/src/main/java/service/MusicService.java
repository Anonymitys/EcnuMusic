package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classcollection.Music;
import classcollection.Song;
import ecnu.ecnumusic.MainActivity;
import ecnu.ecnumusic.R;
import fragments.PlaybarFragment;
import widget.CircleView;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{
    private static final String TAG="MusicService";
    private MusicBinder musicBinder=new MusicBinder();
    private MediaPlayer mediaPlayer;
    private String lastUrl="ecnu";
    private List<Song> musicList;
    private int currentPosition;
    private OnPlayerEventListener listener;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: " );
        musicList=new ArrayList<>();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return musicBinder;
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
        public MediaPlayer getMediaPlayer(){
            return mediaPlayer;
        }
        public List<Song> getMusicList(){
            return musicList;
        }
        public int getCurrentPosition(){
            return currentPosition;
        }
        public void playFromURL(int position,List<Song> songs){
            currentPosition=position;
            Song song=songs.get(position);
            musicList.clear();
            musicList.addAll(songs);

             playFromURI(song);

        }
        public void playPause(){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
        public void playStart(){
            if (!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }
        public void playNext(){
           if (currentPosition==musicList.size()-1){
               playFromURI(musicList.get(0));
               currentPosition=0;
           }else {
               playFromURI(musicList.get(currentPosition+1));
               currentPosition=currentPosition+1;
           }
        }
        public void playSong(Song song){
            String url="http://ws.stream.qqmusic.qq.com/C100"+song.songmid+".m4a?fromtag=0&guid=126548448";

            if (!lastUrl.equals(url)){

                musicList.add(currentPosition+1,song);
                try{
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();

                }catch (IOException ex){
                    ex.printStackTrace();
                }
                lastUrl=url;
                listener.onChange(song);
            }
        }
    }
    private void playFromURI(Song song){
        String url="http://ws.stream.qqmusic.qq.com/C100"+song.songmid+".m4a?fromtag=0&guid=126548448";
        if (!lastUrl.equals(url)){

            try{
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();

            }catch (IOException ex){
                ex.printStackTrace();
            }
            lastUrl=url;
            listener.onChange(song);
      }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        listener.onPlayerStart();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        musicBinder.playNext();
    }

    public void setOnPlayEventListener(OnPlayerEventListener onPlayEventListener){
        listener=onPlayEventListener;
    }
}
