package jiekou;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by root on 17-4-14.
 */
public interface FragmentEntrust {
    public void pushFragment(Fragment fragment, String tag);
    public void popFragment(String tag);
}
