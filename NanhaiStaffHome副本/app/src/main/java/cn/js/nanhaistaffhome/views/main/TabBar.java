package cn.js.nanhaistaffhome.views.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.interfaces.OnTabBarSelectedListener;

/**
 * Created by JS on 8/6/15.
 */
public class TabBar extends RelativeLayout {

    private OnTabBarSelectedListener listener;
    private int currentSelected;
    private RadioGroup radioGroup;

    public void setOnTabBarSelectedListener(OnTabBarSelectedListener listener) {
        this.listener = listener;
    }

    public TabBar(Context context) {
        super(context);
        init(context, null);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.tab_bar, this);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_item_0:
                        currentSelected = 0;
                        break;
                    case R.id.tab_item_1:
                        currentSelected = 1;
                        break;
                    case R.id.tab_item_2:
                        currentSelected = 2;
                        break;
                    case R.id.tab_item_3:
                        currentSelected = 3;
                        break;
                }
                if (currentSelected >= 0) {
                    if (listener != null) {
                        listener.onTabBarSelected(currentSelected);
                    }
                }
            }
        });
    }

    public int getCurrentSelected() {
        return currentSelected;
    }

    public void setCurrentSelected(int index){
        switch (index){
            case 0:
                ((RadioButton)findViewById(R.id.tab_item_0)).setChecked(true);
                break;
            case 1:
                ((RadioButton)findViewById(R.id.tab_item_1)).setChecked(true);
                break;
            case 2:
                ((RadioButton)findViewById(R.id.tab_item_2)).setChecked(true);
                break;
            case 3:
                ((RadioButton)findViewById(R.id.tab_item_3)).setChecked(true);
                break;
        }
    }
}
