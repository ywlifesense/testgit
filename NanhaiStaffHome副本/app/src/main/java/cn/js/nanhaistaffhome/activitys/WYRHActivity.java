package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;

/**
 * Created by JS on 2016/3/21.
 * 我要入会
 */
public class WYRHActivity extends BaseActivity {

    private EditText nameEt;
    private EditText idEt;
    private EditText mobileEt;
    private EditText gzdwEt;
    private EditText ssghEt;
    private EditText addressEt;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyrh2);

        nameEt = (EditText)findViewById(R.id.et_name);
        idEt = (EditText)findViewById(R.id.et_id);
        mobileEt = (EditText)findViewById(R.id.et_mobile);
        gzdwEt = (EditText)findViewById(R.id.et_gzdw);
        ssghEt = (EditText)findViewById(R.id.et_ssgh);
        addressEt = (EditText)findViewById(R.id.et_address);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_commit:
                commit();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void commit(){
        String name = nameEt.getText().toString();
        String id = idEt.getText().toString();
        String mobile = mobileEt.getText().toString();
        final String gzdw = gzdwEt.getText().toString();
        String ssgh = ssghEt.getText().toString();
        String address = addressEt.getText().toString();

        if (TextUtils.isEmpty(name)){
            showToast("请填写姓名");
            return;
        }

        if (TextUtils.isEmpty(id)){
            showToast("请填写身份证号");
            return;
        }

        if (TextUtils.isEmpty(mobile)){
            showToast("请填写手机号");
            return;
        }

        if (TextUtils.isEmpty(gzdw)){
            showToast("请填写工作单位");
            return;
        }

        if (TextUtils.isEmpty(ssgh)){
            showToast("请填写所属镇街及村社区、工业园");
            return;
        }

        if (TextUtils.isEmpty(address)){
            showToast("请填写单位地址");
            return;
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("ctgId", "24");
        mp.put("isPub","1");
        mp.put("uname",name);
        mp.put("identity",id);
        mp.put("phone",mobile);
        mp.put("workunit",gzdw);
        mp.put("labour", ssgh);
        mp.put("workaddr", address);

        HttpClient.addGuestbook(mp, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {
                showProgressDialog("正在咨询，请稍后...");
            }

            @Override
            public void onRequestEnd(String result) {
                hideProgressDialog();
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code", 1) == 0) {
                        addressEt.setText("");
                        ssghEt.setText("");
                        gzdwEt.setText("");
                        mobileEt.setText("");
                        idEt.setText("");
                        nameEt.setText("");
                        showToast("成功");
                    }else {
                        showToast("失败");
                    }
                } catch (Exception e) {
                    showToast("失败");
                }
            }

            @Override
            public void onRequestCancal() {
                showToast("取消");
                hideProgressDialog();
            }
        });
    }
}
