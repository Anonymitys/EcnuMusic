package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.List;

import Utils.GlideImgManager;
import ecnu.ecnumusic.R;
import shouyeclass.rank.Rank;
import shouyeclass.rank.RankName;
import shouyeclass.rank.RankSong;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<Rank> rankList;
    private Context mContext;
    private static final String TAG="RankAdapter";
    public RankAdapter(List<Rank> ranks){
        this.rankList= ranks;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView rankImageView;
        private RecyclerView recyclerView;
        private LinearLayout layout;
        private View itemView;
        public ViewHolder(View view){
            super(view);
            itemView=view;
            rankImageView=(ImageView)view.findViewById(R.id.rank_image);
            recyclerView=(RecyclerView)view.findViewById(R.id.rank_recycler);
            layout=(LinearLayout)view.findViewById(R.id.ll_rank);
        }
        private void setInvisblity(){
            RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)itemView.getLayoutParams();
                itemView.setVisibility(View.GONE);
                params.height=0;
                params.width=0;
            itemView.setLayoutParams(params);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.rank_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final RankName rank=rankList.get(0).rankNameList.get(position);
        if (!rank.listName.contains("MV")){
            GlideImgManager.glideLoaderCircle(mContext,rank.rankIcon,R.drawable.album,R.drawable.album,holder.rankImageView);
            LinearLayoutManager manager=new LinearLayoutManager(mContext);
            holder.recyclerView.setLayoutManager(manager);
            RankSongNameAdapter adapter=new RankSongNameAdapter(rank.rankSongList);
            holder.recyclerView.setAdapter(adapter);
            holder.recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction()==MotionEvent.ACTION_UP){
                        holder.layout.performClick();
                    }
                    return false;
                }
            });
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rankItemClickListener!=null){
                        rankItemClickListener.onRankItemClick(rank.topID,rank.rankIcon,rank.listName);
                    }
                }
            });
        }else {
           holder.setInvisblity();
        }

    }

    @Override
    public int getItemCount() {
        return rankList.get(0).rankNameList.size();
    }

    private OnRankItemClickListener rankItemClickListener;

    public void setRankItemClickListener(OnRankItemClickListener listener){
        this.rankItemClickListener=listener;
    }
    public interface OnRankItemClickListener{
        void onRankItemClick(int topID,String icon,String updateTime);
    }
}
