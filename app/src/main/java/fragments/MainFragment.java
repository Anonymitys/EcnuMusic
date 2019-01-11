package fragments;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Utils.GlideImgManager;
import Utils.MusicUtil;
import Utils.StatusBarUtil;
import adapter.DialogAdapter;
import adapter.FragmentAdapter;
import classcollection.Music;
import classcollection.Song;
import ecnu.ecnumusic.MainActivity;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SearchActivity;
import service.MusicService;
import service.OnPlayerEventListener;
import widget.CircleView;

public class MainFragment extends BaseFragment implements View.OnClickListener, DialogAdapter.OnItemClickListener, OnPlayerEventListener {
    private ImageView searchButton, homeButton;
    private ViewPager pager;
    private TabLayout tabLayout;
    private View statusbarView;

    private View playbarView;
    private ImageView playPause, playList, songImage;
    private TextView songName, songLyrics;
    private CircleView circleView;
    private Handler handler = new Handler();
    private DialogAdapter adapter;
    private MusicService.MusicBinder musicBinder;

    @Override
    protected int setContentId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        searchButton = view.findViewById(R.id.search_button);
        homeButton = view.findViewById(R.id.home_button);
        searchButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        pager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        statusbarView = view.findViewById(R.id.empty_view);
        playbarView = view.findViewById(R.id.playbar_view);
        playPause = view.findViewById(R.id.play_pause_bar);
        playList = view.findViewById(R.id.play_list_bar);
        songImage = view.findViewById(R.id.play_image_bar);
        songName = view.findViewById(R.id.playbar_song_name);
        songLyrics = view.findViewById(R.id.playbar_lyrics);
        circleView = view.findViewById(R.id.circle_view);
        playbarView.setVisibility(View.GONE);
        playbarView.setOnClickListener(this);
        playPause.setOnClickListener(this);
        playList.setOnClickListener(this);
        addStatusbarView();
        initViewPager(pager, tabLayout);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                Intent intent = new Intent(mActivity, SearchActivity.class);
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
                break;
            case R.id.home_button:
                ((MainActivity) mActivity).layout.openDrawer(Gravity.START);
                break;
            case R.id.play_pause_bar:
                onPlayPauseClick();
                break;
            case R.id.play_list_bar:
                showBottomDialog();
                break;

        }
    }

    private void initViewPager(ViewPager viewPager, TabLayout layout) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalFragment());
        fragments.add(new musicFragment());
        fragments.add(new VideoFragment());
        final FragmentAdapter adapter = new FragmentAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        layout.setupWithViewPager(viewPager);
        layout.getTabAt(0).setCustomView(adapter.getTabView(mContext, R.drawable.local));
        layout.getTabAt(1).setCustomView(adapter.getTabView(mContext, R.drawable.music));
        layout.getTabAt(2).setCustomView(adapter.getTabView(mContext, R.drawable.video));
        viewPager.setCurrentItem(1);

    }

    private void addStatusbarView() {
        ViewGroup.LayoutParams params = statusbarView.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(mActivity);
    }

    public void connection(IBinder service) {
        musicBinder = (MusicService.MusicBinder) service;
        musicBinder.getService().setOnPlayEventListener(this);
        if (musicBinder.getMusicList().size() > 0) {
            playbarView.setVisibility(View.VISIBLE);
            Song song = musicBinder.getMusicList().get(musicBinder.getCurrentPosition());
            String url = "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + song.albummid + ".jpg?max_age=2592000";
            songName.setText(song.songname);
            GlideImgManager.glideLoaderCircle(mContext, url, R.drawable.album, R.drawable.album,
                    songImage);
            if (musicBinder.getMediaPlayer().isPlaying()) {
                setPlay();
            } else {
                setPause();
            }

        }
    }

    public void onPlayPauseClick() {
        if (musicBinder.getMediaPlayer().isPlaying()) {
            musicBinder.playPause();
            setPause();
        } else {

            musicBinder.playStart();
            setPlay();

        }
    }

    public void setPlay() {
        playPause.setBackground(mActivity.getDrawable(R.drawable.play));
        circleView.setVisibility(View.VISIBLE);
        circleView.setDuration(musicBinder.getMediaPlayer().getDuration() / 1000);
        handler.post(progressBarRunable);
    }

    public void setPause() {
        playPause.setBackground(mActivity.getDrawable(R.drawable.pause));
        circleView.setVisibility(View.VISIBLE);
        circleView.setDuration(musicBinder.getMediaPlayer().getDuration() / 1000);
        circleView.setCurrentAngle(musicBinder.getMediaPlayer().getCurrentPosition() / 1000);

        circleView.invalidate();
    }

    Runnable progressBarRunable = new Runnable() {
        @Override
        public void run() {
            circleView.setCurrentAngle(musicBinder.getMediaPlayer().getCurrentPosition() / 1000);
            circleView.invalidate();
            if (musicBinder.getMediaPlayer().isPlaying()) {
                handler.postDelayed(progressBarRunable, 800);
            }
        }
    };

    public void showBottomDialog() {
        Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_musiclist, null);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.dialog_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity.getBaseContext());
        recyclerView.setLayoutManager(manager);
        adapter = new DialogAdapter(musicBinder.getMusicList(), musicBinder.getCurrentPosition());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.6);
        view.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.show();
    }

    @Override
    public void onItemClick(int position, Song song) {
        musicBinder.setCurrentPosition(position);
        // musicBinder.playFromURI(song);
        musicBinder.playFromvKey(song);
    }

    @Override
    public void onChange(Song song, Music music) {
        if (song != null) {
            String url = "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + song.albummid + ".jpg?max_age=2592000";
            songName.setText(song.songname);
            GlideImgManager.glideLoaderCircle(mContext, url, R.drawable.album, R.drawable.album,
                    songImage);

        } else {
            songName.setText(music.getTitle());
            GlideImgManager.glideLoaderCircle(mContext, MusicUtil.getAlbumArt(mContext, music.getAlbum_id()), R.drawable.album, R.drawable.album,
                    songImage);
        }
        playbarView.setVisibility(View.VISIBLE);
        playPause.setBackground(mActivity.getDrawable(R.drawable.play));
        circleView.setVisibility(View.VISIBLE);
        circleView.setCurrentAngle(0f);

    }

    @Override
    public void onPlayerStart() {
        setPlay();
    }

}
