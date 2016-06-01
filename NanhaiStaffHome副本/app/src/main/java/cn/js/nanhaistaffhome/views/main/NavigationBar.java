package cn.js.nanhaistaffhome.views.main;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.interfaces.OnNaviBarActionListener;

/**
 * Created by JS on 8/27/15.
 */
public class NavigationBar extends LinearLayout {

    private TextView titleView;
    private ImageButton leftBtn;
    private ImageButton cancelBtn;
    private Button rightBtn;
    private EditText searchEt;
    private OnNaviBarActionListener listener;
    private Handler handler = new Handler();
    private long lastSearchTime;
    private final static long SEARCH_SPEED = 300;

    public void setOnNaviBarActionListener(OnNaviBarActionListener listener) {
        this.listener = listener;
    }

    public NavigationBar(Context context) {
        super(context);
        init(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this);
        leftBtn = (ImageButton) findViewById(R.id.btn_left);
        cancelBtn = (ImageButton)findViewById(R.id.btn_cancel);
        rightBtn = (Button) findViewById(R.id.btn_right);
        searchEt = (EditText) findViewById(R.id.et_search);
        titleView = (TextView) findViewById(R.id.tv_title);

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeftBtnClick();
                }
            }
        });

        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRightBtnClick();
                }
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSearch();
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen){
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                long timeMillis = System.currentTimeMillis();
                if (timeMillis - lastSearchTime < SEARCH_SPEED) {
                    handler.removeCallbacks(runnable);
                }
                handler.postDelayed(runnable, SEARCH_SPEED);
                lastSearchTime = timeMillis;
            }
        });

        searchEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ObjectAnimator.ofInt(cancelBtn,"visibility",GONE,VISIBLE).setDuration(150).start();
                } else {
                    ObjectAnimator.ofInt(cancelBtn,"visibility",VISIBLE,GONE).setDuration(150).start();
                }
                if (listener != null) {
                    listener.onFocusChange(b);
                }
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null) {
                listener.onSearch(searchEt.getText().toString());
            }
        }
    };

    public void clearSearch(){
        searchEt.setText("");
        searchEt.clearFocus();
    }

    public void setTitle(String title) {
        searchEt.setVisibility(GONE);
        titleView.setVisibility(VISIBLE);
        titleView.setText(title);
    }

    public String getSearchContent(){
        return searchEt.getText().toString();
    }

    public void showSearchInput() {
        titleView.setVisibility(GONE);
        searchEt.setVisibility(VISIBLE);
        rightBtn.setVisibility(GONE);
    }

    public void hideSearchInput() {
        searchEt.setVisibility(GONE);
        titleView.setVisibility(VISIBLE);
    }

    public void setRightBtnTitle(String title) {
        rightBtn.setVisibility(VISIBLE);
        rightBtn.setText(title);
    }

    public void hideRightBtn(){
        rightBtn.setVisibility(GONE);
    }
}
