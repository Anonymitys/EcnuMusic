package ecnu.ecnumusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.CategoryAdapter;
import adapter.SortAdapter;
import adapter.SpaceItemDecoration;
import classcollection.sort.Category;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;

public class CategoryActivity extends BaseActivity implements SortAdapter.OnItemClickListener {
    private XRecyclerView recyclerView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        recyclerView = findViewById(R.id.category_recycler);
        toolbar=findViewById(R.id.category_toolbar);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setPullRefreshEnabled(false);
        initToolBar();
        initRequest();
    }

    private void initRequest() {
        MusicRequestUtil.getCategory(this, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                List<Category> categoryList = Utility.handleCategoryResponse(str);
                CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new SpaceItemDecoration(10));
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(CategoryActivity.this, "category error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("选择分类");
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
    public void onItemClick(int categoryId, String categoryName) {
        Toast.makeText(this, String.valueOf(categoryId), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categoryName", categoryName);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }
}
