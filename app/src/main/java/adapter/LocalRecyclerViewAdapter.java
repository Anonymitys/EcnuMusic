package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ecnu.ecnumusic.LocalMusic;
import ecnu.ecnumusic.R;

public class LocalRecyclerViewAdapter extends RecyclerView.Adapter<LocalRecyclerViewAdapter.ViewHolder> {
    private List<Item> items;
    private Context mContext;

    public LocalRecyclerViewAdapter(){
        ininItem();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public LocalRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
       View view= LayoutInflater.from(mContext).inflate(R.layout.local_recyclerview,parent,false);
       ViewHolder holder=new ViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalRecyclerViewAdapter.ViewHolder holder, final int position) {
        Item item=items.get(position);
        holder.iconImage.setImageResource(item.getIconImageId());
        holder.textView.setText(item.getItemTextView());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent intent=new Intent(mContext, LocalMusic.class);
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                        default:
                            break;
                }
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        private ImageView iconImage;
        private TextView textView;
        public ViewHolder(View view){
            super(view);
            itemView=view;
            iconImage=(ImageView)view.findViewById(R.id.icon_imageview);
            textView=(TextView)view.findViewById(R.id.local_textview);
        }
    }
    public class Item{
        private int iconImageId;
        private String itemTextView;
        public Item(int iconImageId,String itemTextView){
            this.iconImageId=iconImageId;
            this.itemTextView=itemTextView;
        }
        public int getIconImageId(){
            return this.iconImageId;
        }
        public String getItemTextView(){
            return this.itemTextView;
        }
    }
    private void ininItem(){
        items=new ArrayList<>();
        items.add(new Item(R.drawable.local_music,"本地音乐"));
         items.add(new Item(R.drawable.latest_play,"最近播放"));
        items.add(new Item(R.drawable.download,"下载管理"));
        items.add(new Item(R.drawable.transceiver,"我的电台"));
        items.add(new Item(R.drawable.collect,"我的收藏"));
    }
}
