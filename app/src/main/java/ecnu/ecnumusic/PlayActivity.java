package ecnu.ecnumusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fragments.SongFragment;
import widget.DiscView;

public class PlayActivity extends AppCompatActivity implements DiscView.IPlayInfo{

    private DiscView discView;
    private android.support.v7.widget.Toolbar toolbar;
    private BackgroundAnimationLinearLayout mRootLayout;
    private int position;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        position=intent.getIntExtra("position",-1);
        setContentView(R.layout.play_activity);

        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.play_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle(SongFragment.mLocalMusics.get(position).getTitle());
            getSupportActionBar().setSubtitle(SongFragment.mLocalMusics.get(position).getArtist());
        }
        discView=(DiscView) findViewById(R.id.disc_view);
        mRootLayout=(BackgroundAnimationLinearLayout)findViewById(R.id.play_rootlayout);
        discView.setMusicDataList(SongFragment.mLocalMusics);
        discView.setPlayInfoListener(this);
        initWindows();
        initViewPager(position);
    }
    private void initWindows(){
        View decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);



    }
    private void initViewPager(int position){
        viewPager=(ViewPager)discView.findViewById(R.id.playview_pager);
            if(position!=0){
                viewPager.setCurrentItem(position);
            }


    }

    @Override
    public void onMusicInfoChanged(String musicName, String musicAuthor) {

            getSupportActionBar().setTitle(musicName);
            getSupportActionBar().setSubtitle(musicAuthor);

    }

    @Override
    public void onMusicChanged(DiscView.MusicChangedStatus musicChangedStatus) {

    }

    @Override
    public void onMusicPicChanged(Bitmap musicPic) {
        try2UpdateMusicPicBackground(musicPic);

    }

    private void try2UpdateMusicPicBackground(final Bitmap musicPic) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Drawable foregroundDrawable = mRootLayout.getForegroundDrawable(PlayActivity.this,musicPic);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                             mRootLayout.setForeground(foregroundDrawable);
                            mRootLayout.beginAnimation();
                        }
                    });
                }
            }).start();
        }

}
