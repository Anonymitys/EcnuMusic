package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import classcollection.singer.SingerDetail;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SingerDetailActivity;

public class SingerRecyclerAdapter extends RecyclerView.Adapter<SingerRecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<SingerDetail> singers;
    private Activity mActivity;

    public SingerRecyclerAdapter(List<SingerDetail> list) {
        this.singers = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
            mActivity = (Activity) mContext;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.singer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SingerDetail singer = singers.get(position);
        // Glide.with(mContext).load(singer.singer_pic).into(holder.singer_pic);
        GlideImgManager.glideLoaderCircle(mContext, singer.singer_pic, R.drawable.album, R.drawable.album, holder.singer_pic);
        holder.singer_name.setText(singer.singer_name);
        holder.singer_rank.setText(String.valueOf(position + 1));
        holder.singer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SingerDetailActivity.class);
                intent.putExtra("singerMid", singer.singer_mid);
                intent.putExtra("singerPic", singer.singer_pic);
                intent.putExtra("singerName", singer.singer_name);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_alpha_in_from_center,R.anim.animation_static);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout singer_layout;
        private TextView singer_rank;
        private ImageView singer_pic;
        private TextView singer_name;

        public ViewHolder(View view) {
            super(view);
            singer_rank = view.findViewById(R.id.singer_rank);
            singer_name = view.findViewById(R.id.singer_name);
            singer_pic = view.findViewById(R.id.singer_pic);
            singer_layout = view.findViewById(R.id.singer_item);
        }
    }

}
