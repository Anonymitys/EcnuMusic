package ecnu.ecnumusic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.RankAdapter;
import adapter.RankGlobalAdapter;
import okhttp3.Request;
import okhttp3.Response;
import shouyeclass.rank.Rank;

public class RankActivity extends BaseActivity implements RankGlobalAdapter.OnGlobalitemClickListener, RankAdapter.OnRankItemClickListener {
    private TextView oneText, twoText;
    private RecyclerView oneRecycler, twoRecycler;
    public static final String TAG = "RankFragment";
    private Toolbar toolbar;
    private View loadingView;
    private AnimationDrawable mAnimationDrawable;
    private android.os.Handler handler = new android.os.Handler();


    public static void getLaunch(Activity activity) {
        Intent intent = new Intent(activity, RankActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        oneText = findViewById(R.id.groupname_one);
        twoText = findViewById(R.id.groupname_two);
        oneRecycler = findViewById(R.id.groupone_recycler);
        oneRecycler.setNestedScrollingEnabled(false);
        twoRecycler = findViewById(R.id.grouptwo_recycler);
        twoRecycler.setNestedScrollingEnabled(false);
        toolbar = findViewById(R.id.rank_toorbar);
        loadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
        ImageView img = loadingView.findViewById(R.id.img_progress);
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        initToolBar();
        showLoading();
        initRequest();
    }

    private void initRequest() {
        MusicRequestUtil.getRank(this, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText = response.body().string();
                List<Rank> ranks = Utility.handleRankResponse(responseText);
                oneText.setText(ranks.get(0).groupname);
                twoText.setText(ranks.get(1).groupname);
                LinearLayoutManager manager = new LinearLayoutManager(RankActivity.this);
                oneRecycler.setLayoutManager(manager);
                RankAdapter adapter = new RankAdapter(ranks);
                adapter.setRankItemClickListener(RankActivity.this);
                oneRecycler.setAdapter(adapter);

                GridLayoutManager layoutManager = new GridLayoutManager(RankActivity.this, 3);
                twoRecycler.setLayoutManager(layoutManager);
                RankGlobalAdapter globalAdapter = new RankGlobalAdapter(ranks);
                globalAdapter.setOnGlobalitemClickListener(RankActivity.this);
                twoRecycler.setAdapter(globalAdapter);
                showContent();
            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e(TAG, "onError: ");
            }
        });
    }

    @Override
    public void onRankItemClick(int topID, String icon, String updateTime) {
        Intent intent = new Intent(this, RankSongActivity.class);
        intent.putExtra("topID", topID);
        intent.putExtra("icon", icon);
        intent.putExtra("listName", updateTime);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    @Override
    public void onGlobalItemClick(int topID, String icon, String updateTime) {
        Intent intent = new Intent(this, RankSongActivity.class);
        intent.putExtra("topID", topID);
        intent.putExtra("icon", icon);
        intent.putExtra("listName", updateTime);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("排行榜");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }

    public void showLoading() {
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
    }

    public void showContent() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
    }

}