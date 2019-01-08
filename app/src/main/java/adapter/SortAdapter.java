package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classcollection.sort.Item;
import ecnu.ecnumusic.R;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private Context mContext;
    private List<Item> items;
    private OnItemClickListener listener;

    public SortAdapter(List<Item> itemList) {
        items = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sort, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.sortName.setText(Html.fromHtml(item.categoryName,FROM_HTML_MODE_COMPACT));
        holder.rlSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(item.categoryId, item.categoryName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sortName;
        private RelativeLayout rlSort;
        public ViewHolder(View view) {
            super(view);
            sortName = view.findViewById(R.id.sort_name);
            rlSort=view.findViewById(R.id.rl_sort);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int categoryId, String categoryName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener getListener() {
        return this.listener;
    }
}
