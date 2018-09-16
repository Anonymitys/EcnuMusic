package fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;
    protected View rootView;
    protected boolean mIsVisible=false;
    protected boolean mIsPrepared=false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity=getActivity();
        mContext=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(setContentId(),container,false);
        initView(rootView);
        mIsPrepared=true;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible=isVisibleToUser;
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    protected void onVisible(){
        if (mIsPrepared && mIsVisible) {
            onLazyLoad();
        }
    }

    protected  void onLazyLoad(){

    }

    protected void initView(View view){

    }
    protected void initData(){

    }
    protected abstract int setContentId();
}
