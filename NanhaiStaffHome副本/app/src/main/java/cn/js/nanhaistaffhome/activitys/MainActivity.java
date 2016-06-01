package cn.js.nanhaistaffhome.activitys;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONObject;

import java.io.File;
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
import cn.js.nanhaistaffhome.utils.FileUtil;
import cn.js.nanhaistaffhome.utils.SharedPreferenceUtil;
import cn.js.nanhaistaffhome.views.home.content.auth.MemberAuthView;
import cn.js.nanhaistaffhome.views.home.content.modifyPwd.ModifyPwdView;
import cn.js.nanhaistaffhome.views.home.content.user.UserInfoView;
import cn.js.nanhaistaffhome.views.home.menu.MainMenu;
import cn.js.nanhaistaffhome.views.main.ContentView;

/**
 * Created by JS on 8/3/15.
 * 旧首页
 */
public class MainActivity extends BaseActivity {

    public MenuDrawer menuDrawer;
    protected ContentView contentView;
    private MainMenu mainMenu;
    private UserInfoView userInfoView;
    private ModifyPwdView modifyPwdView;
    private MemberAuthView memberAuthView;
    private ImageView avatarView;
    private int contentViewIndex;
    private SharedPreferenceUtil spu;
    private boolean isLogined;
    private Uri cameraPhotoUri;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        int parent = getIntent().getIntExtra("parent",0);
//        int child = getIntent().getIntExtra("child",0);
//
//        contentView = new ContentView(this,parent,child);
//        setContentView(contentView);

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
//        mainView = new MainView(this);
//        menuDrawer.setContentView(mainView);
//        if (!isLogined){
//            mainView.showActivityFrame(false);
//        }

        contentView = new ContentView(this,0,0);
        menuDrawer.setContentView(contentView);

        menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
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
            final Intent takePictureImIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            cameraPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureImIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraPhotoUri);
            startActivityForResult(takePictureImIntent,REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == RESULT_OK){
             if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                Uri uri = data.getData();
                uploadImage(FileUtil.toFile(this, uri));
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA ) {
                if(cameraPhotoUri == null){
                    showToast("不能找到图片");
                }else{
                    uploadImage(FileUtil.toFile(this,cameraPhotoUri));
                }
            }
        }
    }

    private void uploadImage(final File file){
        if (file == null || !file.exists()){
            showToast("找不到图片，请稍后再试");
            return;
        }

        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        final Bitmap scaleBmp = BitmapUtil.zoomBitmap(bmp,0.3f);

        SharedPreferenceUtil spf = SharedPreferenceUtil.getInstance(this);
        String username = spf.getString(Constant.SP_KEY_USER_NAME);
        String pwd = spf.getString(Constant.SP_KEY_PASSWORD);
        String imageBase64 = "";
        try{
            imageBase64 = URLEncoder.encode(BitmapUtil.bitmapToBase64(scaleBmp), "utf-8");
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
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code", 1) == 0) {
                        showToast("上传成功");
                        avatarView.setImageBitmap(scaleBmp);
                        userInfoView.setUserAvatar(scaleBmp);
                    } else {
                        showToast(obj.optString("msg"));
                    }
                } catch (Exception e) {
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
                    menuDrawer.setContentView(contentView);
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
                    menuDrawer.setContentView(contentView);
                    menuDrawer.setOnDrawerStateChangeListener(null);
                    break;
                case 1://个人消息
                    contentViewIndex = 1;
                    if (userInfoView == null){
                        userInfoView = new UserInfoView(MainActivity.this);
                        userInfoView.setTitle("个人消息");
                    }
                    menuDrawer.setContentView(userInfoView);
                    menuDrawer.setOnDrawerStateChangeListener(userInfoView);
                    break;
                case 2://密码修改
                    contentViewIndex = 2;
                    if (modifyPwdView == null){
                        modifyPwdView = new ModifyPwdView(MainActivity.this);
                        modifyPwdView.setTitle("密码修改");
                    }
                    menuDrawer.setContentView(modifyPwdView);
                    menuDrawer.setOnDrawerStateChangeListener(modifyPwdView);
                    break;
                case 3://会员认证
                    contentViewIndex = 3;
                    if (memberAuthView == null){
                        memberAuthView = new MemberAuthView(MainActivity.this);
                        memberAuthView.setTitle("申请正式会员");
                    }
                    menuDrawer.setContentView(memberAuthView);
                    menuDrawer.setOnDrawerStateChangeListener(memberAuthView);
                    break;
                case 4://退出
                    SharedPreferenceUtil spu = SharedPreferenceUtil.getInstance(MainActivity.this);
                    spu.putBoolean(Constant.SP_KEY_IS_LOGIN,false);
                    spu.putString(Constant.SP_KEY_USER_NAME, "");
                    spu.putString(Constant.SP_KEY_PASSWORD,"");
                    StaffApplication.getInstance().setUser(null);
                    ActivityManager.toLoginActivity(MainActivity.this);
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
