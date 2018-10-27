package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import adapter.SingerFragmentAdapter;
import ecnu.ecnumusic.R;
import jiekou.FragmentEntrust;

public class SingerListFragment extends Fragment {
    public static final String TAG="SingerListFragment";
    private ViewPager singerlistViewPager;
    private Toolbar singerlistToolbar;
    private TabLayout tabLayout;
    private Handler handler=new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.singerlist_layout,container,false);
        singerlistViewPager=(ViewPager)view.findViewById(R.id.singerlist_viewpage);
        singerlistToolbar=(Toolbar)view.findViewById(R.id.singerlist_toolbar);
        tabLayout=(TabLayout)view.findViewById(R.id.singerlist_tablayout);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbar();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initViewPager();
            }
        },300);
    }

    private void initViewPager(){
        List<Fragment> fragmentList=new ArrayList<>();
        List<String> titleList=new ArrayList<>();

        fragmentList.add(new SingerNeidiFragment());
        fragmentList.add(new SingerGantaiFragment());
        fragmentList.add(new SingerAmericaFragment());
        fragmentList.add(new SingerKoreaFragment());
        fragmentList.add(new SingerJapanFragment());

        titleList.add("内地");
        titleList.add("港台");
        titleList.add("欧美");
        titleList.add("韩国");
        titleList.add("日本");
        SingerFragmentAdapter adapter=new SingerFragmentAdapter(getActivity().getSupportFragmentManager(),fragmentList,titleList);
        singerlistViewPager.setAdapter(adapter);
      //  singerlistViewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(singerlistViewPager);
    }
    private void initToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(singerlistToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("歌手榜");
        ( (AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        singerlistToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " );
               // ((FragmentEntrust)getActivity()).popFragment(SingerListFragment.TAG);
                getActivity().onBackPressed();
            }
        });
    }
}
