package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import Utils.GlideImgManager;
import classcollection.mv.OtherMv;
import ecnu.ecnumusic.R;

public class OtherMvAdapter extends RecyclerView.Adapter<OtherMvAdapter.ViewHolder> {
    private Context mContext;
    private List<OtherMv> otherMvs;
    private OnItemClickListener listener;
    public OtherMvAdapter(List<OtherMv> mvs) {
        this.otherMvs = mvs;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OtherMv mv=otherMvs.get(position);
        GlideImgManager.glideLoaderCircle(mContext,mv.cover_pic,R.drawable.album,R.drawable.album,holder.pic);
        holder.title.setText(mv.name);
        holder.uploader.setText(mv.uploader_nick);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(mv.vid,mv.name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherMvs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView title;
        private TextView uploader;
        private RelativeLayout layout;

        public ViewHolder(View view) {
            super(view);
            pic = view.findViewById(R.id.mv_pic);
            title = view.findViewById(R.id.title);
            uploader = view.findViewById(R.id.update_time);
            layout = view.findViewById(R.id.rl_mv);
        }
    }
    public void setOnOtherMvClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
    public interface OnItemClickListener{
        void onItemClick(String vid,String name);
    }

    public void addItem(List<OtherMv> mvs){
        otherMvs.clear();
        otherMvs.addAll(mvs);
        notifyDataSetChanged();
    }
}
