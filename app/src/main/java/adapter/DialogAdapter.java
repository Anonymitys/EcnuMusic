package adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import classcollection.Singer;
import classcollection.Song;
import ecnu.ecnumusic.R;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
    private List<Song> songList;
    private Context mContext;
    private int currentposition;
    private static final String TAG="DialogAdapter";
    public DialogAdapter(List<Song> songs,int position){
        songList=songs;
        currentposition=position;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView listenImage;
        private TextView songName;
        private TextView singerName;
        private ImageView deleteImage;
        private RelativeLayout layout;
        public ViewHolder(View view){
            super(view);
            listenImage=(ImageView)view.findViewById(R.id.listen_image);
            songName=(TextView)view.findViewById(R.id.song_textview);
            singerName=(TextView)view.findViewById(R.id.artist_textview);
            deleteImage=(ImageView) view.findViewById(R.id.expand_button);
            layout=(RelativeLayout) view.findViewById(R.id.ll_dialog);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.dialog_music_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (currentposition==position){
            holder.listenImage.setVisibility(View.VISIBLE);
            holder.singerName.setTextColor(Color.RED);
            holder.songName.setTextColor(Color.RED);
        }else {
            holder.listenImage.setVisibility(View.GONE);
            holder.singerName.setTextColor(Color.GRAY);
            holder.songName.setTextColor(Color.parseColor("#000000"));
        }
        String singerText="";
        final Song song=songList.get(position);
        holder.songName.setText(song.songname);
        for (Singer singer:song.singerlist){
            if (singer.name!=null){
                singerText=singerText+singer.name+"/";
            }else {
                singerText=singerText+singer.singer_name+"/";
            }

        }
        singerText=singerText.substring(0,singerText.length()-1);
        Log.e(TAG, "onBindViewHolder: "+singerText );
        holder.singerName.setText("-"+singerText);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener!=null){
                    notifyItemChanged(currentposition);
                    notifyItemChanged(position);
                    currentposition=position;
                    clickListener.onItemClick(position,song);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener=listener;
    }
    public interface OnItemClickListener{
        void onItemClick(int position,Song song);
    }
}
