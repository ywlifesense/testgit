package cn.js.nanhaistaffhome.activitys;

import android.os.Bundle;
import android.view.View;

import cn.js.nanhaistaffhome.R;

/**
 * Created by JS on 2016/3/21.
 */
public class LDBHActivity extends BaseActivity{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ldbh);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
