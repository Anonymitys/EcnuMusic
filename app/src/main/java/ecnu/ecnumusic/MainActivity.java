package ecnu.ecnumusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Utils.StatusBarUtil;
import adapter.FragmentAdapter;
import classcollection.Music;
import classcollection.Song;
import fragments.PlaybarFragment;
import jiekou.FragmentEntrust;
import service.MusicService;
import service.OnPlayerEventListener;
import widget.CircleView;
import fragments.LocalFragment;
import fragments.VideoFragment;
import fragments.musicFragment;

public class MainActivity extends BaseActivity implements FragmentEntrust, OnPlayerEventListener{
    private ImageView searchButton,homeButton;
    private ViewPager pager;
    private TabLayout tabLayout;
    private DrawerLayout layout;

    private MusicService.MusicBinder musicBinder;
    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " );
         StatusBarUtil.fullScreen(this);
        // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        StatusBarUtil.addStatusBarView(this,getResources().getColor(R.color.colorPrimary));
        layout=(DrawerLayout)findViewById(R.id.drawer_layout);
       searchButton=(ImageView)findViewById(R.id.search_button) ;
       homeButton=(ImageView)findViewById(R.id.home_button);
       searchButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        pager=(ViewPager)findViewById(R.id.view_pager);
        tabLayout=(TabLayout)findViewById(R.id.tab_layout);

        initViewPager(pager,tabLayout);
      //  setListener();
        requestPermission(this);
    }



   @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.search_button:
              Intent intent=new Intent(MainActivity.this,SearchActivity.class);
              startActivity(intent);
                break;
            case R.id.home_button:
                layout.openDrawer(Gravity.START);
                break;

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );

    }


private void requestPermission(Activity activity){
    if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    break;

                }else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setMessage("必须同意权限才能使用软件");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent=new Intent();
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package",getPackageName(), null));
                            MainActivity.this.startActivity(intent);
                        }
                    });
                    dialog.show();
                }
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViewPager(ViewPager viewPager, TabLayout layout){


        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new LocalFragment());
        fragments.add(new musicFragment());
        fragments.add(new VideoFragment());
        final FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        layout.setupWithViewPager(viewPager);
        layout.getTabAt(0).setCustomView(adapter.getTabView(this,R.drawable.local));
        layout.getTabAt(1).setCustomView(adapter.getTabView(this,R.drawable.music));
        layout.getTabAt(2).setCustomView(adapter.getTabView(this,R.drawable.video));
        viewPager.setCurrentItem(1);

    }

    @Override
    public void connection(IBinder service) {
       super.connection(service);
        Log.e(TAG, "connection: " );
        musicBinder=(MusicService.MusicBinder)service;
        musicBinder.getService().setOnPlayEventListener(this);


    }

    @Override
    public void pushFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in,R.anim.push_down_out,R.anim.push_up_in, R.anim.push_down_out);
        transaction.add(R.id.root_layout,fragment,tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void popFragment(String tag) {
        getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public MusicService.MusicBinder getMusicBinder() {
        return musicBinder;
    }

    @Override
    public void onChange(Song song, Music music) {
        super.onChange(song,music);
    }

    @Override
    public void onPlayerStart() {
        super.onPlayerStart();
    }

    @Override
    public void onPublish(int progress) {

    }
}
