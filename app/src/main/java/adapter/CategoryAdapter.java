package adapter;

import android.content.Context;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import classcollection.sort.Category;
import ecnu.ecnumusic.R;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Category> categories;
    private GridLayoutManager manager;
    private SortAdapter adapter;
    private SortAdapter.OnItemClickListener listener;
    private static int HEAD_TYPE = 101;
    private static int NORMAL_TYPE = 102;

    public CategoryAdapter(SortAdapter.OnItemClickListener listener, List<Category> categoryList) {
        this.categories = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (mContext == null) {
            mContext = parent.getContext();
        }
        manager = new GridLayoutManager(mContext, 3);
        if (viewType==HEAD_TYPE){
            view=LayoutInflater.from(mContext).inflate(R.layout.item_head,parent,false);
            return new HeadViewHolder(view);
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Category category = categories.get(position);
        if (position==0){
            ((HeadViewHolder)holder).sortName.setText("全部歌单");
            ((HeadViewHolder)holder).rlSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(category.itemList.get(0).categoryId,category.itemList.get(0).categoryName);
                }
            });
            return;
        }
        ((ViewHolder)holder).groupName.setText(category.categoryGroupName);
        ((ViewHolder)holder).recyclerView.setLayoutManager(manager);
        adapter = new SortAdapter(category.itemList);
        if (adapter.getListener() == null) {
            adapter.setOnItemClickListener(listener);
        }
        ((ViewHolder)holder).recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEAD_TYPE;
        return NORMAL_TYPE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView groupName;
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            groupName = view.findViewById(R.id.groupname);
            recyclerView = view.findViewById(R.id.category_recycler);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        private TextView sortName;
        private RelativeLayout rlSort;
        public HeadViewHolder(View view) {
            super(view);
            sortName = view.findViewById(R.id.sort_name);
            rlSort=view.findViewById(R.id.rl_sort);
        }
    }

}
