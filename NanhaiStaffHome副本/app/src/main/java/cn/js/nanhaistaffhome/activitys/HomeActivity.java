package cn.js.nanhaistaffhome.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.js.nanhaistaffhome.ActivityManager;
import cn.js.nanhaistaffhome.Constant;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnMenuClickListener;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.models.User;
import cn.js.nanhaistaffhome.utils.BitmapUtil;
import cn.js.nanhaistaffhome.utils.SharedPreferenceUtil;
import cn.js.nanhaistaffhome.views.home.content.auth.MemberAuthView;
import cn.js.nanhaistaffhome.views.home.content.main.MainView;
import cn.js.nanhaistaffhome.views.home.content.modifyPwd.ModifyPwdView;
import cn.js.nanhaistaffhome.views.home.content.user.UserInfoView;
import cn.js.nanhaistaffhome.views.home.menu.MainMenu;

/**
 * Created by JS on 8/30/15.
 * 新首页(已抛弃？？还是未改版？)
 */
public class HomeActivity extends BaseActivity {

    public MenuDrawer menuDrawer;
    private MainView mainView;
    private MainMenu mainMenu;
    private UserInfoView userInfoView;
    private ModifyPwdView modifyPwdView;
    private MemberAuthView memberAuthView;
    private ImageView avatarView;
    private int contentViewIndex;
    private SharedPreferenceUtil spu;
    private boolean isLogined;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        spu = SharedPreferenceUtil.getInstance(this);
        isLogined = spu.getBoolean(Constant.SP_KEY_IS_LOGIN,false);
        if (isLogined) {
            getUserInfo(spu.getString(Constant.SP_KEY_USER_NAME));
        }

        menuDrawer = MenuDrawer.attach(this);
        //菜单
        mainMenu = new MainMenu(this);
        mainMenu.setUserName(StaffApplication.getInstance().getUser().getUserName());
        mainMenu.setOnMenuClickListener(onMenuClickListener);
        menuDrawer.setMenuView(mainMenu);

        //首页
        mainView = new MainView(this);
        menuDrawer.setContentView(mainView);
        if (!isLogined){
            mainView.showActivityFrame(false);
        }

        menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        menuDrawer.setDropShadowEnabled(false);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        menuDrawer.setMenuSize(metrics.widthPixels * 4 / 5);

        avatarView = (ImageView)findViewById(R.id.iv_user_avatar);

        mainMenu.setSelectedItem(0);
        contentViewIndex = 0;

        checkNewest();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.activity_frame://活动中心
                if (mainView.newestActi!=null){
                    ActivityManager.toActiDetailActivity(this,mainView.newestActi.getId());
                }
                break;
            case R.id.btn_sqxx://社区信息
                ActivityManager.toMainActivity(this,0,0);
                break;
            case R.id.btn_whqy://维护权益
                ActivityManager.toMainActivity(this,1,0);
                break;
            case R.id.btn_fwcs://服务超市
                ActivityManager.toMainActivity(this,2,0);
                break;
            case R.id.btn_hdzx://活动中心
                if (!isLogined){
                    showUnLoginAlertDialog();
                    return;
                }
                ActivityManager.toMainActivity(this,3,0);
                break;
            case R.id.btn_zhzx://综合咨询
                ActivityManager.toMainActivity(this,4,0);
                break;
            case R.id.btn_persion_info://个人信息
                if (isLogined) {
                    contentViewIndex = 1;
                    if (userInfoView == null) {
                        userInfoView = new UserInfoView(HomeActivity.this);
                        userInfoView.setTitle("个人消息");
                        userInfoView.init();
                    }
                    menuDrawer.setContentView(userInfoView);
                    mainMenu.setSelectedItem(1);
                }else {
                    showUnLoginAlertDialog();
                }
                break;
            case R.id.btn_flfg://法律法规
                ActivityManager.toMainActivity(this,1,0);
                break;
            case R.id.btn_flcs://法律常识
                ActivityManager.toMainActivity(this,1,1);
                break;
            case R.id.btn_flws://法律文书
                ActivityManager.toMainActivity(this,1,2);
                break;
            case R.id.btn_flzx://常见问题
                ActivityManager.toMainActivity(this,1,3);
                break;
            case R.id.iv_user_avatar:
                String[] items = {"相册","拍照"};
                new AlertDialog.Builder(this)
                        .setTitle("请选择")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0){
                                    getImageFromAlbum();
                                }else if (i == 1){
                                    getImageFromCamera();
                                }
                            }
                        }).create().show();
                break;
        }
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            uploadImage(BitmapUtil.getBitmapFromUri(uri,HomeActivity.this));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA ) {
            Uri uri = data.getData();
            if(uri == null){
                //use bundle to get data
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap
                    //spath :生成图片取个名字和路径包含类型
//                    saveImage(Bitmap photo, String spath);

                    uploadImage(photo);
                } else {
                    Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                uploadImage(BitmapUtil.getBitmapFromUri(uri,HomeActivity.this));
            }
        }else if (requestCode == REQUEST_CODE_SEARCH_COMPANY){
            if (data!=null){
                String result = data.getStringExtra("result");
                memberAuthView.setCompanyName(result);
            }
        }
    }

