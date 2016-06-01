package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
import android.os.Handler;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.Constant;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.models.User;
import cn.js.nanhaistaffhome.utils.SharedPreferenceUtil;

/**
 * Created by JS on 2016/4/7.
 */
public class LaunchActivity extends BaseActivity{

    private SharedPreferenceUtil spu;

    public void onCreate(Bundle savedInstanceState){
        // 隐藏android系统的状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏应用程序的标题栏，即当前activity的标题栏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Handler handler = new Handler();
        handler.postDelayed(runnable,2000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            spu = SharedPreferenceUtil.getInstance(LaunchActivity.this);
            boolean isLogin = spu.getBoolean(Constant.SP_KEY_IS_LOGIN,false);
            if (isLogin){
                User user =  new User(spu.getString(Constant.SP_KEY_USER_NAME),
                        spu.getString(Constant.SP_KEY_PASSWORD));
                user.setId(spu.getLong(Constant.SP_KEY_USER_ID));
                StaffApplication.getInstance().setUser(user);
                ActivityManager.toMainActivity(LaunchActivity.this);
            }else {
                ActivityManager.toLoginActivity(LaunchActivity.this);
            }

            finish();
        }
    };
}
