package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import ClassCollection.Music;
import Utils.MusicUtil;
import Utils.VariableUtil;
import ecnu.ecnumusic.PlayActivity;
import ecnu.ecnumusic.R;

public class LocalMusicRecyclerViewAdapter extends RecyclerView.Adapter<LocalMusicRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Music> mLocalMusic;
    private MediaPlayer mediaPlayer;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    public LocalMusicRecyclerViewAdapter(List<Music> localMusics,MediaPlayer player){
        mLocalMusic=localMusics;
        mediaPlayer=player;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        pref= PreferenceManager.getDefaultSharedPreferences(mContext);
        VariableUtil.lastPosition=pref.getInt("position",-1);
        View view= LayoutInflater.from(mContext).inflate(R.layout.local_music_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocalMusicRecyclerViewAdapter.ViewHolder holder, final int position) {
        Music localMusic=mLocalMusic.get(position);
        holder.songTextView.setText(localMusic.getTitle());
       // holder.artistTextView.setText(localMusic.getArtist());
        holder.artistTextView.setText(String.valueOf(localMusic.getAlbum_id()));
        if(VariableUtil.lastPosition!=-1&&position==VariableUtil.lastPosition){
            holder.imageView.setVisibility(View.VISIBLE);
            VariableUtil.lastImageView=holder.imageView;
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              editor=pref.edit();
              editor.putInt("position",position);
              editor.apply();
                if(VariableUtil.lastImageView!=null&&VariableUtil.lastImageView!=holder.imageView){
                    VariableUtil.lastImageView.setVisibility(View.GONE);
                    }
                VariableUtil.lastImageView=holder.imageView;
                holder.imageView.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.reset();
                        initMediaPlayer(position);
                        mediaPlayer.start();
                    }
                }).start();
                Intent intent=new Intent(mContext, PlayActivity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
                Toast.makeText(mContext,"start",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocalMusic.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView songTextView;
        TextView artistTextView;
        View mview;
        ImageView imageView;
        LinearLayout layout;
        public ViewHolder(View view){
            super(view);
            mview=view;
            songTextView=(TextView)view.findViewById(R.id.song_textview);
            artistTextView=(TextView)view.findViewById(R.id.artist_textview);
            imageView=(ImageView)view.findViewById(R.id.listen_image);
            layout=(LinearLayout)view.findViewById(R.id.play_list);
        }

    }
    public void initMediaPlayer(int position){

        try {
                    File file=new File(mLocalMusic.get(position).getPath());
                    mediaPlayer.setDataSource(file.getPath());
                    mediaPlayer.prepare();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

}
