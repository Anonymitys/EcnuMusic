package ecnu.ecnumusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SingerRecyclerAdapter;
import adapter.TextAdapter;
import classcollection.singer.SingerDetail;
import classcollection.singer.SingerList;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;

public class SingerActivity extends BaseActivity implements TextAdapter.OnTagClickListener {
    private RecyclerView rvArea, rvSex, rvGenre;
    private XRecyclerView rvSinger;
    private int sex = -100, genre = -100, area = -100, count = 0;
    private static final int TAG_SEX = 1000, TAG_AREA = 1001, TAG_GENRE = 1002;
    private List<SingerDetail> singerDetails;
    private SingerRecyclerAdapter singerAdapter;
    private Toolbar toolbar;
    private static final String TAG = "SingerActivity";

    public static void getLaunch(Activity activity) {
        Intent intent = new Intent(activity, SingerActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);
        rvArea = findViewById(R.id.rv_area);
        rvSex = findViewById(R.id.rv_sex);
        rvGenre = findViewById(R.id.rv_genre);
        rvSinger = findViewById(R.id.rv_singer);
        toolbar = findViewById(R.id.singer_toolbar);
        initToolBar();
        initRecyclerView();
        initRequest();

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("歌手");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager areaManager = new LinearLayoutManager(this);
        areaManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvArea.setLayoutManager(areaManager);
        LinearLayoutManager sexManager = new LinearLayoutManager(this);
        sexManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSex.setLayoutManager(sexManager);
        LinearLayoutManager genreManager = new LinearLayoutManager(this);
        genreManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGenre.setLayoutManager(genreManager);
        rvSinger.setLayoutManager(new LinearLayoutManager(this));
        rvSinger.clearHeader();
        rvSinger.setPullRefreshEnabled(false);
        rvSinger.setLoadingListener(new XRecyclerView.LoadingListener() {
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

    private void initRequest() {
        MusicRequestUtil.getSinger(this, area, genre, sex, count, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                SingerList singerList = Utility.handleSingerListResponse(str);
                singerDetails = singerList.singerList;
                Log.e(TAG, "onResponse: " + singerList.singerList.get(0).singer_name);
                TextAdapter area = new TextAdapter(TAG_AREA, singerList.singerTag.areaList);
                area.setOnTagClickListener(SingerActivity.this);
                rvArea.setAdapter(area);
                TextAdapter genre = new TextAdapter(TAG_GENRE, singerList.singerTag.genreList);
                genre.setOnTagClickListener(SingerActivity.this);
                rvGenre.setAdapter(genre);
                TextAdapter sex = new TextAdapter(TAG_SEX, singerList.singerTag.sexList);
                sex.setOnTagClickListener(SingerActivity.this);
                rvSex.setAdapter(sex);
                singerAdapter = new SingerRecyclerAdapter(singerDetails);
                rvSinger.setAdapter(singerAdapter);
            }

            @Override
            public void onError(Request request, Exception ex) {

            }
        });
    }

    public void loadingData(final int count) {
        Log.e("count", "loadingData: "+count );
        MusicRequestUtil.getSinger(this, area, genre, sex, count, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                SingerList singerList = Utility.handleSingerListResponse(str);
                Log.e(TAG, "onResponse: "+singerList.singerList.size() );
                if (count == 0) {
                    rvSinger.reset();
                    singerDetails.clear();
                    rvSinger.scrollToPosition(0);
                }
                singerDetails.addAll(singerList.singerList);
                singerAdapter.notifyDataSetChanged();
                rvSinger.refreshComplete();
                if (count == 5) {
                    rvSinger.noMoreLoading();
                }

            }

            @Override
            public void onError(Request request, Exception ex) {

            }
        });
    }

    @Override
    public void onItemClick(int tag, int id) {
        if (tag == TAG_AREA && area != id) {
            area = id;
            Log.e(TAG, "onItemClick:area "+id );
            count = 0;
            loadingData(0);
        }
        if (tag == TAG_GENRE && genre != id) {
            count = 0;
            genre = id;
            Log.e(TAG, "onItemClick:genre "+id );
            loadingData(0);
        }
        if (tag == TAG_SEX && sex != id) {
            count = 0;
            sex = id;
            Log.e(TAG, "onItemClick:sex "+id );
            loadingData(0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }
}
