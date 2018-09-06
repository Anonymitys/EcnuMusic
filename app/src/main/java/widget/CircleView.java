package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import ecnu.ecnumusic.R;

public class CircleView extends View {
    private Paint backPaint=new Paint();
    private Paint forePaint=new Paint();
    private int backColor;
    private int foreColor;
    private int duration;
    private boolean isPause=false;
    private int mCircleX=0;
    private int mCircleY=0;
    private RectF rectF=new RectF();
    private float currentAngle=0f;

    public CircleView(Context context){
        super(context);
    }
    public CircleView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCircleX=getLeft()+getWidth()/2;
        mCircleY=getTop()+getHeight()/2;
        rectF.left=mCircleX-getWidth()/2+getPaddingLeft();
        rectF.right=mCircleX+getWidth()/2-getPaddingRight();
        rectF.top=mCircleY-getHeight()/2+getPaddingTop();
        rectF.bottom=mCircleX+getHeight()/2-getPaddingBottom();
        canvas.drawArc(rectF,-90,360,false,backPaint);
        canvas.drawArc(rectF,-90,currentAngle,false,forePaint);
        if(!isPause){
            if(currentAngle<360){
                currentAngle=currentAngle+360f/duration;
                postInvalidateDelayed(1000);
            }
        }


    }
    private void init(Context context,AttributeSet attrs){
       TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.CircleView);
       backColor=typedArray.getColor(R.styleable.CircleView_backColor,getResources().getColor(R.color.backColor,null));
       foreColor=typedArray.getColor(R.styleable.CircleView_foreColor,getResources().getColor(R.color.colorPrimary,null));
       typedArray.recycle();
       backPaint.setColor(backColor);
       backPaint.setAntiAlias(true);
       backPaint.setStyle(Paint.Style.STROKE);
       backPaint.setStrokeWidth(4f);
       forePaint.setColor(foreColor);
       forePaint.setAntiAlias(true);
       forePaint.setStyle(Paint.Style.STROKE);
       forePaint.setStrokeWidth(4f);


    }
    public void setDuration(int duration){
        this.duration=duration;
    }
    public void setBackColorTransparent(boolean values){
        if(values){
            backPaint.setColor(Color.TRANSPARENT);
        }else{
            backPaint.setColor(backColor);
        }

    }
    public void setPause(boolean value){
        isPause=value;
    }
}
