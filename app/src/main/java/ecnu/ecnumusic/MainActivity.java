package ecnu.ecnumusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import Utils.StatusBarUtil;
import adapter.FragmentAdapter;
import classcollection.Music;
import classcollection.Song;
import fragments.LocalFragment;
import fragments.MainFragment;
import fragments.VideoFragment;
import fragments.musicFragment;
import jiekou.FragmentEntrust;
import service.MusicService;
import service.OnPlayerEventListener;

public class MainActivity extends BaseActivity implements FragmentEntrust, OnPlayerEventListener {

    public DrawerLayout layout;
    private MainFragment mainFragment;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.fullScreen(this);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.drawer_layout);
        mainFragment = new MainFragment();
        addMainFragment();
        requestPermission(this);
    }

    private void requestPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;

                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("必须同意权限才能使用软件");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
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

    private void addMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment, mainFragment);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void pushFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_down_out, R.anim.push_up_in, R.anim.push_down_out);
        transaction.add(R.id.root_layout, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void popFragment(String tag) {
        getSupportFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public boolean usePlaybarView() {
        return false;
    }

    @Override
    public void connection(IBinder service) {
        mainFragment.connection(service);
    }
}
