package fragments;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.IOException;

import ClassCollection.CdList;
import ClassCollection.Song;
import Utils.GlideImgManager;
import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.StatusBarUtil;
import Utils.Utility;
import adapter.SongDetailAdapter;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SongListDetailActivity;
import jiekou.FragmentEntrust;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Request;
import okhttp3.Response;

public class DayRecommnedFragment extends Fragment implements SongDetailAdapter.OnItemClickListener{
    public static final String TAG="DayRecommnedFragment";
    private Long content_id;
    private RecyclerView recyclerView;
    private TextView titleView;
    private ImageView title_image,title_image_background;
    private MediaPlayer mediaPlayer;
    private String title,imageUrl;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey("content_id")){
            content_id=getArguments().getLong("content_id");
            imageUrl=getArguments().getString("imageUrl");
            title=getArguments().getString("titleContent").split("：")[1];
            Log.i(TAG,imageUrl);
            Log.e(TAG,title );
        }
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.day_recommend_layout,container,false);
       recyclerView=(RecyclerView)view.findViewById(R.id.day_recommend);
        recyclerView.setNestedScrollingEnabled(false);
       titleView=(TextView)view.findViewById(R.id.title_content) ;
       title_image=(ImageView)view.findViewById(R.id.title_image) ;
       toolbar=(android.support.v7.widget.Toolbar)view.findViewById(R.id.day_toolbar);
       title_image_background=(ImageView)view.findViewById(R.id.title_imagebackground);
       mediaPlayer=new MediaPlayer();
       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolBar();
        GlideImgManager.glideLoaderCircle(getContext(),imageUrl,R.drawable.album,R.drawable.album,
                title_image);
        Glide.with(this).load(imageUrl)
                .error(R.drawable.stackblur_default)
                .bitmapTransform(new BlurTransformation(getContext(), 70, 4)).into(title_image_background);
        titleView.setText(title);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        getDayRecommend(String.valueOf(content_id));
    }
    private void getDayRecommend(String dissid){
        MusicRequestUtil.getDetailMusic(getContext(), dissid, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String textString=response.body().string();
                CdList list= Utility.handleCdlistResponse(getContext(),textString);
                SongDetailAdapter adapter=new SongDetailAdapter(list.songlist,DayRecommnedFragment.TAG);
                adapter.setOnItemClickListener(DayRecommnedFragment.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(getContext(),"dayRecommend failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Song song) {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }
        initMediaPlayer(song);
    }
    private void initMediaPlayer(Song song){
        String url="http://ws.stream.qqmusic.qq.com/C100"+song.songmid+".m4a?fromtag=0&guid=126548448";
        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e(TAG, "onPrepared: " );
                    mediaPlayer.start();
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    private void initWindows(){
        View decorView=getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    private void initToolBar(){
       ( (AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ( (AppCompatActivity)getActivity()).getSupportActionBar().setTitle("新歌推荐");
        ( (AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentEntrust)getActivity()).popFragment(DayRecommnedFragment.TAG);
            }
        });
    }
}
