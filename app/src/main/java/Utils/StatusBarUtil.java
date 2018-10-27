package Utils;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ecnu.ecnumusic.R;

public class StatusBarUtil {
    public static int getStatusBarHeight(Activity activity){
        int result=0;
        int resourceId=activity.getResources().getIdentifier("status_bar_height","dimen","android");
        if(resourceId>0){
            result=activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public static void addStatusBarView(Activity activity,int color){
        ViewGroup rootView=(ViewGroup)activity.getWindow().findViewById(android.R.id.content);
       // ViewGroup rootView=(ViewGroup)activity.findViewById(android.R.id.content);

        ViewGroup rootLayout=(ViewGroup) rootView.getChildAt(0);
        View parentView=rootLayout.getChildAt(0);
        LinearLayout linearLayout=new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        View statusBarView=new View(activity);
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,StatusBarUtil.getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(color);
        linearLayout.addView(statusBarView,lp);
        DrawerLayout drawer=(DrawerLayout)parentView;
        View content=activity.findViewById(R.id.root_layout);
        drawer.removeView(content);
        linearLayout.addView(content,content.getLayoutParams());
        drawer.addView(linearLayout,0);
    }
    public static void fullScreen(Activity activity){
        View decorView=activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
       activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}
