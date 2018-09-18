package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ecnu.ecnumusic.R;
import shouyeclass.rank.RankSong;

public class RankSongNameAdapter extends RecyclerView.Adapter <RankSongNameAdapter.ViewHolder>{
    private List<RankSong> rankSongList;
    private Context mContext;
    public RankSongNameAdapter(List<RankSong> songs){
        this.rankSongList=songs;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView songNumber;
        private TextView songName;
        public ViewHolder(View view){
            super(view);
            songName=(TextView)view.findViewById(R.id.rank_song);
            songNumber=(TextView)view.findViewById(R.id.ranksong_number);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (mContext==null){
           mContext=parent.getContext();
       }
       View view= LayoutInflater.from(mContext).inflate(R.layout.ranksongname_item,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankSong rankSong=rankSongList.get(position);
        holder.songNumber.setText(String.valueOf(position+1));
        String str=rankSong.songname+"-"+rankSong.singername;
        holder.songName.setText(str);

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
