package widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import classcollection.Music;
import Utils.DisplayUtil;
import Utils.MusicUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import ecnu.ecnumusic.R;

public class DiscView extends RelativeLayout {
    private int mScreenWidth,mScreenHeight;
    private ImageView mIvNeedle;
    private CircleImageView circleImageView,backgroundImage;
    private ViewPager mVpContain;
    private PagerAdapter adapter;
    private Context mContext;
    private IPlayInfo mIPlayInfo;
    private boolean isNeedTostartAnimation=false;
    private NeedleStatus needleStatus= NeedleStatus.IN_FAR_POSITION;
    private ObjectAnimator needleanimation;


    private List<View> mDiscLayouts=new ArrayList<>();
    private List<ObjectAnimator> mDiscAnimators=new ArrayList<>();
    private List<Music> musicList=new ArrayList<>();

    public enum MusicChangedStatus {
        PLAY, PAUSE, NEXT, LAST, STOP
    }
    public enum NeedleStatus{
        IN_NEAR_POSITION,
        IN_FAR_POSITION,
        TO_NEAR_POSITION,
        TO_FAR_POSITION
    }
    public interface IPlayInfo {
        /*用于更新标题栏变化*/
        public void onMusicInfoChanged(String musicName, String musicAuthor);
        /*用于更新背景图片*/
        public void onMusicPicChanged(Bitmap musicPic);
        /*用于更新音乐播放状态*/
        public void onMusicChanged(MusicChangedStatus musicChangedStatus);
    }

    public DiscView(Context context) {
        this(context, null);
    }

    public DiscView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mScreenWidth = DisplayUtil.getScreenWidth(context);
        mScreenHeight = DisplayUtil.getScreenHeight(context);
        LayoutInflater.from(context).inflate(R.layout.disc_view,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initNeedleView();
        initDiscBlackground();
        initViewPager();
        initObjectAnimation();

    }
    private void initNeedleView(){
        mIvNeedle = (ImageView) findViewById(R.id.iv_Needle);

        int needleWidth = (int) (DisplayUtil.SCALE_NEEDLE_WIDTH * mScreenWidth);
        int needleHeight = (int) (DisplayUtil.SCALE_NEEDLE_HEIGHT * mScreenHeight);

        /*设置手柄的外边距为负数，让其隐藏一部分*/
        int marginTop = (int) (DisplayUtil.SCALE_NEEDLE_MARGIN_TOP * mScreenHeight) * -1;
        int marginLeft = (int) (DisplayUtil.SCALE_NEEDLE_MARGIN_LEFT * mScreenWidth);

        Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable
                .play_neddle);
        Bitmap bitmap = Bitmap.createScaledBitmap(originBitmap, needleWidth, needleHeight, false);

        LayoutParams layoutParams = (LayoutParams) mIvNeedle.getLayoutParams();
        layoutParams.setMargins(marginLeft, marginTop, 0, 0);

        int pivotX = (int) (DisplayUtil.SCALE_NEEDLE_PIVOT_X * mScreenWidth);
        int pivotY = (int) (DisplayUtil.SCALE_NEEDLE_PIVOT_Y * mScreenWidth);

