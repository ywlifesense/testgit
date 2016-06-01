package cn.js.nanhaistaffhome.views.others;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.js.nanhaistaffhome.R;

/**
 * Created by js on 8/1/15.
 */
public class WeightImageView extends ImageView{

    //目标宽度高度
    private int tWidth;
    private int tHeight;
    //目标缩放比例
    private float tScale;

    public WeightImageView(Context context){
        super(context);
        init(context,null);
    }

    public WeightImageView(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){

        setScaleType(ScaleType.MATRIX);

        if (attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeightImageView);
            float weight = typedArray.getFloat(R.styleable.WeightImageView_scaleToParent,0);
            if (weight > 0){
                DisplayMetrics metrics = new DisplayMetrics();
                WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                manager.getDefaultDisplay().getMetrics(metrics);
                tWidth = (int)(metrics.widthPixels * weight);
                tHeight = (int)(metrics.heightPixels * weight);

                Drawable drawable = getDrawable();
                if (drawable != null){
                    //获取图片宽高
                    int dw = drawable.getIntrinsicWidth();
                    int dh = drawable.getIntrinsicHeight();

                    tScale = 1.0f;
                    //图片宽度大于视图宽度
                    if (dw > tWidth && dh <= tHeight){
                        tScale = tWidth*1.0f/dw;
                    }
                    //图片高度大于视图高度
                    if (dw <= tWidth && dh > tHeight){
                        tScale = tHeight*1.0f/dh;
                    }
                    //图片宽度高度都大于视图宽度高度
                    if (dw > tWidth && dh > tHeight){
                        tScale = (float)Math.min(tWidth * 1.0f / dw, tHeight * 1.0 / dh);
                    }

                    tWidth = (int)(dw * tScale);
                    tHeight = (int)(dh * tScale);

                    Matrix matrix = new Matrix();
                    matrix.postTranslate((tWidth-dw)/2,(tHeight-dh)/2);
                    matrix.postScale(tScale,tScale,tWidth/2,tHeight/2);
                    setImageMatrix(matrix);
                }
            }
            typedArray.recycle();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(tWidth,tHeight);
    }

}