//    public static boolean saveImage(Bitmap photo, String spath) {
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(
//                    new FileOutputStream(spath, false));
//            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

    private void uploadImage(final Bitmap bitmap){
        if (bitmap == null){
            showToast("找不到图片，请稍后再试");
            return;
        }
        SharedPreferenceUtil spf = SharedPreferenceUtil.getInstance(this);
        String username = spf.getString(Constant.SP_KEY_USER_NAME);
        String pwd = spf.getString(Constant.SP_KEY_PASSWORD);
        String imageBase64 = "";
        try{
            imageBase64 = URLEncoder.encode(BitmapUtil.bitmapToBase64(bitmap),"utf-8");
        }catch (UnsupportedEncodingException e){
            showToast("不支持该图片类型，上传失败！");
        }
        HttpClient.uploadImage(username, pwd, imageBase64, new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {
                showProgressDialog("正在上传头像，请稍后...");
            }

            @Override
            public void onRequestEnd(String result) {
                hideProgressDialog();
                try{
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        showToast("上传成功");
                        avatarView.setImageBitmap(bitmap);
                        userInfoView.setUserAvatar(bitmap);
                    }else {
                        showToast(obj.optString("msg"));
                    }
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (menuDrawer.isMenuVisible()) {
                menuDrawer.closeMenu();
                return false;
            }else {

                if ( contentViewIndex == 0){
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("温馨提示")
                            .setMessage("你确定要退出南海职工之家吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create().show();
                }else {
                    contentViewIndex = 0;
                    mainMenu.setSelectedItem(0);
                    menuDrawer.setContentView(mainView);
//                    menuDrawer.openMenu();
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private OnMenuClickListener onMenuClickListener = new OnMenuClickListener() {
        @Override
        public void onSettingBtnClick() {

        }

        @Override
        public boolean onMenuItemSelected(int index) {

            if (index >= 1 && index <= 3){
                if (!isLogined){
                    showUnLoginAlertDialog();
                    return false;
                }
            }

            switch (index) {
                case 0://首页
                    contentViewIndex = 0;
                    if (mainView == null){
                        mainView = new MainView(HomeActivity.this);
                    }
                    menuDrawer.setContentView(mainView);
                    menuDrawer.setOnDrawerStateChangeListener(null);
                    break;
                case 1://个人消息
                    contentViewIndex = 1;
                    if (userInfoView == null){
                        userInfoView = new UserInfoView(HomeActivity.this);
                        userInfoView.setTitle("个人消息");
                    }
                    menuDrawer.setContentView(userInfoView);
                    menuDrawer.setOnDrawerStateChangeListener(userInfoView);
                    break;
                case 2://密码修改
                    contentViewIndex = 2;
                    if (modifyPwdView == null){
                        modifyPwdView = new ModifyPwdView(HomeActivity.this);
                        modifyPwdView.setTitle("密码修改");
                    }
                    menuDrawer.setContentView(modifyPwdView);
                    menuDrawer.setOnDrawerStateChangeListener(modifyPwdView);
                    break;
                case 3://会员认证
                    contentViewIndex = 3;
                    if (memberAuthView == null){
                        memberAuthView = new MemberAuthView(HomeActivity.this);
                        memberAuthView.setTitle("申请正式会员");
                    }
                    menuDrawer.setContentView(memberAuthView);
                    menuDrawer.setOnDrawerStateChangeListener(memberAuthView);
                    break;
                case 4://退出
                    SharedPreferenceUtil spu = SharedPreferenceUtil.getInstance(HomeActivity.this);
                    spu.putBoolean(Constant.SP_KEY_IS_LOGIN,false);
                    spu.putString(Constant.SP_KEY_USER_NAME, "");
                    spu.putString(Constant.SP_KEY_PASSWORD,"");
                    StaffApplication.getInstance().setUser(null);
                    ActivityManager.toLoginActivity(HomeActivity.this);
                    finish();
                    break;
            }
            if (index != 4) {
                menuDrawer.closeMenu();
            }

            return true;
        }
    };

    private void getUserInfo(String name){
        HttpClient.getUserInfo(name, new OnSimpleHttpRequestListener() {
            @Override
            public void onRequestEnd(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        User user = User.build(obj.getJSONObject("data"));
                        StaffApplication.getInstance().setUser(user);
                        mainMenu.setAvatar(user.getAvatarUrl());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkNewest(){
        HttpClient.getNewestVersion(new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {

            }

            @Override
            public void onRequestEnd(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.optInt("code") == 0) {
                        JSONObject obj = object.getJSONArray("data").getJSONObject(0);
                        double versionCode = obj.optDouble("version");
                        if (versionCode > getAppVersionName()) {
                            String url = obj.optString("download");
                            showUpDateAlertDialog(versionCode, url);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestCancal() {

            }
        });
    }

    /**
     * 返回当前程序版本名
     */
    public double getAppVersionName() {
        double versioncode = 0;
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    public void showUpDateAlertDialog(double versionName,final String url){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("温馨提示")
                .setMessage("当前最新版本为" + versionName + "，请更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
    }
}
