package cn.js.nanhaistaffhome.views.home.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpConstant;
import cn.js.nanhaistaffhome.interfaces.OnMenuClickListener;
import cn.js.nanhaistaffhome.views.others.RoundImageView;

/**
 * Created by JS on 8/4/15.
 */
public class MainMenu extends RelativeLayout {

    private RoundImageView avatarView;
    private TextView userNameTv;
    private MainMenuItem[] items;
    private MainMenuItem lastSelectedItem;
    private ViewGroup setBtn;
    private OnMenuClickListener onMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.onMenuClickListener = listener;
    }

    public MainMenu(Context context) {
        super(context);
        init(context);
    }

    public MainMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.menu_main, this);
        avatarView = (RoundImageView) findViewById(R.id.iv_user_avatar);
        userNameTv = (TextView)findViewById(R.id.tv_user_name);

        items = new MainMenuItem[5];
        items[0] = (MainMenuItem) findViewById(R.id.item_home);
        items[1] = (MainMenuItem) findViewById(R.id.item_user_info);
        items[2] = (MainMenuItem) findViewById(R.id.item_pwd_modify);
        items[3] = (MainMenuItem) findViewById(R.id.item_member_auth);
        items[4] = (MainMenuItem) findViewById(R.id.item_exit);

        for (int i=0;i<items.length;i++){
            items[i].setOnClickListener(menuItemClickListener);
        }

        setBtn = (ViewGroup) findViewById(R.id.btn_setting);
        setBtn.setOnClickListener(setBtnClickListener);
    }

    public void setUserName(String name){
        userNameTv.setText(name);
    }

    public void setAvatar(String url){
        ImageLoader.getInstance().displayImage(HttpConstant.HOST+url,avatarView);
    }

    public void setSelectedItem(int index) {
        if (lastSelectedItem != null) {
            lastSelectedItem.setSelected(false);
        }
        items[index].setSelected(true);
        lastSelectedItem = items[index];
    }

    private boolean itemSelected(int index) {
        if (onMenuClickListener != null && index >= 0) {
            return onMenuClickListener.onMenuItemSelected(index);
        }
        return true;
    }

    private OnClickListener menuItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            String tag = view.getTag().toString();
            int index = Integer.parseInt(tag);
            if (index >=0 && itemSelected(index)){
                lastSelectedItem.setSelected(false);
                items[index].setSelected(true);
                lastSelectedItem = (MainMenuItem) view;
            }
        }
    };

    private OnClickListener setBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onMenuClickListener != null) {
                onMenuClickListener.onSettingBtnClick();
            }
        }
    };

}
