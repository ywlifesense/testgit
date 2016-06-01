package cn.js.nanhaistaffhome.views.home.content.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.simonvt.menudrawer.MenuDrawer;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.activitys.BaseActivity;
import cn.js.nanhaistaffhome.activitys.HomeActivity;
import cn.js.nanhaistaffhome.activitys.MainActivity;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnSimpleHttpRequestListener;
import cn.js.nanhaistaffhome.models.User;
import cn.js.nanhaistaffhome.utils.MyTextUtils;
import cn.js.nanhaistaffhome.views.home.content.ContentPage;
import cn.js.nanhaistaffhome.views.others.RoundImageView;

/**
 * Created by JS on 8/14/15.
 */
public class UserInfoView extends ContentPage{

    private EditText userNameEt;
    private EditText signEt;
    private EditText emailEt;
    private EditText realNameEt;
    private EditText sexEt;
    private EditText birthEt;
    private EditText introEt;
    private EditText fromEt;
    private EditText msnEt;
    private EditText qqEt;
    private EditText phoneEt;
    private EditText mobileEt;
    private Button toggleBtn;
//    private ImageButton leftBtn;
    private RoundImageView avatarView;
    private BaseActivity parant;
    private boolean hadInit;

    public UserInfoView(BaseActivity context){
        super(context);
        parant = context;
    }

    @Override
    public void init(){
        User user = StaffApplication.getInstance().getUser();
        initView(user);
        getUserInfo(user.getUserName());
        hadInit = true;
    }

    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();

