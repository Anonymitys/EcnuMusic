package ecnu.ecnumusic;


import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SearchAdapter;
import classcollection.Song;
import okhttp3.Request;
import okhttp3.Response;
import service.MusicService;
import shouyeclass.searchsong.SongSearch;

public class SearchActivity extends BaseActivity implements SearchAdapter.OnItemClickListener {

    private android.support.v7.widget.Toolbar toolbar;
    private EditText searchText;
    private RecyclerView recyclerView;
    private MusicService.MusicBinder musicBinder;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.tool_bar);
        recyclerView = findViewById(R.id.search_recycler);
        initToolbar();
        searchText = findViewById(R.id.search_edit);
        searchText.setFocusable(true);
        searchText.setFocusableInTouchMode(true);
        searchText.requestFocus();
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e(TAG, "onEditorAction: " + v.getText());
                    initRequest(v.getText().toString());
                }
                return false;
            }
        });

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRequest(String keyWord) {
        MusicRequestUtil.getSearchSong(this, keyWord, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText = response.body().string();
                SongSearch songSearch = Utility.handleSongSearchResponse(responseText);
                LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
                recyclerView.setLayoutManager(manager);
                SearchAdapter adapter = new SearchAdapter(songSearch);
                adapter.setOnItemClickListener(SearchActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e(TAG, "onError: ");
            }
        });
    }

    @Override
    public void connection(IBinder service) {
        super.connection(service);
        musicBinder = (MusicService.MusicBinder) service;
    }

    @Override
    public void onSingerClick(String singerMid, String singerPic, String singerName) {
        Intent intent = new Intent(this, SingerDetailActivity.class);
        intent.putExtra("singerMid", singerMid);
        intent.putExtra("singerPic", singerPic);
        intent.putExtra("singerName", singerName);
        startActivity(intent);
    }

    @Override
    public void onSongClick(Song song) {
        musicBinder.playSong(song);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }
}
