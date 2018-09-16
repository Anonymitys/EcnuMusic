package fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SingerRecyclerAdapter;
import classcollection.SingerList;
import ecnu.ecnumusic.R;
import okhttp3.Request;
import okhttp3.Response;

public class SingerGantaiFragment extends BaseFragment {
    private RecyclerView singerRecycler;
    @Override
    protected int setContentId() {
        return R.layout.singer_detail;
    }

    @Override
    protected void onLazyLoad() {
        initRequest();
    }

    @Override
    protected void initView(View view) {
        singerRecycler=(RecyclerView)view.findViewById(R.id.singer_recycler);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initRequest(){
        MusicRequestUtil.getGantaiSinger(mContext, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                SingerList list= Utility.handleSingerListResponse(responseText);
                LinearLayoutManager manager=new LinearLayoutManager(getContext());
                singerRecycler.setLayoutManager(manager);
                SingerRecyclerAdapter adapter=new SingerRecyclerAdapter(list);
                singerRecycler.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(mContext,"singer error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
