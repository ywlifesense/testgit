package cn.js.nanhaistaffhome.views.home.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import cn.js.nanhaistaffhome.utils.DensityUtils;

/**
 * Created by JS on 8/6/15.
 */
public class MenuTitleView extends View {

    private Paint tPaint;
    private String text;

    public MenuTitleView(Context context){
        super(context);

        tPaint = new Paint();
        tPaint.setAntiAlias(true);
        tPaint.setColor(Color.WHITE);
        tPaint.setTextSize(DensityUtils.sp2px(context, 16));
    }

    public void setTextColor(int color){
        tPaint.setColor(color);
        postInvalidate();
    }

    public void setText(String str){
        this.text = str;
        postInvalidate();
    }

    public void setNumber(int number){
        this.text = String.valueOf(number);
        postInvalidate();
    }

    public void setTextSize(int size){
        tPaint.setTextSize(DensityUtils.sp2px(getContext(),size));
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        float th = getTextHeight(text);
//        float tw = tPaint.measureText(text);
        canvas.drawText(text,0,getHeight()/2+th/2,tPaint);
    }

    public float getTextHeight(String string){
        Rect rect = new Rect();
        tPaint.getTextBounds(string, 0, string.length(), rect);
        return rect.height();
    }

}
