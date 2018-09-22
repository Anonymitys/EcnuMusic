package adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import classcollection.Music;
import classcollection.Song;
import ecnu.ecnumusic.R;

public class LocalMusicRecyclerViewAdapter extends RecyclerView.Adapter<LocalMusicRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Music> mLocalMusic;
    private List<Song> songList;
    private MediaPlayer mediaPlayer;
    public LocalMusicRecyclerViewAdapter(List<Music> localMusics){
        mLocalMusic=localMusics;
        songList=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.local_music_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocalMusicRecyclerViewAdapter.ViewHolder holder, final int position) {
        String text="";
        final Music localMusic=mLocalMusic.get(position);
        holder.songTextView.setText(localMusic.getTitle());
        text=localMusic.getArtist()+"-"+localMusic.getAlbum();
        holder.artistTextView.setText(text);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null){
                    itemClickListener.onItemClick(localMusic);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocalMusic.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView songTextView;
        TextView artistTextView;
        View mview;
        ImageView imageView;
        RelativeLayout layout;
        public ViewHolder(View view){
            super(view);
            mview=view;
            songTextView=(TextView)view.findViewById(R.id.song_textview);
            artistTextView=(TextView)view.findViewById(R.id.artist_textview);
            imageView=(ImageView)view.findViewById(R.id.listen_image);
            layout=(RelativeLayout) view.findViewById(R.id.play_list);
        }

    }
    private OnLocalMusicItemClickListener itemClickListener;
    public void setLocalMusicItemClickListener(OnLocalMusicItemClickListener listener){
        itemClickListener=listener;
    }
    public interface OnLocalMusicItemClickListener{
        void onItemClick(Music music);
    }

}
