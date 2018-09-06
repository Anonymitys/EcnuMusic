package ecnu.ecnumusic;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ClassCollection.Music;
import adapter.LocalMusicFragmentAdapter;
import fragments.AlbumFragment;
import fragments.FileFragment;
import fragments.SingerFragment;
import fragments.SongFragment;

public class LocalMusic extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music);
        mTabLayout=(TabLayout)findViewById(R.id.localmusic_tablayout);
        mViewPager=(ViewPager)findViewById(R.id.localmusic_viewpager);
        initPagerView();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.local_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
           actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

       }
        return super.onMenuOpened(featureId, menu);
    }

    private void initPagerView(){
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new SongFragment());
        fragments.add(new SingerFragment());
        fragments.add(new AlbumFragment());
        fragments.add(new FileFragment());
        List<String> titles=new ArrayList<>();
        titles.add("单曲");
        titles.add("歌手");
        titles.add("专辑");
        titles.add("文件夹");
        LocalMusicFragmentAdapter adapter=new LocalMusicFragmentAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


}

