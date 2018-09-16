package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Utils.GlideImgManager;
import ecnu.ecnumusic.R;
import shouyeclass.Album;
import shouyeclass.Vhot;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class RecommendSonglistAdapter extends RecyclerView.Adapter<RecommendSonglistAdapter.ViewHolder> {
  //  private List<RecommendList> recommendLists;
    private List<Vhot> vhotList;
    private List<Album>  albumList;
    private Context mContext;
    private String mTag;

    public RecommendSonglistAdapter(String tag,List<Vhot> vhots,List<Album> albums){
        vhotList=vhots;
        albumList=albums;
        mTag=tag;

    }
    @Override
    public int getItemCount() {
        if ("songlist".equals(mTag)){
            return vhotList.size()>6?6:vhotList.size();
        }else {
            return albumList.size()>6?6:albumList.size();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
       View view= LayoutInflater.from(mContext).inflate(R.layout.recommendlist_songlist_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Vhot recommend=vhotList.get(position);
        final Album album=albumList.get(position);
        if ("songlist".equals(mTag)){
            holder.backgroundImage.setVisibility(View.GONE);
            GlideImgManager.glideLoaderCircle(mContext,recommend.cover,R.drawable.album,R.drawable.album,
                    holder.imageView);
            holder.textView.setText(Html.fromHtml(recommend.title,FROM_HTML_MODE_COMPACT));

        }else {
            holder.backgroundImage.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params=holder.imageView.getLayoutParams();
            params.height=295;
            params.width=295;
            holder.imageView.setLayoutParams(params);
            String url="https://y.gtimg.cn/music/photo_new/T002R300x300M000"+album.album_mid+".jpg?max_age=2592000";
            holder.backgroundImage.setBackground(mContext.getDrawable(R.drawable.album_background));
            GlideImgManager.glideLoaderCircle(mContext,url,R.drawable.album,R.drawable.album,
                      holder.imageView);
           holder.textView.setText(Html.fromHtml(album.album_name,FROM_HTML_MODE_COMPACT));

        }




        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onItemClick(mTag,holder.imageView,recommend,album);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView,backgroundImage;
        private TextView textView;
        private LinearLayout layout;
        public ViewHolder(View view){
            super(view);
            layout=(LinearLayout)view.findViewById(R.id.ll_songlist);
            imageView=(ImageView)view.findViewById(R.id.iv_recommendlist);
            textView=(TextView)view.findViewById(R.id.songlist_describe);
            backgroundImage=(ImageView)view.findViewById(R.id.background_album);

        }

    }

    public interface OnItemClickListener{
       // public void onItemClick(ImageView imageView,RecommendList recommendList);
       public void onItemClick(String tag,ImageView imageView,Vhot vhot,Album album);
    }
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }
}
