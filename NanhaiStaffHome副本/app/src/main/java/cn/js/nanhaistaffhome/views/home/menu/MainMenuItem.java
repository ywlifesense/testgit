package cn.js.nanhaistaffhome.views.home.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 8/4/15.
 */
public class MainMenuItem extends RelativeLayout {

    private final static String TAG_DIVIDER = "divider";
    private final static String TAG_SELECTED_LINE = "selected";
    private final static String TAG_ICON = "icon";
    private final static String TAG_NUMBER = "number";
    private final static String TAG_ARROW = "arrow";
    private final static String TAG_TITLE = "title";

    private View selectedLine;
    private View dividerLine;
    private ImageView iconIv;
    private MenuTitleView titleTv;
    private MessageToastView numberTv;
    private ImageView arrowIv;
    private boolean selected;

    public MainMenuItem(Context context){
        super(context);
        init(context,null);
    }

    public MainMenuItem(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        selectedLine = new View(context);
        selectedLine.setTag(TAG_SELECTED_LINE);
        selectedLine.setBackgroundResource(R.color.orange);
        addView(selectedLine);

        iconIv= new ImageView(context);
        iconIv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iconIv.setTag(TAG_ICON);
        addView(iconIv);

        dividerLine = new View(context);
        dividerLine.setTag(TAG_DIVIDER);
        dividerLine.setBackgroundResource(R.color.white_alpha_1);
        addView(dividerLine);

        arrowIv = new ImageView(context);
        arrowIv.setTag(TAG_ARROW);
        arrowIv.setImageResource(R.drawable.ic_menu_arrow_right);
        addView(arrowIv);

        numberTv = new MessageToastView(context);
        numberTv.setTag(TAG_NUMBER);
        numberTv.setTextColor(Color.WHITE);
        numberTv.setVisibility(INVISIBLE);
        addView(numberTv);

        titleTv = new MenuTitleView(context);
        titleTv.setTag(TAG_TITLE);
        titleTv.setTextColor(context.getResources().getColor(R.color.white));
        titleTv.setText("个人信息");
        titleTv.setTextSize(16);
        addView(titleTv);

        if (attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MainMenuItem);

            int iconId = typedArray.getResourceId(R.styleable.MainMenuItem_item_icon,-1);
            if (iconId > 0){
                iconIv.setImageResource(iconId);
            }

            String title = typedArray.getString(R.styleable.MainMenuItem_item_title);
            if (!TextUtils.isEmpty(title)){
                titleTv.setText(title);
            }

            boolean isSelected = typedArray.getBoolean(R.styleable.MainMenuItem_item_selected,false);
            setSelected(isSelected);

            typedArray.recycle();
        }
    }

    public void setSelected(boolean isSelected){
        this.selected = isSelected;
        if (isSelected){
            selectedLine.setVisibility(VISIBLE);
            setBackgroundResource(R.color.white_alpha_1);
        }else {
            selectedLine.setVisibility(INVISIBLE);
            setBackgroundResource(R.color.transparent);
        }
    }

    public boolean isSelected(){
        return selected;
    }

    public void setToastNumber(int number){
        if (number > 0){
            numberTv.setNumber(number);
            numberTv.setVisibility(VISIBLE);
        }else {
            numberTv.setNumber(0);
            numberTv.setVisibility(INVISIBLE);
        }
    }

    public void hideToastNumber(){
        numberTv.setVisibility(VISIBLE);
    }

    @Override
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight());
    }

    private int measureWidth(int widthMeasureSpec){
        int result = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthMode){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        int height = (int)(metrics.heightPixels * 0.09);
        return height;
    }

    @Override
    public void onLayout(boolean changed,int l,int t,int r,int b){
        int totalWidth = r - l;
        int totalHeight = b - t;
        int gap = totalHeight/4;
        int usedWidth = 0;
        int childCount = getChildCount();

        //按照添加子控件顺序设置子控件layout
        for (int i=0;i<childCount;i++){
            View view = getChildAt(i);
            if (view.getTag().equals(TAG_SELECTED_LINE)){
                view.layout(0,0,2,totalHeight);
                usedWidth += 2 + gap;
            }else if (view.getTag().equals(TAG_ICON)){
                view.layout(usedWidth,gap,totalHeight/2+usedWidth,totalHeight-gap);
                usedWidth += totalHeight/2 + gap;
            }else if(view.getTag().equals(TAG_DIVIDER)){
                view.layout(usedWidth,0,totalWidth,1);
            }else if (view.getTag().equals(TAG_ARROW)){
                view.layout(totalWidth-totalHeight/3-gap,totalHeight/3,totalWidth,totalHeight*2/3);
                usedWidth += totalHeight/3 + 2*gap;
            }else if (view.getTag().equals(TAG_NUMBER)){
                int nw = totalHeight/2;
                int nh = totalHeight/2;
                int nx = arrowIv.getLeft() - nw;
                view.layout(nx, gap, nx + nw, nh + gap);
            }else if(view.getTag().equals(TAG_TITLE)){
                int tl = iconIv.getRight() + gap;
                int tt = gap;
                int tr = numberTv.getLeft() - gap;
                int tb = gap * 3;

                view.layout(tl,tt,tr,tb);
            }
        }
    }

}
