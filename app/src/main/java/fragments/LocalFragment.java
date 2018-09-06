package fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import adapter.LocalRecyclerViewAdapter;
import ecnu.ecnumusic.R;

public class LocalFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ExpandableListView mExpandableListView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.local,container,false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.local_recyclerview);
        mExpandableListView=(ExpandableListView)view.findViewById(R.id.local_expandableview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new LocalRecyclerViewAdapter());
    }
}
