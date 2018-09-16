package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.Utility;
import Utils.ResultCallback;
import adapter.RecommendSonglistAdapter;
import ecnu.ecnumusic.PlaylistFragment;
import ecnu.ecnumusic.R;
import ecnu.ecnumusic.SongListDetailActivity;
import jiekou.FragmentEntrust;
import okhttp3.Request;
import okhttp3.Response;
import other.GlideImageLoader;
import shouyeclass.Album;
import shouyeclass.Content;
import shouyeclass.Shouye;
import shouyeclass.Vhot;

public class musicFragment extends Fragment implements View.OnClickListener, RecommendSonglistAdapter.OnItemClickListener{
    private Banner banner;
    private LinearLayout singerLayout,recommendlayout,songlistLayout,rankLayout,recommendsonglistLayout,changeLayout;
    private List<String> imageUrls=new ArrayList<>();
    private RecyclerView songlistRecycler,albumRecycler;
    private TextView textView;
    private static final String TAG="MusicFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.music,container,false);
       initView(view);
       initShouyeRequest();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setText(getDay());
    }

    private void initView(View view){
        banner=(Banner)view.findViewById(R.id.banner_view);
        singerLayout=(LinearLayout)view.findViewById(R.id.ll_singer);
        singerLayout.setOnClickListener(this);
        recommendlayout=(LinearLayout)view.findViewById(R.id.ll_recommend);
        recommendlayout.setOnClickListener(this);
        songlistLayout=(LinearLayout)view.findViewById(R.id.ll_song_list);
        songlistLayout.setOnClickListener(this);
        rankLayout=(LinearLayout)view.findViewById(R.id.ll_rank_list);
        rankLayout.setOnClickListener(this);
        recommendsonglistLayout=(LinearLayout)view.findViewById(R.id.ll_recommend_songlist);
        recommendsonglistLayout.setOnClickListener(this);
        songlistRecycler=(RecyclerView)view.findViewById(R.id.songlist_recycler);
        songlistRecycler.setNestedScrollingEnabled(false);
        albumRecycler=(RecyclerView)view.findViewById(R.id.album_recycler);
        albumRecycler.setNestedScrollingEnabled(false);
        changeLayout=(LinearLayout)view.findViewById(R.id.ll_change_songlist);
        textView=(TextView)view.findViewById(R.id.date_song);

    }

    private void initBannerCover(Shouye shouye){
        for (Content content:shouye.focusData.contentData.contentList){
            imageUrls.add(content.picInfo.url);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        PlaylistFragment playlistFragment=new PlaylistFragment();
        SingerListFragment singerListFragment=new SingerListFragment();
        switch (v.getId()){
            case R.id.ll_singer:
                ((FragmentEntrust)getActivity()).pushFragment(singerListFragment,PlaylistFragment.TAG);
                break;
            case R.id.ll_recommend:
                getDayRecommendList();
                break;
            case R.id.ll_song_list:
                ((FragmentEntrust)getActivity()).pushFragment(playlistFragment,PlaylistFragment.TAG);
                break;
            case R.id.ll_rank_list:
                Toast.makeText(getContext(),"ranklist",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_recommend_songlist:
                ((FragmentEntrust)getActivity()).pushFragment(playlistFragment,PlaylistFragment.TAG);
                break;

                default:
                    break;
        }
    }

    private void changeRecommendlist(List<Vhot> vhotList){
        List<Vhot> lists=new ArrayList<>();
        lists.clear();
        for(int i=0;i<6;i++){
            lists.add(i,vhotList.remove(0));
        }
       vhotList.addAll(lists);
    }

    @Override
    public void onItemClick(String tag,ImageView imageView,Vhot vhot,Album album) {

        Bundle bundle=new Bundle();
        bundle.putString("tag",tag);
        bundle.putSerializable("recommendlist",vhot);
        bundle.putSerializable("album",album);

        Intent intent=new Intent(getContext(), SongListDetailActivity.class);
        intent.putExtra("song",bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),imageView,"sharedView");//与xml文件对应
        startActivity(intent,options.toBundle());
    }

    private void getDayRecommendList(){
        MusicRequestUtil.getDayRecommendid(getContext(), new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String text=response.body().string();
                try{
                    JSONObject jsonObject=new JSONObject(text).getJSONObject("recomPlaylist").getJSONObject("data");
                    JSONArray jsonArray=jsonObject.getJSONArray("v_hot");
                    long content_id=jsonArray.getJSONObject(0).getLong("content_id");
                    String imageUrl=jsonArray.getJSONObject(0).getString("cover");
                    String titleContent=jsonArray.getJSONObject(0).getString("title");
                    Bundle bundle=new Bundle();
                    bundle.putLong("content_id",content_id);
                    bundle.putString("imageUrl",imageUrl);
                    bundle.putString("titleContent",titleContent);
                    DayRecommnedFragment fragment=new DayRecommnedFragment();
                    fragment.setArguments(bundle);
                    ((FragmentEntrust)getActivity()).pushFragment(fragment,DayRecommnedFragment.TAG);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onError(Request request, Exception ex) {

            }
        });
    }
    private String getDay(){
        Calendar calendar=Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.DATE));
    }

    private void initShouyeRequest(){
        MusicRequestUtil.getShouyeMusic(getContext(), new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                Log.e(TAG, "onResponse: "+responseText);
                Shouye shouye=Utility.handleShouyeResponse(responseText);

                initBannerCover(shouye);
                banner.setImageLoader(new GlideImageLoader());
                banner.setImages(imageUrls);
                banner.start();
                initSongListRecycler(shouye);
            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(getContext(),"shouyefailed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSongListRecycler(Shouye shouye){
        final List<Vhot> vhotList=shouye.recomData.vhotData.vhotList;
        final List<Album> albumList=shouye.albumData.listData.albumList;
        vhotList.remove(0);
        GridLayoutManager manager=new GridLayoutManager(getContext(),3);
        GridLayoutManager albumManager=new GridLayoutManager(getContext(),3);
        songlistRecycler.setLayoutManager(manager);
        albumRecycler.setLayoutManager(albumManager);
        final RecommendSonglistAdapter adapter=new RecommendSonglistAdapter("songlist",vhotList,albumList);
        final RecommendSonglistAdapter albumAdapter=new RecommendSonglistAdapter("albumlist",vhotList,albumList);
        adapter.setOnItemClickListener(this);
        albumAdapter.setOnItemClickListener(this);
        songlistRecycler.setAdapter(adapter);
        albumRecycler.setAdapter(albumAdapter);
        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeRecommendlist(vhotList);
                adapter.notifyDataSetChanged();
            }
        });
    }



}

