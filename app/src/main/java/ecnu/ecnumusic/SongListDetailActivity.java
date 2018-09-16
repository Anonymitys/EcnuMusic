package ecnu.ecnumusic;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.transition.ArcMotion;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.List;

import classcollection.CdList;
import classcollection.Singer;
import classcollection.Song;
import Utils.GlideImgManager;
import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.StatusBarUtil;
import Utils.Utility;
import adapter.SongDetailAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.PlaybarFragment;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Request;
import okhttp3.Response;
import other.CustomChangeBounds;
import service.MusicService;
import service.OnPlayerEventListener;
import shouyeclass.Album;
import shouyeclass.PlayList;
import shouyeclass.Vhot;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class SongListDetailActivity extends BaseActivity implements SongDetailAdapter.OnItemClickListener, OnPlayerEventListener{

    //private RecommendList recommendList;
    private Vhot vhot;
    private Album album;
    private PlayList playList;
    private RecyclerView recyclerView;
    private ImageView imageView,head_background,title_imagebg,singerMore;
    private TextView title_textview,author_name,public_time;
    private CircleImageView author_image;
    private Toolbar toolbar;
    private ScrollView scrollView;
    private int slidingDistance;
    private Handler handler=new Handler();
    private static final String TAG="SonglistDetailActivity";

    private String tag;
   private MusicService.MusicBinder musicBinder;
 //  private PlaybarFragment playbarFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlistdetail);
        initWindows();
        Intent intent=getIntent();
        tag=(String)intent.getBundleExtra("song").getString("tag");
        if ("playlist".equals(tag)){
            playList=(PlayList)intent.getBundleExtra("song").getSerializable("playlist");
        }else {
            album=(Album)intent.getBundleExtra("song").getSerializable("album") ;
            vhot=(Vhot) intent.getBundleExtra("song").getSerializable("recommendlist");
        }


        recyclerView=(RecyclerView)findViewById(R.id.songlistdetail_recycler);

        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);
        imageView=(ImageView)findViewById(R.id.title_imageview);
        title_textview=(TextView)findViewById(R.id.title_textview);
        author_name=(TextView)findViewById(R.id.author_name);
        public_time=(TextView)findViewById(R.id.public_time) ;
        singerMore=(ImageView) findViewById(R.id.singer_more);
        author_image=(CircleImageView)findViewById(R.id.author_image);
        toolbar=(Toolbar)findViewById(R.id.title_toolbar);
        head_background=(ImageView)findViewById(R.id.head_background);
        scrollView=(ScrollView)findViewById(R.id.scroll_view);
        title_imagebg=(ImageView)findViewById(R.id.title_imagebg);



         initToolbar();
        setMotion(imageView,false);
        if ("songlist".equals(tag)){
           setHeadInfo(vhot.cover,vhot.title,vhot.username);
        }
        if ("playlist".equals(tag)){
            setHeadInfo(playList.imgurl,playList.dissname,playList.creator.name);
        }
        if ("albumlist".equals(tag)){
            author_image.setVisibility(View.GONE);
            public_time.setVisibility(View.VISIBLE);
            singerMore.setVisibility(View.VISIBLE);
            public_time.setText("发行时间:"+album.public_time);
            String url="https://y.gtimg.cn/music/photo_new/T002R300x300M000"+album.album_mid+".jpg?max_age=2592000";
            String singername="歌手:"+getSingerName(album.singerList);
            setHeadInfo(url,album.album_name,singername);
        }

        initScrollViewListener();
        handler.postDelayed(requestRunable,230);


    }
    private void setHeadInfo(String imageUrl,String titleText,String subtitleText){
        GlideImgManager.glideLoaderCircle(this,imageUrl,R.drawable.album,R.drawable.album,
                imageView);
        title_textview.setText(Html.fromHtml(titleText,FROM_HTML_MODE_COMPACT));
        author_name.setText(subtitleText);
        setImgHeaderBg(imageUrl);
    }

   private void getAlbum(String albummid){
        MusicRequestUtil.getAlbumMusic(this, albummid, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String textString =response.body().string();
                List<Song> songList=Utility.handleAlbumResponse(textString);
                SongDetailAdapter adapter=new SongDetailAdapter(songList,SongListDetailActivity.TAG);
                adapter.setOnItemClickListener(SongListDetailActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(SongListDetailActivity.this,"failed",Toast.LENGTH_SHORT).show();
            }
        });
   }

    private void getDetailMusic(String dissid){
        MusicRequestUtil.getDetailMusic(this, dissid, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String textString=response.body().string();
                CdList list= Utility.handleCdlistResponse(SongListDetailActivity.this,textString);
                SongDetailAdapter adapter=new SongDetailAdapter(list.songlist,SongListDetailActivity.TAG);
                adapter.setOnItemClickListener(SongListDetailActivity.this);
                recyclerView.setAdapter(adapter);
                Glide.with(SongListDetailActivity.this).load(list.headurl).into(author_image);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(SongListDetailActivity.this,"failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void setMotion(ImageView imageView, boolean isShow) {
        if (!isShow) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();
            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(imageView);
            changeBounds.setDuration(300);
            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }
    Runnable requestRunable=new Runnable() {
        @Override
        public void run() {
           // initRequest(recommendList.dissid);
            if ("songlist".equals(tag)){
                getDetailMusic(String.valueOf(vhot.content_id));
            }else if ("albumlist".equals(tag)){
                getAlbum(album.album_mid);
            }else if ("playlist".equals(tag)){
                getDetailMusic(playList.dissid);
            }

        }
    };
    private void setImgHeaderBg(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {

            // 高斯模糊背景 原来 参数：12,5  23,4
            Glide.with(this).load(imgUrl)
                    .error(R.drawable.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this, 23, 4)).into(head_background);
            Glide.with(this).load(imgUrl).error(R.drawable.stackblur_default)
                    .bitmapTransform(new BlurTransformation(this,23,4)).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    title_imagebg.setImageAlpha(0);
                    return false;
                }
            }).into(title_imagebg);

        int headbgHeight=toolbar.getLayoutParams().height+StatusBarUtil.getStatusBarHeight(this);
        ViewGroup.LayoutParams params=(ViewGroup.LayoutParams)title_imagebg.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams=(ViewGroup.MarginLayoutParams)title_imagebg.getLayoutParams();
        slidingDistance=params.height-headbgHeight;
        marginLayoutParams.setMargins(0,-slidingDistance,0,0);
        }
    }

    private void initWindows(){
    View decorView=getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    getWindow().setStatusBarColor(Color.TRANSPARENT);
}

    private void initScrollViewListener() {
       scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
           @Override
           public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
               scrollChangeHeader(scrollY);
           }
       });
    }

    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);
        if (alpha>0.3f){
            if ("songlist".equals(tag)){
                getSupportActionBar().setTitle(Html.fromHtml(vhot.title,FROM_HTML_MODE_COMPACT));
            }else if ("albumlist".equals(tag)){
                getSupportActionBar().setTitle(Html.fromHtml(album.album_name,FROM_HTML_MODE_COMPACT));
            }else if ("playlist".equals(tag)){
                getSupportActionBar().setTitle(Html.fromHtml(playList.dissname,FROM_HTML_MODE_COMPACT));
            }

        }else {

            if("albumlist".equals(tag)){
                getSupportActionBar().setTitle("专辑");
            }else {
                getSupportActionBar().setTitle("歌单");
            }

        }
        if (scrolledY <= slidingDistance) {
            // title部分的渐变
           title_imagebg.setImageAlpha ((int)(alpha * 255));
        } else {
           title_imagebg.setImageAlpha(255);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            if ("songlist".equals(tag)){
                getSupportActionBar().setTitle("歌单");
                getSupportActionBar().setSubtitle("编辑推荐");
            }else if ("albumlist".equals(tag)){
                getSupportActionBar().setTitle("专辑");
            }else if ("playlist".equals(tag)){
                getSupportActionBar().setTitle("歌单");
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        ViewGroup.MarginLayoutParams marginLayoutParams=(ViewGroup.MarginLayoutParams)toolbar.getLayoutParams();
        marginLayoutParams.setMargins(0, StatusBarUtil.getStatusBarHeight(this),0,0);
    }






    @Override
    public void onItemClick(int position, List<Song> songs) {
        Log.e(TAG, "onItemClick: " );
       // String url="http://ws.stream.qqmusic.qq.com/C100"+song.songmid+".m4a?fromtag=0&guid=126548448";
        musicBinder.playFromURL(position,songs);
    }

    @Override
    public void connection(IBinder service) {
        super.connection(service);
       musicBinder=(MusicService.MusicBinder)service;
       musicBinder.getService().setOnPlayEventListener(this);


    }


   private String getSingerName(List<Singer> singers){
       String text="";
       for (Singer singer:singers){
           text=text+singer.singer_name+"/";
       }
       text=text.substring(0,text.length()-1);
       return text;
   }

    @Override
    public void onChange(Song song) {
        super.onChange(song);
    }

    @Override
    public void onPlayerStart() {
        super.onPlayerStart();
    }
}
