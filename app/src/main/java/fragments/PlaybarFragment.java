package fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import Utils.GlideImgManager;
import classcollection.Song;
import ecnu.ecnumusic.R;

public class PlaybarFragment extends Fragment implements View.OnClickListener{

    private ImageView playPause,playList,songImage;
    private TextView songName,songLyrics;
    private OnPlaybarListener playbarListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.play_bar,container,false);
        playPause=(ImageView)view.findViewById(R.id.play_pause_bar);
        playList=(ImageView)view.findViewById(R.id.play_list_bar);
        songImage=(ImageView)view.findViewById(R.id.play_image_bar);
        songName=(TextView) view.findViewById(R.id.playbar_song_name);
        songLyrics=(TextView)view.findViewById(R.id.playbar_lyrics);
       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        playPause.setOnClickListener(this);
        playList.setOnClickListener(this);
    }
    public void setOnPlayListener(OnPlaybarListener listener){
        playbarListener=listener;
    }
    public interface OnPlaybarListener{
        void onPlayPauseClick();
        void onPlayListClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause_bar:
                if (playbarListener!=null){
                    playbarListener.onPlayPauseClick();
                }
                break;
            case R.id.play_list_bar:
                if (playbarListener!=null){

                    playbarListener.onPlayListClick();
                }
                break;
        }
    }
    public void setPlaybarUI(Context context,Song song){
        String url="https://y.gtimg.cn/music/photo_new/T002R300x300M000"+song.albummid+".jpg?max_age=2592000";
        songName.setText(song.songname);
        GlideImgManager.glideLoaderCircle(context,url,R.drawable.album,R.drawable.album,
                songImage);

    }
    public void setPlay(){
        playPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
    }
    public void setPause(){
        playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
    }
}
