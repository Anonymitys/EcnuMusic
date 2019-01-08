package adapter;

import android.app.Activity;
import android.content.Context;
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
import classcollection.mv.MVDetail;
import ecnu.ecnumusic.PlayMVActivity;
import ecnu.ecnumusic.R;

public class MVAdapter extends RecyclerView.Adapter<MVAdapter.ViewHolder> {
    private List<MVDetail> details;
    private Context mContext;

    public MVAdapter(List<MVDetail> mvDetailList) {
        this.details = mvDetailList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MVDetail detail = details.get(position);
        GlideImgManager.glideLoaderCircle(mContext, detail.pic, R.drawable.album, R.drawable.album, holder.pic);
        holder.title.setText(detail.title);
        holder.updateTime.setText(detail.date);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMVActivity.getLaunch((Activity) mContext, detail.vid, detail.pic, detail.title);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView title;
        private TextView updateTime;
        private RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            pic = view.findViewById(R.id.mv_pic);
            title = view.findViewById(R.id.title);
            updateTime = view.findViewById(R.id.update_time);
            layout = view.findViewById(R.id.rl_mv);
        }
    }
}
