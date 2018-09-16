package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classcollection.Singer;
import classcollection.Song;
import ecnu.ecnumusic.R;
import shouyeclass.singersong.MusicData;
import shouyeclass.singersong.Musics;

public class SingerSongRecyclerAdapter extends RecyclerView.Adapter<SingerSongRecyclerAdapter.ViewHolder> {
    private List<Musics> musics;
    private List<Song> songList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SingerSongRecyclerAdapter(List<Musics> musicsList){
        this.musics=musicsList;
        songList=new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.songdetail_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String text="";
        Song musicData=musics.get(position).musciData;
        songList.add(musicData);
        holder.positionTextview.setText(String.valueOf(position+1));
        holder.songname.setText(musicData.songname);
        for (Singer singer:musicData.singerlist){
            text=text+singer.name+"/";
        }
        text=text.substring(0,text.length()-1);
        if (!musicData.albumname.trim().isEmpty()){
            text=text+"-"+musicData.albumname;
        }

        holder.singername.setText(text);

        holder.songItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position,songList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView positionTextview;
        private TextView songname;
        private TextView singername;
        private RelativeLayout songItem;
        public ViewHolder(View view) {
            super(view);
            songname = (TextView) view.findViewById(R.id.songname_textview);
            singername = (TextView) view.findViewById(R.id.singername_textview);
            positionTextview = (TextView) view.findViewById(R.id.position_textview);
            //  songItem=(LinearLayout)view.findViewById(R.id.ll_song_item);
            songItem = (RelativeLayout) view.findViewById(R.id.rl_song_item);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position,List<Song> songs);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener=listener;
    }
}
