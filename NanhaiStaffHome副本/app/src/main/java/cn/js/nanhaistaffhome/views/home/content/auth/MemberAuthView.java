package cn.js.nanhaistaffhome.views.home.content.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.activitys.BaseActivity;
import cn.js.nanhaistaffhome.activitys.HomeActivity;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.utils.MyTextUtils;
import cn.js.nanhaistaffhome.views.home.content.ContentPage;

/**
 * Created by JS on 8/18/15.
 */
public class MemberAuthView extends ContentPage{

    private final static String[] ZZMMS = {"党员","团员","群众","其他"};
    private final static String[] EDUCATIONS = {"博士硕士","研究生","本科","高中","初中","小学","专科、专职","其他"};
    private String[] COMPANYLIST;

    private BaseActivity parent;

    public MemberAuthView(BaseActivity context){
        super(context);
        parent = context;
    }

    @Override
    public void init(){
        checkAuth(StaffApplication.getInstance().getUser().getId());
    }

    @Override
    public int getLayoutResource(){
        return R.layout.content_auth;
    }

    private void auth(String userName, String idNo, String memberid,
                      String unionName, String politicsStatus, String education,
                      String telephone){
        HttpClient.auth(userName, idNo, memberid,unionName,politicsStatus,education,telephone,
            new OnHttpRequestListener() {
                @Override
                public void onRequestStart() {
                    parent.showProgressDialog("正在认证，请稍后...");
                }

                @Override
                public void onRequestEnd(String result) {
                    parent.hideProgressDialog();
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.optInt("code", 1) == 0) {
                            View view = findViewById(R.id.content_auth_input);
                            view.setVisibility(GONE);
                            showAuthInfo(obj.optJSONObject("data"));
                        }
                        parent.showToast(obj.optString("msg"));
                    } catch (Exception e) {
                        parent.showToast("认证失败，请稍后再试");
                    }
                }

                @Override
                public void onRequestCancal() {
                    parent.hideProgressDialog();
                    parent.showToast("认证取消");
                }
            }
        );
    }

    private void checkAuth(long userId){
        HttpClient.checkAuth(userId, new OnSimpleHttpRequestListener() {
            @Override
            public void onRequestEnd(String result) {
                showLoading(false);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code", 1) == 0) {
                        showAuthInfo(obj.getJSONObject("data"));
//                        showAuthInputView();
                    } else {
                        showAuthInputView();
                    }
                } catch (Exception e) {
                    showAuthInputView();
                }
            }

            @Override
            public void onRequestCancal() {
                showLoading(false);
                showAuthInputView();
            }
        });
    }

    private void showAuthInfo(JSONObject obj){
        if (obj != null){
            initView();

            findViewById(R.id.content_auth_info).setVisibility(VISIBLE);
            ((TextView)findViewById(R.id.tv_user_name)).setText(StaffApplication.getInstance().getUser().getUserName());
            ((TextView)findViewById(R.id.tv_real_name)).setText(MyTextUtils.checkEmpty(obj.optString("NAME"), "暂无"));
            ((TextView)findViewById(R.id.tv_belong_staff)).setText(MyTextUtils.checkEmpty(obj.optString("UNIONNAME"),"暂无"));
            ((TextView)findViewById(R.id.tv_zzmm)).setText(MyTextUtils.checkEmpty(obj.optString("POLITICS_STATUS"),"暂无"));
            ((TextView)findViewById(R.id.tv_edu_background)).setText(MyTextUtils.checkEmpty(obj.optString("EDUCATION"),"暂无"));
            ((TextView)findViewById(R.id.tv_phone)).setText(MyTextUtils.checkEmpty(obj.optString("TELEPHONE"), "暂无"));

        }else {
            showAuthInputView();
        }
    }

    private void showAuthInputView(){
        initView();

        findViewById(R.id.content_auth_input).setVisibility(VISIBLE);
        final EditText nameEt = (EditText)findViewById(R.id.et_real_name);
        final EditText idEt = (EditText)findViewById(R.id.et_id);

        findViewById(R.id.btn_auth).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameEt.getText().toString();
                String idNo = idEt.getText().toString();
                String memberid = String.valueOf(StaffApplication.getInstance().getUser().getId());
                String unionName = ((TextView)findViewById(R.id.btn_company)).getText().toString();
                String politicsStatus = ((TextView)findViewById(R.id.btn_zzmm)).getText().toString();
                String education = ((TextView)findViewById(R.id.btn_education)).getText().toString();
                String telephone = ((EditText)findViewById(R.id.et_tel)).getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(idNo) || TextUtils.isEmpty(memberid)
                        || TextUtils.isEmpty(unionName) || TextUtils.isEmpty(politicsStatus) || TextUtils.isEmpty(education)
                        || TextUtils.isEmpty(telephone)) {
                    parent.showToast("请完善资料");
                    return;
                }
                auth(userName,idNo,memberid,unionName,politicsStatus,education,telephone);
            }
        });

        findViewById(R.id.btn_company).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.toSearchCompanyActivity(parent,parent.REQUEST_CODE_SEARCH_COMPANY);
            }
        });

        findViewById(R.id.btn_zzmm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showZZMMDialog();
            }
        });

        findViewById(R.id.btn_education).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showEducationDialog();
            }
        });
    }

    public void setCompanyName(String name){
        ((TextView)findViewById(R.id.btn_company)).setText(name);
    }

    private void showZZMMDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("请选择")
                .setItems(ZZMMS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView)findViewById(R.id.btn_zzmm)).setText(ZZMMS[which]);
                    }
                }).create().show();
    }

    private void showEducationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("请选择")
                .setItems(EDUCATIONS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView)findViewById(R.id.btn_education)).setText(EDUCATIONS[which]);
                    }
                }).create().show();
    }

    private void showCompanyListDialog(final String[] list){
        new AlertDialog.Builder(getContext())
                .setTitle("请选择")
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView)findViewById(R.id.btn_company)).setText(list[which]);
                    }
                }).create().show();
    }

//    private void showCompanyListDialog(){
//        if (COMPANYLIST!=null && COMPANYLIST.length>0){
//            showCompanyListDialog(COMPANYLIST);
//            return;
//        }
//
//    }

}
