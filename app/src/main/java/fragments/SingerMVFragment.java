package fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.MVAdapter;
import classcollection.mv.MVDetail;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SingerDetailActivity;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;

public class SingerMVFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private int count = 0;
    private static final String TAG = "SingerMVFragment";
    private MVAdapter adapter;
    private List<MVDetail> mvDetails;
    private String singerMid;

    @Override
    protected void initView(View view) {
        super.initView(view);
        recyclerView = view.findViewById(R.id.singer_recycler);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                count++;
                loadingData(count);
            }
        });
    }

    @Override
    protected void initData() {
        singerMid=((SingerDetailActivity) getActivity()).getSingerMid();
        initRequest();
    }

    @Override
    protected int setContentId() {
        return R.layout.singer_detail;
    }

    private void initRequest() {
        MusicRequestUtil.getMv(getContext(), singerMid, 0, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                mvDetails = Utility.handleMVResponse(str);
                adapter = new MVAdapter(mvDetails);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {

            }
        });

    }
    private void loadingData(int count){
        MusicRequestUtil.getMv(getContext(), singerMid, count, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                List<MVDetail> mvDetail = Utility.handleMVResponse(str);
                if (count==0){
                    mvDetails.clear();
                }
                mvDetails.addAll(mvDetail);
                adapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
                if (mvDetail.size()<100){
                    recyclerView.noMoreLoading();
                }
            }

            @Override
            public void onError(Request request, Exception ex) {

            }
        });
    }
}
