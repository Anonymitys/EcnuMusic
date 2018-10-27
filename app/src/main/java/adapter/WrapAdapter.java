package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class WrapAdapter extends RecyclerView.Adapter {
    // 基本的头部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    // 基本的底部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;
    private SparseArray<View> headView;
    private SparseArray<View> footView;

    private static final String TAG="WrapAdapter";

    private RecyclerView.Adapter mAdapter;
    public WrapAdapter(RecyclerView.Adapter adapter){
        mAdapter=adapter;
        headView=new SparseArray<>();
        footView=new SparseArray<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return headView.keyAt(position);
        }
        if (isFooterPosition(position)) {
            // 直接返回position位置的key
            position = position - headView.size() - mAdapter.getItemCount();
            return footView.keyAt(position);
        }
        // 返回列表Adapter的getItemViewType
        position = position - headView.size();
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + headView.size() + footView.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHeadViewType(viewType)) {
            View headerView = headView.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }

        if (isFootViewType(viewType)) {
            Log.e(TAG, "onCreateViewHolder: " );
            View footerView = footView.get(viewType);
            return createHeaderFooterViewHolder(footerView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        // 计算一下位置
        position = position - headView.size();
        mAdapter.onBindViewHolder(holder, position);

    }

    private boolean isHeadViewType(int viewType){
        int position=headView.indexOfKey(viewType);
        return position>=0;
    }
    private boolean isFootViewType(int viewType){
        int position=footView.indexOfKey(viewType);
        return position>=0;
    }
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {

        };
    }
    private boolean isFooterPosition(int position) {
        return position >= (headView.size() + mAdapter.getItemCount());
    }

    private boolean isHeaderPosition(int position) {
        return position < headView.size();
    }

    public void addHeaderView(View view) {
        int position = headView.indexOfValue(view);
        if (position < 0) {
            headView.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        int position = footView.indexOfValue(view);
        if (position < 0) {
            footView.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }
    // 移除头部
    public void removeHeaderView(View view) {
        int index = headView.indexOfValue(view);
        if (index < 0) return;
        headView.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = footView.indexOfValue(view);
        if (index < 0) return;
        footView.removeAt(index);
        notifyDataSetChanged();
    }
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ?layoutManager.getSpanCount():1;
                }
            });
        }
    }


}
