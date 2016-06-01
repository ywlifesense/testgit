package cn.js.nanhaistaffhome.views.home.content.modifyPwd;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONObject;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.activitys.BaseActivity;
import cn.js.nanhaistaffhome.activitys.HomeActivity;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.models.User;
import cn.js.nanhaistaffhome.views.home.content.ContentPage;

/**
 * Created by JS on 8/18/15.
 */
public class ModifyPwdView extends ContentPage{

    private BaseActivity parent;
    private EditText oldPwdEt;
    private EditText newPwdEt;
    private EditText comfirmEt;
    private Button modifyBtn;

    public ModifyPwdView(BaseActivity context) {
        super(context);
        parent = context;
    }

    public void init(){
        initView();
    }

    public int getLayoutResource(){
        return R.layout.content_modify_pwd;
    }

//    @Override
//    public void onDetachedFromWindow(){
//        super.onDetachedFromWindow();
//        oldPwdEt.setText("");
//        newPwdEt.setText("");
//        comfirmEt.setText("");
//    }

    public void initView(){
        super.initView();

        oldPwdEt = (EditText) findViewById(R.id.et_old_pwd);
        newPwdEt = (EditText) findViewById(R.id.et_new_pwd);
        comfirmEt = (EditText) findViewById(R.id.et_pwd_comfirm);
        modifyBtn = (Button) findViewById(R.id.btn_modify);
        modifyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = StaffApplication.getInstance().getUser().getUserName();
                String oldPwd = oldPwdEt.getText().toString();
                String newPwd = newPwdEt.getText().toString();
                String comfirmPwd = comfirmEt.getText().toString();

                if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(comfirmPwd)) {
                    parent.showToast("请完善资料");
                    return;
                }

                if (!newPwd.equals(comfirmPwd)) {
                    parent.showToast("两次输入的密码不一致");
                    return;
                }

                if (oldPwd.equals(newPwd)) {
                    parent.showToast("新密码不能与原密码一致");
                    return;
                }

                modifyPassword(userName,oldPwd,newPwd);

            }
        });
    }

    private void modifyPassword(String userName, String oldPwd, String newPwd) {
        HttpClient.modifyPwd(userName, oldPwd, newPwd,
                new OnHttpRequestListener() {
                    @Override
                    public void onRequestStart() {
                        parent.showProgressDialog("正在修改密码，请稍后...");
                    }

                    @Override
                    public void onRequestEnd(String result) {
                        parent.hideProgressDialog();
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.optInt("code", 1) == 0) {
                                oldPwdEt.setText("");
                                newPwdEt.setText("");
                                comfirmEt.setText("");
                            }
                            parent.showToast(obj.optString("msg"));
                        }catch (Exception e){
                            parent.showToast("修改密码失败，请稍后再试");
                        }
                    }

                    @Override
                    public void onRequestCancal() {
                        parent.hideProgressDialog();
                        parent.showToast("修改取消");
                    }
                }
        );
    }
}
