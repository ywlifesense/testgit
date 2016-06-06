package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.Constant;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.http.HttpHelper;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.models.User;
import cn.js.nanhaistaffhome.utils.MD5Utils;
import cn.js.nanhaistaffhome.utils.SDCardUtil;
import cn.js.nanhaistaffhome.utils.SharedPreferenceUtil;


public class LoginActivity extends BaseActivity {

    //测试git

    private EditText nameEt;
    private EditText pwdEt;
    private SharedPreferenceUtil spu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spu = SharedPreferenceUtil.getInstance(this);
        boolean isLogin = spu.getBoolean(Constant.SP_KEY_IS_LOGIN,false);
        if (isLogin){
            User user =  new User(spu.getString(Constant.SP_KEY_USER_NAME),
                    spu.getString(Constant.SP_KEY_PASSWORD));
            user.setId(spu.getLong(Constant.SP_KEY_USER_ID));
            StaffApplication.getInstance().setUser(user);
            ActivityManager.toMainActivity(this);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        nameEt = (EditText)findViewById(R.id.et_name);
        pwdEt = (EditText)findViewById(R.id.et_pwd);

//        nameEt.setText("kkk123");
//        pwdEt.setText("123456q");
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                final String name = nameEt.getText().toString();
                final String pwd = pwdEt.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)){
                    HttpClient.login(name, pwd, new OnHttpRequestListener() {
                        @Override
                        public void onRequestStart() {
                            showProgressDialog("正在登录，请稍后...");
                        }

                        @Override
                        public void onRequestEnd(String result) {
                            hideProgressDialog();
                            try{
                                JSONObject obj = new JSONObject(result);
                                if (obj.optInt("code",1)==0){

                                    showToast("登陆成功");

                                    spu.putString(Constant.SP_KEY_USER_NAME, name);
                                    spu.putString(Constant.SP_KEY_PASSWORD, pwd);
                                    spu.putBoolean(Constant.SP_KEY_IS_LOGIN, true);
                                    User user = new User(name,pwd);

                                    JSONObject data = obj.optJSONObject("data");
                                    if (data != null) {
                                        long userId = data.optLong("userid");
                                        spu.putLong(Constant.SP_KEY_USER_ID, userId);
                                        user.setId(userId);
                                    }
                                    StaffApplication.getInstance().setUser(user);

                                    ActivityManager.toMainActivity(LoginActivity.this);
                                    finish();
                                }else {
                                    if(obj.optString("msg").equals("认证不成功！")){
                                        String str = "认证失败，或用户名密码错误!";
                                        showToast(str);
                                    }
                                   }
                            }catch (Exception e){
                                showToast("登陆失败，请稍后再试！");
                            }
                        }

                        @Override
                        public void onRequestCancal() {
                            hideProgressDialog();
                            showToast("登录已取消！");
                        }
                    });
                }else {
                    showToast("用户名和密码不能为空！");
                }
                break;
            case R.id.btn_forget_pwd:
                break;
            case R.id.btn_register:
                ActivityManager.toRegisterActivity(this);
                break;
            case R.id.btn_visitor:
                ActivityManager.toMainActivity(LoginActivity.this);
                finish();
                break;
            default:
                break;
        }
    }

}
