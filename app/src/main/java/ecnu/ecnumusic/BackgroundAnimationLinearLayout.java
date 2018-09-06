package ecnu.ecnumusic;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import Utils.FastBlurUtil;
import Utils.MusicUtil;
import fragments.SongFragment;

public class BackgroundAnimationLinearLayout extends LinearLayout {
    private static final int INDEX_BACKGROUND=0;
    private static final int INDEX_FROEGROUND=1;
    private static final int DURATION_ANIMATION = 1000;
    private LayerDrawable layerDrawable;
    private ObjectAnimator objectAnimator;
    private int musicPicRes=-1;
   private float number;
    public BackgroundAnimationLinearLayout(Context context){
        this(context,null);

    }
    public BackgroundAnimationLinearLayout(Context context, AttributeSet attr){
        this(context,attr,0);
    }
    public BackgroundAnimationLinearLayout(Context context, AttributeSet attr, int defStyleAttr){
        super(context,attr,defStyleAttr);
        initLayerDrawable(context);
        initObjectAnimator();
    }
    private void initLayerDrawable(Context context){
        Drawable backgroundDrawable=getForegroundDrawable(context, MusicUtil.getAlbumBitmap(context,1, SongFragment.mLocalMusics.get(0).getAlbum_id()));
        Drawable[] drawables=new Drawable[2];
        drawables[INDEX_BACKGROUND]=backgroundDrawable;
        drawables[INDEX_FROEGROUND]=backgroundDrawable;
        layerDrawable=new LayerDrawable(drawables);
        setBackground(layerDrawable);
    }
    private void initObjectAnimator(){
        objectAnimator=ObjectAnimator.ofFloat(this,"number",0f,1f);
        objectAnimator.setDuration(DURATION_ANIMATION);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int foregroundAlpha=(int)((float)animation.getAnimatedValue()*255);
                layerDrawable.getDrawable(INDEX_FROEGROUND).setAlpha(foregroundAlpha);
                BackgroundAnimationLinearLayout.this.setBackground(layerDrawable);
            }
        });
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                layerDrawable.setDrawable(INDEX_BACKGROUND,layerDrawable.getDrawable(INDEX_FROEGROUND));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setForeground(Drawable drawable){
        layerDrawable.setDrawable(INDEX_FROEGROUND,drawable);
    }

    public boolean isNeed2UpdateBackground(Bitmap musicPic) {
        if (this.musicPicRes == -1) return true;
        if (musicPicRes != this.musicPicRes) {
            return true;
        }
        return false;
    }
    public void beginAnimation() {
         objectAnimator.start();
    }
    public Drawable getForegroundDrawable(Context context,Bitmap musicPic) {
        if(musicPic==null){
            return context.getDrawable(R.drawable.ic_blackground);
        }

        // Bitmap blurBitmap= RenderScriptUtil.rsBlur(context,musicPic,1,1f);
        Bitmap blurBitmap= FastBlurUtil.blur(musicPic,50);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }
    public void setNumber(float number) {
        this.number = number;
    }

   public float getNumber() {
       return number;
    }

}
