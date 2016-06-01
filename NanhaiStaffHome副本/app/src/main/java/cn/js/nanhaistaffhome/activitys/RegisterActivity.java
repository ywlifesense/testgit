package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.utils.MD5Utils;
import cn.js.nanhaistaffhome.utils.MyTextUtils;

/**
 * Created by JS on 8/9/15.
 */
public class RegisterActivity extends BaseActivity {

    private EditText nameEt;
    private EditText pwdEt;
    private EditText checkpwdEt;
    private EditText emailEt;
    private EditText mobileEt;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        nameEt = (EditText)findViewById(R.id.et_name);
        pwdEt = (EditText)findViewById(R.id.et_pwd);
        checkpwdEt = (EditText)findViewById(R.id.et_check_pwd);
        emailEt = (EditText)findViewById(R.id.et_email);
        mobileEt = (EditText)findViewById(R.id.et_mobile);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_register:
                String name = nameEt.getText().toString();
                String pwd = pwdEt.getText().toString();
                String checkPwd = checkpwdEt.getText().toString();
                String email = emailEt.getText().toString();
                String mobile = mobileEt.getText().toString();
                doRegister(name,pwd,checkPwd,email,mobile);
                break;
        }
    }

    public void doRegister(String name,String pwd,String checkPwd,String email,String mobile){

        if (TextUtils.isEmpty(name)){
            showToast("用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(email)){
            showToast("邮箱不能为空");
            return;
        }else {
            if (!MyTextUtils.isEmail(email)){
                showToast("邮箱格式不正确，请重新填写");
                return;
            }
        }

        if (TextUtils.isEmpty(mobile)){
            showToast("手机号码不能为空");
            return;
        }

        if (TextUtils.isEmpty(pwd)){
            showToast("密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(checkPwd)){
            showToast("确认密码不能为空");
            return;
        }


        if (!pwd.equals(checkPwd)) {
            showToast("两次输入的密码不一致");
            return;
        }

        HttpClient.register(name, pwd, email, mobile, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {
                showProgressDialog("正在注册，请稍后...");
            }

            @Override
            public void onRequestEnd(String result) {
                hideProgressDialog();
                try{
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        finish();
                    }
                    showToast(obj.optString("msg"));
                }catch (Exception e){
                    showToast("注册失败，请稍后再试！");
                }
            }

            @Override
            public void onRequestCancal() {
                hideProgressDialog();
                showToast("注册已取消！");
            }
        });
    }
}
