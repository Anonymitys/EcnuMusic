package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Utils.GlideImgManager;
import ecnu.ecnumusic.R;
import shouyeclass.PlayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<PlayList> playLists;
    private Context mContext;
    public PlaylistAdapter(List<PlayList> lists){
        this.playLists=lists;
    }
    @Override
    public int getItemCount() {
        return playLists.size();
    }




     @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }

            View view= LayoutInflater.from(mContext).inflate(R.layout.playlist_item,parent,false);
            return new ViewHolder(view);



    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final PlayList playList=playLists.get(position);
        GlideImgManager.glideLoaderCircle(mContext,playList.imgurl,R.drawable.album,R.drawable.album,holder.playlistImg);
        holder.playlistTitle.setText(playList.dissname);
        holder.playlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicklistener!=null){
                    clicklistener.onItemClick(holder.playlistImg,playList);
                }
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView playlistImg;
        private TextView playlistTitle;
        private LinearLayout playlistLayout;
        public ViewHolder(View view){
            super(view);
            playlistImg=(ImageView)view.findViewById(R.id.playlist_image);
            playlistTitle=(TextView)view.findViewById(R.id.playlist_title);
            playlistLayout=(LinearLayout)view.findViewById(R.id.playlist_layout);
        }
    }
    private OnItemClickListener clicklistener;
    public void setOnItemClicklistener(OnItemClickListener listener){
        this.clicklistener=listener;
    }
    public interface OnItemClickListener{
        public void onItemClick(ImageView imageView,PlayList playList);
    }
}
