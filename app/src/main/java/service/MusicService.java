package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import classcollection.Music;
import classcollection.Song;
import okhttp3.Request;
import okhttp3.Response;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "MusicService";
    private MusicBinder musicBinder = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private String lastSongmid = "ecnu";
    private String lastPath = "ecnu";
    private List<Song> musicList;
    private int currentPosition = -1;
    private OnPlayerEventListener listener;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        musicList = new ArrayList<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }

        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }

        public List<Song> getMusicList() {
            return musicList;
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public void setCurrentPosition(int position) {
            currentPosition = position;
        }

        /*public void playFromURL(int position, List<Song> songs) {
            currentPosition = position;
            Song song = songs.get(position);
            musicList.clear();
            musicList.addAll(songs);

            playFromURI(song);

        }*/

        public void playPause() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }

        public void playStart() {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }

        public void playNext() {
            Log.e(TAG, "playNext: ");
            if (currentPosition == musicList.size() - 1) {
                playFromvKey(musicList.get(0));
                currentPosition = 0;
            } else {
                playFromvKey(musicList.get(currentPosition + 1));
                currentPosition = currentPosition + 1;
            }
        }

        public void playSong(Song song) {

            currentPosition = currentPosition + 1;
            musicList.add(currentPosition, song);
            playFromvKey(song);
        }

        public void playFromURI(Song song) {
            //  String url="http://ws.stream.qqmusic.qq.com/C100"+song.songmid+".m4a?fromtag=0&guid=126548448";
            String url = "https://api.bzqll.com/music/tencent/url?key=579621905&id=" + song.songmid + "&br=128";
            Log.e(TAG, "playFromURI: " + url);
            if (!lastSongmid.equals(song.songmid)) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepareAsync();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                lastSongmid = song.songmid;
                listener.onChange(song, null);
            }
        }

        public void playFromvkey(int position, List<Song> songs) {
            currentPosition = position;
            Song song = songs.get(position);
            musicList.clear();
            musicList.addAll(songs);
            playFromvKey(song);
        }

        public void playLocalSong(Music music) {
            String path = music.getPath();
            if (!lastPath.equals(path)) {

                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepareAsync();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                lastPath = path;
                listener.onChange(null, music);
            }
        }

        public void playFromvKey(final Song song) {
            MusicRequestUtil.getvkey(getApplicationContext(), song.songmid, new ResultCallback() {
                @Override
                public void onResponse(Response response) throws IOException {
                    String str = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(str).getJSONObject("req").getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("midurlinfo");
                        String vkey = jsonArray.getJSONObject(0).getString("purl");
                        String url = "https://dl.stream.qqmusic.qq.com/" + vkey;
                        if (vkey.isEmpty()) {
                            url = "https://api.bzqll.com/music/tencent/url?key=579621905&id=" + song.songmid + "&br=320";
                        }
                        Log.e(TAG, "playUrl: "+url );
                        if (!lastSongmid.equals(song.songmid)) {
                            Log.e(TAG, "onResponse: "+lastSongmid );
                            Log.e(TAG, "onResponse: "+song.songmid );
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(url);
                                mediaPlayer.prepareAsync();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            lastSongmid = song.songmid;
                            listener.onChange(song, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Request request, Exception ex) {
                    Toast.makeText(getApplicationContext(), "getVkey failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "onPrepared: ");
        mediaPlayer.start();
        listener.onPlayerStart();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        musicBinder.playNext();
    }


    public void setOnPlayEventListener(OnPlayerEventListener onPlayEventListener) {
        listener = onPlayEventListener;
    }
}
