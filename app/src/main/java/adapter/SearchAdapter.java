package adapter;

import android.content.Context;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import Utils.GlideImgManager;
import classcollection.Singer;
import classcollection.Song;
import ecnu.ecnumusic.R;
import shouyeclass.searchsong.SearchSong;
import shouyeclass.searchsong.SongSearch;
import shouyeclass.searchsong.ZhidaoSinger;

public class SearchAdapter extends RecyclerView.Adapter {
    private List<SearchSong> songList;
    private Context mContext;
    private ZhidaoSinger zhidaoSinger;
    private static final int SINGER_ITEM=1;
    private static final int SINGER_SONG=2;

    public SearchAdapter(SongSearch songs){
        songList=songs.search.searchSongList;
        if (songs.zhidao.zhidaoSinger!=null){
            zhidaoSinger=songs.zhidao.zhidaoSinger;
        }
    }
    class ViewSongHolder extends RecyclerView.ViewHolder{
        private TextView songView,singerView,moreView;
        private RelativeLayout ll_songitem;
        public ViewSongHolder(View view){
            super(view);

            songView=(TextView)view.findViewById(R.id.songname_textview);
            singerView=(TextView)view.findViewById(R.id.singername_textview);
            ll_songitem=(RelativeLayout) view.findViewById(R.id.ll_song_item);
            moreView=(TextView)view.findViewById(R.id.more_version);
        }

    }
    class ViewSingerHolder extends RecyclerView.ViewHolder{
        private ImageView singerImage;
        private TextView singerName;
        private RelativeLayout layout;
        public ViewSingerHolder(View view){
            super(view);
            singerImage=(ImageView)view.findViewById(R.id.singer_pic);
            singerName=(TextView)view.findViewById(R.id.singername_textview);
            layout=(RelativeLayout)view.findViewById(R.id.rl_singer);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        if (viewType==SINGER_ITEM){
            View view=LayoutInflater.from(mContext).inflate(R.layout.search_singer_item,parent,false);
            return new ViewSingerHolder(view);
        }else if (viewType==SINGER_SONG){
            View view= LayoutInflater.from(mContext).inflate(R.layout.search_item,parent,false);
            return new ViewSongHolder(view);
        }
      return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==SINGER_ITEM){
            ViewSingerHolder singerHolder=(ViewSingerHolder)holder;
            GlideImgManager.glideLoaderCircle(mContext,zhidaoSinger.singerPic,R.drawable.album,R.drawable.album,singerHolder.singerImage);
            singerHolder.singerName.setText("歌手："+zhidaoSinger.singerName);
            singerHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onSingerClick(zhidaoSinger.singerMID,zhidaoSinger.singerPic,zhidaoSinger.singerName);
                    }
                }
            });
        }else if (getItemViewType(position)==SINGER_SONG){
            final SearchSong searchSong;
            ViewSongHolder songHolder=(ViewSongHolder)holder;
            if (zhidaoSinger!=null){
                searchSong=songList.get(position-1);
            }else {
                searchSong=songList.get(position);
            }
            String text="";
            if (searchSong.grpList.size()==0){
                songHolder.moreView.setVisibility(View.GONE);
            }else{
                songHolder.moreView.setVisibility(View.GONE);
            }
            songHolder.songView.setText(searchSong.title);
            for(Singer singer:searchSong.singerList){
                text=text+singer.name+"/";
            }
            text=text.substring(0,text.length()-1);
            if (!searchSong.album.name.trim().isEmpty()){
                text=text+"-"+searchSong.album.name;
            }

            songHolder.singerView.setText(text);
            songHolder.ll_songitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        Song song=new Song();
                        song.albummid=searchSong.album.mid;
                        song.albumname=searchSong.album.name;
                        song.singerlist=searchSong.singerList;
                        song.songname=searchSong.title;
                        song.songmid=searchSong.songmid;
                        onItemClickListener.onSongClick(song);
                    }
                }
            });
        }
    }



    @Override
    public int getItemViewType(int position) {
        if (position==0&&zhidaoSinger!=null){
            return SINGER_ITEM;
        }
        return SINGER_SONG;
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    public OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener=listener;
    }
    public interface OnItemClickListener{
        void onSingerClick(String singerMid,String singerPic,String singerName);
        void onSongClick(Song song);
    }
}
