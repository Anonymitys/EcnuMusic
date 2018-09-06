package adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class LocalMusicFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public LocalMusicFragmentAdapter(FragmentManager fm, List<Fragment> fragments,List<String> titles){
        super(fm);
        this.mFragments=fragments;
        this.mTitles=titles;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
     return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}