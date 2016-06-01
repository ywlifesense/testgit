package cn.js.nanhaistaffhome.views.home.content;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.simonvt.menudrawer.MenuDrawer;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.activitys.HomeActivity;
import cn.js.nanhaistaffhome.activitys.MainActivity;

/**
 * Created by JS on 2015/9/15.
 */
public abstract class ContentPage extends RelativeLayout implements MenuDrawer.OnDrawerStateChangeListener{

    public abstract void init();
    public abstract int getLayoutResource();

    private ImageButton leftBtn;
    private TextView titleTv;
    private ProgressBar progressBar;
    public ViewStub viewStub;
    private boolean hadInflate;

    public ContentPage(final Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.content_layout, this);
        leftBtn = (ImageButton)findViewById(R.id.btn_left);
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).menuDrawer.toggleMenu();
            }
        });
        titleTv = (TextView)findViewById(R.id.tv_title);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        viewStub = (ViewStub)findViewById(R.id.import_panel);
        viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub viewStub, View view) {
                hadInflate = true;
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        showLoading(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                view.startAnimation(animation);
            }
        });
    }

    public void setTitle(String title){
        titleTv.setText(title);
    }

    public void showLoading(boolean isShow){
        if (isShow){
            progressBar.setVisibility(VISIBLE);
        }else {
            progressBar.setVisibility(GONE);
        }
    }

    protected void initView(){
        if (!hadInflate) {
            viewStub.setLayoutResource(getLayoutResource());
            viewStub.inflate();
        }

//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {}
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                showLoading(false);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {}
//        });
//        view.startAnimation(animation);
    }

    @Override
    public void onDrawerStateChange(int oldState, int newState) {
        if (newState == MenuDrawer.STATE_CLOSED && !hadInflate){
            init();
        }
    }

    @Override
    public void onDrawerSlide(float openRatio, int offsetPixels) {

    }
}
