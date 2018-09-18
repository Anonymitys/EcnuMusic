package adapter;

import android.content.Context;
import android.icu.util.ValueIterator;
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
import shouyeclass.rank.Rank;
import shouyeclass.rank.RankName;

public class RankGlobalAdapter extends RecyclerView.Adapter<RankGlobalAdapter.ViewHolder> {
    private List<Rank> rankList;
    private Context mContext;
    public RankGlobalAdapter(List<Rank> ranks){
        rankList=ranks;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView globalImage;
        private TextView globalText;
        private LinearLayout layout;
        public ViewHolder(View view){
            super(view);
            globalImage=(ImageView) view.findViewById(R.id.rank_icon);
            globalText=(TextView)view.findViewById(R.id.rank_list);
            layout=(LinearLayout)view.findViewById(R.id.ll_rank_global);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.globalrank_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RankName rankName=rankList.get(1).rankNameList.get(position);
        GlideImgManager.glideLoaderCircle(mContext,rankName.rankIcon,R.drawable.album,R.drawable.album,holder.globalImage);
        holder.globalText.setText(rankName.listName);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGlobalitemClickListener!=null){
                    onGlobalitemClickListener.onGlobalItemClick(rankName.topID,rankName.rankIcon,rankName.listName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rankList.get(1).rankNameList.size();
    }
    private OnGlobalitemClickListener onGlobalitemClickListener;
    public void setOnGlobalitemClickListener(OnGlobalitemClickListener listener){
        this.onGlobalitemClickListener=listener;
    }
    public interface OnGlobalitemClickListener{
        void onGlobalItemClick(int topID,String icon,String updateTime);
    }
}
