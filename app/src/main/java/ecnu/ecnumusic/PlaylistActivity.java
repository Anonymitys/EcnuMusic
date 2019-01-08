package ecnu.ecnumusic;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.PlaylistAdapter;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;
import shouyeclass.PlayList;

public class PlaylistActivity extends BaseActivity implements PlaylistAdapter.OnItemClickListener {
    public static final String TAG = "PlaylistFragment";
    private XRecyclerView playlistRecycler;
    private Toolbar toolbar;
    private int isLoading = 0;
    private List<PlayList> playLists;
    private PlaylistAdapter adapter;
    private TextView category, hotText, newText;
    private static final int REQUEST_SORT = 1000;
    private View loadingView;
    private AnimationDrawable mAnimationDrawable;

    private int categoryId = 10000000, sortId = 3, lastSortId = 3;

    public static void getLaunch(Activity activity) {
        Intent intent = new Intent(activity, PlaylistActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        playlistRecycler = findViewById(R.id.playlist_recycler);
        playlistRecycler.setNestedScrollingEnabled(false);
        loadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
        ImageView img = loadingView.findViewById(R.id.img_progress);
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        toolbar = findViewById(R.id.playlist_toolbar);
        initToolBar();
        initRecycler();
        showLoading();
        getPlaylistRequest(categoryId, sortId);
    }

    private void getPlaylistRequest(int categoryId, int sortId) {
        MusicRequestUtil.getPlayList(0, this, categoryId, sortId, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String text = response.body().string();
                playLists = Utility.handlePlayListResponse(text);
                adapter = new PlaylistAdapter(playLists);
                adapter.setOnItemClicklistener(PlaylistActivity.this);
                playlistRecycler.setAdapter(adapter);
                showContent();
                playlistRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
                    @Override
                    public void onRefresh() {

                    }

                    @Override
                    public void onLoadMore() {
                        isLoading++;
                        loadData(isLoading);

                    }
                });

            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(PlaylistActivity.this, "playlist failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("歌单");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void initRecycler() {
        playlistRecycler.setPullRefreshEnabled(false);
        playlistRecycler.clearHeader();
        View headView = View.inflate(this, R.layout.head_item_playlist, null);
        category = headView.findViewById(R.id.category_textview);
        hotText = headView.findViewById(R.id.hot_text);
        hotText.setSelected(true);
        hotText.setTextColor(Color.WHITE);
        newText = headView.findViewById(R.id.new_text);
        hotText.setOnClickListener(this);
        newText.setOnClickListener(this);
        category.setOnClickListener(this);
        playlistRecycler.addHeaderView(headView);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        playlistRecycler.setLayoutManager(manager);
    }

    private void loadData(final int count) {
        Log.e(TAG, "loadData: ");
        MusicRequestUtil.getPlayList(count, this, categoryId, sortId, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String text = response.body().string();
                List<PlayList> playList = Utility.handlePlayListResponse(text);
                if (count == 0) {
                    playLists.clear();
                }
                playLists.addAll(playList);
                Log.e(TAG, "onResponse: " + playList.get(0).dissname);
                adapter.notifyDataSetChanged();
                playlistRecycler.refreshComplete();
                if (playList.size()<80)
                    playlistRecycler.noMoreLoading();
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(PlaylistActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(ImageView imageView, PlayList playList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("playlist", playList);
        bundle.putString("tag", "playlist");
        Intent intent = new Intent(this, SongListDetailActivity.class);
        intent.putExtra("song", bundle);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, "sharedView");//与xml文件对应
        startActivity(intent, options.toBundle());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.category_textview) {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivityForResult(intent, REQUEST_SORT);
            overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
        }
        if (v.getId() == R.id.hot_text) {
            newText.setSelected(false);
            newText.setTextColor(getColor(R.color.colorPrimaryDark));
            hotText.setSelected(true);
            hotText.setTextColor(Color.WHITE);
            sortId = 3;
            if (lastSortId != sortId) {
                isLoading = 0;
                loadData(0);
                lastSortId = sortId;
            }

        }
        if (v.getId() == R.id.new_text) {
            newText.setSelected(true);
            newText.setTextColor(Color.WHITE);
            hotText.setSelected(false);
            hotText.setTextColor(getColor(R.color.colorPrimaryDark));
            sortId = 2;
            if (lastSortId != sortId) {
                isLoading = 0;
                loadData(0);
                lastSortId = sortId;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SORT && resultCode == RESULT_OK) {
            categoryId = data.getIntExtra("categoryId", 10000000);
            if (categoryId == 10000000) {
                category.setText("全部歌单");
            } else {
                category.setText(data.getStringExtra("categoryName"));
            }
            isLoading = 0;
            loadData(0);
        }
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
