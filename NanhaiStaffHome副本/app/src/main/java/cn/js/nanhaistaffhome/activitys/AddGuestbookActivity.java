package cn.js.nanhaistaffhome.activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.js.nanhaistaffhome.Constant;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;

/**
 * Created by JS on 8/24/15.
 */
public class AddGuestbookActivity extends BaseActivity {

    private String[] isPubItems = {"不允许公开", "允许公开"};

    private EditText channelEt;
    private EditText titleEt;
    private EditText emailEt;
    private EditText phoneEt;
    private EditText qqEt;
    private EditText isPubEt;
    private EditText contentEt;
    private int index = 0;
    private int isPub = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guestbook);
        channelEt = (EditText) findViewById(R.id.et_channel_selected);
        titleEt = (EditText) findViewById(R.id.et_title);
        emailEt = (EditText) findViewById(R.id.et_email);
        phoneEt = (EditText) findViewById(R.id.et_mobile);
        qqEt = (EditText) findViewById(R.id.et_qq);
        isPubEt = (EditText) findViewById(R.id.et_is_pub);
        contentEt = (EditText) findViewById(R.id.et_content);

        isPubEt.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    touch_flag++;
                    if (touch_flag == 2) {
                        //自己业务
                        showIsPubDialog();
                        touch_flag = 0;
                    }
                }
                return false;
            }
        });
        isPubEt.setText(isPubItems[isPub]);

        index = getIntent().getIntExtra("index", 0);
//        index = 0;
        channelEt.setText(Constant.CTG_NAMES.get(index));
        changeUI(index);
    }

    private void changeUI(int index) {
        if (index == 3) {//在线维权
            emailEt.setVisibility(View.GONE);
            phoneEt.setVisibility(View.GONE);
            qqEt.setVisibility(View.GONE);
            isPubEt.setVisibility(View.GONE);
        } else if (index == 2) {//法律援助
            emailEt.setVisibility(View.VISIBLE);
            phoneEt.setVisibility(View.VISIBLE);
            qqEt.setVisibility(View.VISIBLE);
            isPubEt.setVisibility(View.GONE);
        } else if (index == 1) {//投诉建议
            emailEt.setVisibility(View.VISIBLE);
            phoneEt.setVisibility(View.VISIBLE);
            qqEt.setVisibility(View.VISIBLE);
            isPubEt.setVisibility(View.VISIBLE);
        } else if (index == 0) {//在线咨询
            emailEt.setVisibility(View.VISIBLE);
            phoneEt.setVisibility(View.VISIBLE);
            qqEt.setVisibility(View.VISIBLE);
            isPubEt.setVisibility(View.VISIBLE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_commit:
                addWords(index);
                break;
            default:
                break;
        }
    }

    private void showTypeSelectDialog() {
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setItems((String[]) Constant.CTG_NAMES.toArray(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeUI(which);
                        channelEt.setText(Constant.CTG_NAMES.get(which));
                    }
                }).create().show();
    }

    private void showIsPubDialog() {
        new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setItems(isPubItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isPub = which;
                        isPubEt.setText(isPubItems[which]);
                    }
                }).create().show();
    }

    private boolean checkEmpty(int index) {

        if (TextUtils.isEmpty(titleEt.getText().toString())) {
            return true;
        }

        if (TextUtils.isEmpty(contentEt.getText().toString())) {
            return true;
        }

        if (index == 0 || index == 1 || index == 2) {
            if (TextUtils.isEmpty(emailEt.getText().toString())) {
                return true;
            }
            if (TextUtils.isEmpty(phoneEt.getText().toString())) {
                return true;
            }
            if (TextUtils.isEmpty(qqEt.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    private void addWords(int index) {
        if (checkEmpty(index)) {
            showToast("请完善好资料，再发表留言");
            return;
        }

        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();
        String email = emailEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String qq = qqEt.getText().toString();

        if (!chechData(index,title,content,email,phone,qq)){
            return;
        }

        Map<String, String> mp = new HashMap<>();
        mp.put("ctgId", String.valueOf(Constant.CTG_IDS.get(index)));
        mp.put("title", title);
        mp.put("content", content);
        if (index != 3) {
            mp.put("email",email);
            mp.put("phone",phone);
            mp.put("qq",qq);
        }else {
            mp.put("email","");
            mp.put("phone","");
            mp.put("qq", "");
        }

        if (index == 2) {
            mp.put("isPub",String.valueOf(isPub));
        }else {
            mp.put("isPub",String.valueOf(1));
        }

        HttpClient.addGuestbook(mp, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {
                showProgressDialog("正在咨询，请稍后...");
            }

            @Override
            public void onRequestEnd(String result) {
                hideProgressDialog();
                try{
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        titleEt.setText("");
                        contentEt.setText("");
                        emailEt.setText("");
                        phoneEt.setText("");
                        qqEt.setText("");
                        isPub = 1;
                        isPubEt.setText(isPubItems[isPub]);
                    }
                    showToast(obj.optString("msg"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestCancal() {
                hideProgressDialog();
            }
        });
    }

    public boolean chechData(int index,String title,String content
            ,String email,String phone,String qq){

        return true;
    }

}