        if (hadInit){
            User user = StaffApplication.getInstance().getUser();
            ImageLoader.getInstance().displayImage(HttpConstant.HOST + user.getAvatarUrl(), avatarView);
            setUserInfo(user);
        }
    }

    @Override
    public int getLayoutResource(){
        return R.layout.content_user_info;
    }

    private void getUserInfo(String name){
        HttpClient.getUserInfo(name, new OnSimpleHttpRequestListener() {
            @Override
            public void onRequestEnd(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.optInt("code",1)==0){
                        User user = User.build(obj.getJSONObject("data"));
                        StaffApplication.getInstance().setUser(user);
                        setUserInfo(user);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView(User user){
       super.initView();

        userNameEt = (EditText)findViewById(R.id.et_user_name);
        signEt = (EditText)findViewById(R.id.et_sign);
        emailEt = (EditText)findViewById(R.id.et_email);
        realNameEt = (EditText)findViewById(R.id.et_real_name);
        sexEt = (EditText)findViewById(R.id.et_sex);
        birthEt = (EditText)findViewById(R.id.et_birth);
        introEt = (EditText)findViewById(R.id.et_introduce);
        fromEt = (EditText)findViewById(R.id.et_from);
        msnEt = (EditText)findViewById(R.id.et_msn);
        qqEt = (EditText)findViewById(R.id.et_qq);
        phoneEt = (EditText)findViewById(R.id.et_phone);
        mobileEt = (EditText)findViewById(R.id.et_mobile);
        avatarView = (RoundImageView)findViewById(R.id.iv_user_avatar);

        sexEt.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
//            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    touch_flag++;
//                    if (touch_flag == 1) {
                        //自己业务
                        showTypeSelectDialog();
//                        touch_flag = 0;
//                    }
                }
                return false;
            }
        });

        birthEt.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
//            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                touch_flag++;
//                if (touch_flag == 2) {
                    //自己业务
                    showDateSelectDialog();
//                    touch_flag = 0;
//                }
                }
                return false;
            }
        });

        toggleBtn = (Button)findViewById(R.id.btn_edit);
        toggleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (canEdit) {//点击编辑
//                    changeEditState(true);
//                    toggleBtn.setText("上传");
//                } else {//点击上传
                    modifyUserInfo();
//                }
            }
        });

        ImageLoader.getInstance().displayImage(HttpConstant.HOST + user.getAvatarUrl(), avatarView);
        setUserInfo(user);
    }

    public void setUserAvatar(Bitmap bitmap){
        avatarView.setImageBitmap(bitmap);
    }

    private void setUserInfo(User user){
        userNameEt.setText(MyTextUtils.checkEmpty(user.getUserName()));
        signEt.setText(MyTextUtils.checkEmpty(user.getSign()));
        emailEt.setText(MyTextUtils.checkEmpty(user.getEmail()));
        realNameEt.setText(MyTextUtils.checkEmpty(user.getRealName()));
        String sex;
        switch (user.getSex()){
            case 0:
                sex = "女";
                break;
            case 1:
                sex = "男";
                break;
            default:
                sex = "保密";
                break;
        }
        sexEt.setText(sex);
        birthEt.setText(MyTextUtils.checkEmpty(user.getBirth()));
        introEt.setText(MyTextUtils.checkEmpty(user.getIntro()));
        fromEt.setText(MyTextUtils.checkEmpty(user.getComfrom()));
        msnEt.setText(MyTextUtils.checkEmpty(user.getMsn()));
        qqEt.setText(MyTextUtils.checkEmpty(user.getQq()));
        phoneEt.setText(MyTextUtils.checkEmpty(user.getPhone()));
        mobileEt.setText(MyTextUtils.checkEmpty(user.getMobile()));
    }

    private void showTypeSelectDialog() {
        final String[] sexTles = {"女","男","保密"};
        new AlertDialog.Builder(getContext())
                .setTitle("请选择")
                .setItems(sexTles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexEt.setText(sexTles[which]);
                    }
                }).create().show();
    }

    private void showDateSelectDialog(){
        Calendar calendar = Calendar.getInstance(Locale.CHINESE);
        calendar.set(1970,0,1);
        Dialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                birthEt.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void modifyUserInfo(){
        String userName = userNameEt.getText().toString();
        String sign = signEt.getText().toString();
        String email = emailEt.getText().toString();
        final String realName = realNameEt.getText().toString();
        String sex = sexEt.getText().toString();
        String birth = birthEt.getText().toString();
        String intro = introEt.getText().toString();
        String from = fromEt.getText().toString();
        String msn = msnEt.getText().toString();
        String qq = qqEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String mobile = mobileEt.getText().toString();

        String gender;
        if (sex.equals("女")){
            gender = "0";
        }else if(sex.equals("男")){
            gender = "1";
        }else {
            gender = "2";
        }

        if (!MyTextUtils.isEmail(email) && !TextUtils.isEmpty(email)){
            parant.showToast("邮箱格式不正确，请修改");
            return;
        }

        if (!MyTextUtils.isMobileNO(mobile) && !TextUtils.isEmpty(mobile)){
            parant.showToast("手机号码格式不正确，请修改");
            return;
        }

        if (!MyTextUtils.isNumeric(qq) && !TextUtils.isEmpty(qq)){
            parant.showToast("QQ号码格式不正确，请修改");
            return;
        }

        if (!MyTextUtils.isNumeric(phone) && !TextUtils.isEmpty(phone)){
            parant.showToast("电话号码格式不正确，请修改");
            return;
        }

        HttpClient.modifyUserInfo(userName, realName, email, gender, birth, qq,
                msn, from, intro, mobile, phone, sign,
                new OnHttpRequestListener() {
                    @Override
                    public void onRequestStart() {
                        parant.showProgressDialog("正在上传你的资料，请稍后...");
                    }

                    @Override
                    public void onRequestEnd(String result) {
                        parant.hideProgressDialog();
                        try {
                            JSONObject obj = new JSONObject(result);
                            parant.showToast(obj.optString("msg"));
                        }catch (Exception e){
                            e.printStackTrace();
                            parant.showToast("上传失败！");
                        }
                    }

                    @Override
                    public void onRequestCancal() {
                        parant.hideProgressDialog();
                    }
                }
        );
    }

}
