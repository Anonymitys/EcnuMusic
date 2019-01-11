package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classcollection.Singer;
import classcollection.Song;
import ecnu.ecnumusic.PlayMVActivity;
import ecnu.ecnumusic.R;
import shouyeclass.rank.SongData;
import shouyeclass.singersong.MusicData;
import shouyeclass.singersong.Musics;

public class SingerSongRecyclerAdapter extends RecyclerView.Adapter<SingerSongRecyclerAdapter.ViewHolder> {
    private List<Musics> musics;
    private List<Song> songList;
    private List<SongData> songDataList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private String songTag = "ecnu";

    public SingerSongRecyclerAdapter(List<Musics> musicsList) {
        this.musics = musicsList;
        songList = new ArrayList<>();
    }

    public SingerSongRecyclerAdapter(List<SongData> musicData, String tag) {
        songDataList = musicData;
        songList = new ArrayList<>();
        songTag = tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.songdetail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String text = "";
        Song musicData;
        if (songTag.equals("RankSongActivity")) {
            musicData = songDataList.get(position).song;
        } else {
            musicData = musics.get(position).musciData;
        }
        if (!TextUtils.isEmpty(musicData.vid.trim())) {
            Log.e("vid", "onBindViewHolder: "+musicData.songname+musicData.vid );
            holder.ivPlaymv.setVisibility(View.VISIBLE);
        }else{
            holder.ivPlaymv.setVisibility(View.GONE);
        }
        songList.add(musicData);
        holder.positionTextview.setText(String.valueOf(position + 1));
        holder.songname.setText(musicData.songname);
        for (Singer singer : musicData.singerlist) {
            text = text + singer.name + "/";
        }
        text = text.substring(0, text.length() - 1);
        if (!musicData.albumname.trim().isEmpty()) {
            text = text + "-" + musicData.albumname;
        }
        holder.singername.setText(text);

        holder.songItem.setOnClickListener((view) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, songList);
            }

        });
        holder.ivPlaymv.setOnClickListener((v) -> {
            Log.e("click", "onBindViewHolder: "+musicData.vid );
            PlayMVActivity.getLaunch((Activity) mContext, musicData.vid, "", musicData.songname);
        });
    }

    @Override
    public int getItemCount() {
        if (songTag.equals("RankSongActivity")) {
            return songDataList.size();
        }
        return musics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView positionTextview;
        private TextView songname;
        private TextView singername;
        private RelativeLayout songItem;
        private ImageView ivPlaymv;

        public ViewHolder(View view) {
            super(view);
            songname = view.findViewById(R.id.songname_textview);
            singername = view.findViewById(R.id.singername_textview);
            positionTextview = view.findViewById(R.id.position_textview);
            songItem = view.findViewById(R.id.rl_song_item);
            ivPlaymv = view.findViewById(R.id.play_mv_view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<Song> songs);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
}