        mIvNeedle.setPivotX(pivotX);
        mIvNeedle.setPivotY(pivotY);
         mIvNeedle.setRotation(DisplayUtil.ROTATION_INIT_NEEDLE);
        mIvNeedle.setImageBitmap(bitmap);
        mIvNeedle.setLayoutParams(layoutParams);
    }
    private void initDiscBlackground() {
        backgroundImage=(CircleImageView)findViewById(R.id.backgroundimage_view);
        int marginTop = (int) (DisplayUtil.SCALE_DISC_MARGIN_TOP * mScreenHeight);

        LayoutParams layoutParam = (LayoutParams)backgroundImage
                .getLayoutParams();
        layoutParam.setMargins(0, marginTop-2, 0, 0);
        backgroundImage.setLayoutParams(layoutParam);

    }
    private void initViewPager(){
        mVpContain=(ViewPager)findViewById(R.id.playview_pager);
        mVpContain.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return mDiscLayouts.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(mDiscLayouts.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
               View disclayout=mDiscLayouts.get(position);
               container.addView(disclayout);
               return disclayout;
            }
        };
        mVpContain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPositionOffsetPixels=0;
            int currentItem;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //左滑
                if (lastPositionOffsetPixels > positionOffsetPixels) {
                    if (positionOffset < 0.5) {
                        notifyMusicInfoChanged(position);
                    } else {
                        notifyMusicInfoChanged(mVpContain.getCurrentItem());
                    }
                }
                //右滑
                else if (lastPositionOffsetPixels < positionOffsetPixels) {
                    if (positionOffset > 0.5) {
                        notifyMusicInfoChanged(position+1 );
                    } else {
                        notifyMusicInfoChanged(position);
                    }
                }
                lastPositionOffsetPixels = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                notifyMusicPicChanged(position);
                resetDiscAnimator(position);
                currentItem=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case 0:
                        playAnimation();
                        break;



                    case 1:
                       pauseAnimator();
                       break;


                }
            }
        });
        mVpContain.setAdapter(adapter);
    }
    private void initObjectAnimation(){
        needleanimation=ObjectAnimator.ofFloat(mIvNeedle,"rotation",-30,0);
        needleanimation.setInterpolator(new AccelerateInterpolator());
        needleanimation.setDuration(300);
        needleanimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(needleStatus== NeedleStatus.IN_FAR_POSITION){
                    needleStatus= NeedleStatus.TO_NEAR_POSITION;
                }else if(needleStatus== NeedleStatus.IN_NEAR_POSITION){
                    needleStatus= NeedleStatus.TO_FAR_POSITION;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(needleStatus== NeedleStatus.TO_NEAR_POSITION){
                    needleStatus= NeedleStatus.IN_NEAR_POSITION;
                    int index=mVpContain.getCurrentItem();
                    playDiscAnimation(index);
                }else if(needleStatus== NeedleStatus.TO_FAR_POSITION){
                    needleStatus= NeedleStatus.IN_FAR_POSITION;
                }
                if(isNeedTostartAnimation){
                    isNeedTostartAnimation=false;
                    playAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private void resetDiscAnimator(int position){
        for(int i=0;i<mDiscLayouts.size();i++){
            if(position==i)continue;
            mDiscAnimators.get(i).cancel();
            CircleImageView view=mDiscLayouts.get(i).findViewById(R.id.discimage_view);
            view.setRotation(0);
        }
    }
    private void playAnimation(){
        if(needleStatus== NeedleStatus.IN_FAR_POSITION){
            needleanimation.start();
        }else if(needleStatus== NeedleStatus.TO_FAR_POSITION){
            isNeedTostartAnimation=true;
        }
    }
    private void pauseAnimator(){
        if(needleStatus== NeedleStatus.IN_NEAR_POSITION){
            int index=mVpContain.getCurrentItem();
            pauseDiscAnimator(index);
        }else if(needleStatus== NeedleStatus.TO_NEAR_POSITION){
            needleanimation.reverse();
            needleStatus= NeedleStatus.TO_FAR_POSITION;
        }
    }
    private void playDiscAnimation(int position){
        ObjectAnimator objectAnimator=mDiscAnimators.get(position);
        if(objectAnimator.isPaused()){
            objectAnimator.resume();
        }else{
            objectAnimator.start();
        }

    }
    private void pauseDiscAnimator(int position){
        ObjectAnimator objectAnimator=mDiscAnimators.get(position);
        objectAnimator.pause();
        needleanimation.reverse();
    }
    public void setMusicDataList(List<Music> musicDataList) {
        if (musicDataList.isEmpty()) return;

        mDiscLayouts.clear();
        musicList.clear();
        musicList.addAll(musicDataList);

        int i = 0;
        int musicPicSize = (int) (mScreenWidth * DisplayUtil.SCALE_MUSIC_PIC_SIZE);

        for (Music music : musicList) {
            View discLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_disc,
                    mVpContain, false);

            CircleImageView disc = (CircleImageView) discLayout.findViewById(R.id.discimage_view);

            int marginTop = (int) (DisplayUtil.SCALE_DISC_MARGIN_TOP * mScreenHeight);

            disc.setImageDrawable(getDiscDrawable(MusicUtil.getAlbumBitmap(mContext,musicPicSize,music.getAlbum_id())));
            LayoutParams layoutParam = (LayoutParams)disc
                    .getLayoutParams();
            layoutParam.setMargins(0, marginTop, 0, 0);
            disc.setLayoutParams(layoutParam);
            mDiscAnimators.add(getDiscAnimation(disc));
            mDiscLayouts.add(discLayout);
        }


        adapter.notifyDataSetChanged();


    }
    private ObjectAnimator getDiscAnimation(CircleImageView circleImageView){
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(circleImageView,"rotation",0,360);
        objectAnimator.setDuration(20000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        return objectAnimator;
    }
    private Drawable getDiscDrawable(Bitmap musicPic) {
        if(musicPic==null){
            musicPic=BitmapFactory.decodeResource(getResources(), R.drawable.empty_foreground);
        }
        Bitmap bitmapDisc=Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_disc));
        BitmapDrawable discDrawable =new BitmapDrawable(bitmapDisc);
        RoundedBitmapDrawable roundMusicDrawable = RoundedBitmapDrawableFactory.create
                (getResources(), musicPic);
        roundMusicDrawable.setCircular(true);


        //抗锯齿
        discDrawable.setAntiAlias(true);
        roundMusicDrawable.setAntiAlias(true);

        Drawable[] drawables = new Drawable[2];
        drawables[0] = roundMusicDrawable;
        drawables[1] = discDrawable;

        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        //调整专辑图片的四周边距，让其显示在正中
        layerDrawable.setLayerInset(0, 45, 45, 45,
                45);

        return layerDrawable;
    }
    public void setPlayInfoListener(IPlayInfo playInfo){
        this.mIPlayInfo=playInfo;
    }
    private void notifyMusicInfoChanged(int position){
        if(mIPlayInfo!=null){
            mIPlayInfo.onMusicInfoChanged(musicList.get(position).getTitle(),musicList.get(position).getArtist());
        }
    }
    public void notifyMusicPicChanged(int position) {
        if (mIPlayInfo != null) {
            Music musicData = musicList.get(position);
            mIPlayInfo.onMusicPicChanged(MusicUtil.getAlbumBitmap(mContext,1,musicData.getAlbum_id()));
        }
    }
}
