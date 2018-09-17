package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Utils.GlideImgManager;
import classcollection.Song;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SongListDetailActivity;
import shouyeclass.Album;

public class SingerAlbumRecyclerAdapter extends RecyclerView.Adapter<SingerAlbumRecyclerAdapter.ViewHolder> {

    private List<Album> albumList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
   public SingerAlbumRecyclerAdapter(List<Album> list){
       albumList=list;
   }

    class ViewHolder extends RecyclerView.ViewHolder{
       private ImageView albumImage;
       private TextView albumName;
       private TextView pubTime;
       private LinearLayout linearLayout;
        public ViewHolder(View view){
            super(view);
            albumImage=(ImageView)view.findViewById(R.id.album_pic);
            albumName=(TextView)view.findViewById(R.id.album_name);
            pubTime=(TextView)view.findViewById(R.id.public_time);
            linearLayout=(LinearLayout)view.findViewById(R.id.album_ll);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (mContext==null){
           mContext=parent.getContext();
       }
       View view=LayoutInflater.from(mContext).inflate(R.layout.singeralbum_item,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       final Album album=albumList.get(position);
        String url="https://y.gtimg.cn/music/photo_new/T002R300x300M000"+album.album_mid+".jpg?max_age=2592000";
        GlideImgManager.glideLoaderCircle(mContext,url,R.drawable.album,R.drawable.album,holder.albumImage);
        holder.albumName.setText(album.album_name);
        holder.pubTime.setText(album.pub_time);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder.albumImage,album);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    public interface OnItemClickListener{
        void onItemClick(ImageView imageView,Album album);
    }
    public void setOnItemClickListener(SingerAlbumRecyclerAdapter.OnItemClickListener listener){
        onItemClickListener=listener;
    }
}
