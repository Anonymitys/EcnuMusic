package fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.RankAdapter;
import adapter.RankGlobalAdapter;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.RankSongActivity;
import okhttp3.Request;
import okhttp3.Response;
import shouyeclass.rank.Rank;

public class RankFragment extends BaseFragment implements RankGlobalAdapter.OnGlobalitemClickListener,RankAdapter.OnRankItemClickListener{
    private TextView oneText,twoText;
    private RecyclerView oneRecycler,twoRecycler;
    public static final String TAG="RankFragment";
    private Toolbar toolbar;
    private android.os.Handler handler=new android.os.Handler();
    @Override
    protected int setContentId() {
        return R.layout.rankfragment_layout;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        oneText=(TextView)view.findViewById(R.id.groupname_one);
        twoText=(TextView)view.findViewById(R.id.groupname_two);
        oneRecycler=(RecyclerView)view.findViewById(R.id.groupone_recycler);
        oneRecycler.setNestedScrollingEnabled(false);
        twoRecycler=(RecyclerView)view.findViewById(R.id.grouptwo_recycler);
        twoRecycler.setNestedScrollingEnabled(false);
        toolbar=(Toolbar)view.findViewById(R.id.rank_toorbar);
    }

    @Override
    protected void initData() {
        super.initData();
        initToorBar();
        handler.postDelayed(runnable,300);
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            initRequest();
        }
    };
    private void initRequest(){
        MusicRequestUtil.getRank(mContext, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                List<Rank> ranks= Utility.handleRankResponse(responseText);
                oneText.setText(ranks.get(0).groupname);
                twoText.setText(ranks.get(1).groupname);
                LinearLayoutManager manager=new LinearLayoutManager(mContext);
                oneRecycler.setLayoutManager(manager);
                RankAdapter adapter=new RankAdapter(ranks);
                adapter.setRankItemClickListener(RankFragment.this);
                oneRecycler.setAdapter(adapter);

                GridLayoutManager layoutManager=new GridLayoutManager(mContext,3);
                twoRecycler.setLayoutManager(layoutManager);
                RankGlobalAdapter globalAdapter=new RankGlobalAdapter(ranks);
                globalAdapter.setOnGlobalitemClickListener(RankFragment.this);
                twoRecycler.setAdapter(globalAdapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e(TAG, "onError: " );
            }
        });
    }

    @Override
    public void onGlobalItemClick(int topID,String icon,String rankName) {
        Log.e(TAG, "onGlobalItemClick: "+topID );
        Intent intent=new Intent(mActivity, RankSongActivity.class);
        intent.putExtra("topID",topID);
        intent.putExtra("icon",icon);
        intent.putExtra("listName",rankName);
        startActivity(intent);
    }

    @Override
    public void onRankItemClick(int topID,String icon,String rankName) {
        Log.e(TAG, "onRankItemClick: "+topID );
        Intent intent=new Intent(mActivity, RankSongActivity.class);
        intent.putExtra("topID",topID);
        intent.putExtra("icon",icon);
        intent.putExtra("listName",rankName);
        startActivity(intent);

    }
    private void initToorBar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("排行榜");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}
