package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private static final String TAG = "RankAdapter";

    public RankAdapter(List<Rank> ranks) {
        this.rankList = ranks;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView rankImageView;
        private RelativeLayout layout;
        private View itemView;
        private TextView text1, text2, text3;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            rankImageView = view.findViewById(R.id.rank_image);
            layout = view.findViewById(R.id.rl_rank);
            text1 = view.findViewById(R.id.text_view1);
            text2 = view.findViewById(R.id.text_view2);
            text3 = view.findViewById(R.id.text_view3);
        }

        private void setInvisblity() {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            itemView.setVisibility(View.GONE);
            params.height = 0;
            params.width = 0;
            itemView.setLayoutParams(params);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.rank_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final RankName rank = rankList.get(0).rankNameList.get(position);
        final List<RankSong> rankSongs = rank.rankSongList;
        if (!rank.listName.contains("MV")) {
            GlideImgManager.glideLoaderCircle(mContext, rank.rankIcon, R.drawable.album, R.drawable.album, holder.rankImageView);
            holder.text1.setText("1 " + getText(rankSongs.get(0)));
            holder.text2.setText("2 " + getText(rankSongs.get(1)));
            holder.text3.setText("3 " + getText(rankSongs.get(2)));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rankItemClickListener != null) {
                        rankItemClickListener.onRankItemClick(rank.topID, rank.rankIcon, rank.listName);
                    }
                }
            });
        } else {
            holder.setInvisblity();
        }

    }

    @Override
    public int getItemCount() {
        return rankList.get(0).rankNameList.size();
    }

    private OnRankItemClickListener rankItemClickListener;

    public void setRankItemClickListener(OnRankItemClickListener listener) {
        this.rankItemClickListener = listener;
    }

    public interface OnRankItemClickListener {
        void onRankItemClick(int topID, String icon, String updateTime);
    }

    private String getText(RankSong rankSong) {
        return rankSong.songname + "-" + rankSong.singername;
    }
}
