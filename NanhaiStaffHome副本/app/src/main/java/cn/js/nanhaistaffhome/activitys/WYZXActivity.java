package cn.js.nanhaistaffhome.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;

/**
 * Created by JS on 2016/3/21.
 */
public class WYZXActivity extends BaseActivity implements View.OnClickListener {

    private String[] SexItems = {"男", "女"};
    private EditText sexEt;
    private EditText nameEt;
    private EditText mobileEt;
    private EditText emailEt;
    private EditText titleEt;
    private EditText contentEt;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyzx2);

        nameEt = (EditText) findViewById(R.id.et_name);
        sexEt = (EditText) findViewById(R.id.et_sex);
        mobileEt = (EditText) findViewById(R.id.et_mobile);
        emailEt = (EditText) findViewById(R.id.et_email);
        titleEt = (EditText) findViewById(R.id.et_title);
        contentEt = (EditText) findViewById(R.id.et_content);
        sexEt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_sex:
                showSexSelectDialog();
                break;
            case R.id.btn_commit:
                commit();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void showSexSelectDialog() {
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setItems(SexItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexEt.setText(SexItems[which]);
                    }
                }).create().show();
    }

    private void commit() {
        String name = nameEt.getText().toString();
        String sex = sexEt.getText().toString();
        String mobile = mobileEt.getText().toString();
        String email = emailEt.getText().toString();
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showToast("请填写姓名");
            return;
        }

        if (TextUtils.isEmpty(sex)) {
            showToast("请选择性别");
            return;
        }

        if (TextUtils.isEmpty(mobile)) {
            showToast("请填写手机号");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            showToast("请填写邮箱");
            return;
        }

        if (TextUtils.isEmpty(title)) {
            showToast("请填写主题");
            return;
        }

        if (TextUtils.isEmpty(content)) {
            showToast("请填写求助信息");
            return;
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("ctgId", "25");
        mp.put("isPub", "1");
        mp.put("uname", name);
        mp.put("sex", sex);
        mp.put("phone", mobile);
        mp.put("email", email);
        mp.put("title", title);
        mp.put("content", content);

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
                        titleEt.setText("");
                        contentEt.setText("");
                        emailEt.setText("");
                        mobileEt.setText("");
                        sexEt.setText("");
                        nameEt.setText("");
                        showToast("成功");
                    } else {
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
