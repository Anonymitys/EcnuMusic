package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import classcollection.singer.Maps;
import ecnu.ecnumusic.R;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    private List<Maps> mapsList;
    private Context mContext;
    private OnTagClickListener listener;
    private int currentPosition=0;
    private int tag;
    public TextAdapter(int tag,List<Maps> maps) {
        mapsList = maps;
        this.tag=tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_text,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Maps map=mapsList.get(position);
        holder.tags.setText(map.name);
        if (currentPosition==position){
            holder.tags.setSelected(true);
            holder.tags.setTextColor(Color.WHITE);
        }else {
            holder.tags.setSelected(false);
            holder.tags.setTextColor(Color.parseColor("#8A000000"));
        }

        holder.tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    notifyItemChanged(currentPosition);
                    notifyItemChanged(position);
                    currentPosition=position;
                    currentPosition=position;
                    listener.onItemClick(tag,map.id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tags;
        public ViewHolder(View view) {
            super(view);
            tags=view.findViewById(R.id.tags);
        }
    }
    public void setOnTagClickListener(OnTagClickListener listener){
        this.listener=listener;
    }
    public interface OnTagClickListener{
        void onItemClick(int tag,int id);
    }
}
