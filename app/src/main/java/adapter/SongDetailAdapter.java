package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import classcollection.Singer;
import classcollection.Song;
import Utils.GlideImgManager;
import ecnu.ecnumusic.PlayMVActivity;
import ecnu.ecnumusic.R;

public class SongDetailAdapter extends RecyclerView.Adapter<SongDetailAdapter.ViewHolder> {
    private List<Song> songList;
    private Context mContext;
    private String fromWhat;

    public SongDetailAdapter(List<Song> songs, String tag) {
        this.songList = songs;
        fromWhat = tag;
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
        final Song song = songList.get(position);
        if (!TextUtils.isEmpty(song.vid)){
            holder.ivPlayMv.setVisibility(View.VISIBLE);
        }else{
            holder.ivPlayMv.setVisibility(View.GONE);
        }
        String url = "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + song.albummid + ".jpg?max_age=2592000";
        if ("DayRecommend".equals(fromWhat)) {
            holder.album_image.setVisibility(View.VISIBLE);
            holder.positionTextview.setVisibility(View.INVISIBLE);
            GlideImgManager.glideLoaderCircle(mContext, url, R.drawable.album, R.drawable.album,
                    holder.album_image);
        } else {
            holder.album_image.setVisibility(View.GONE);
            holder.positionTextview.setVisibility(View.VISIBLE);
            holder.positionTextview.setText(String.valueOf(position + 1));
        }
        holder.songname.setText(song.songname);
        for (Singer singer : song.singerlist) {
            text = text + singer.name + "/";
        }
        text = text.substring(0, text.length() - 1);
        if (!song.albumname.trim().isEmpty()) {
            text = text + "-" + song.albumname;
        }

        holder.singername.setText(text);

        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemListener != null) {
                    mItemListener.onItemClick(position, songList);
                }
            }
        });
        holder.ivPlayMv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMVActivity.getLaunch((Activity) mContext, song.vid, "", song.songname);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView positionTextview;
        private TextView songname;
        private TextView singername;
        private RelativeLayout songItem;
        private ImageView album_image,ivPlayMv;

        public ViewHolder(View view) {
            super(view);
            songname = view.findViewById(R.id.songname_textview);
            singername = view.findViewById(R.id.singername_textview);
            positionTextview = view.findViewById(R.id.position_textview);
            songItem = view.findViewById(R.id.rl_song_item);
            album_image = view.findViewById(R.id.album_imageview);
            ivPlayMv=view.findViewById(R.id.play_mv_view);
        }
    }

    private OnItemClickListener mItemListener;

    public interface OnItemClickListener {
        void onItemClick(int position, List<Song> songs);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }
}
