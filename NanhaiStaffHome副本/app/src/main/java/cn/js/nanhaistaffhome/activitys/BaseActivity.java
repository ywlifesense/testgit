package cn.js.nanhaistaffhome.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by JS on 8/3/15.
 */
public class BaseActivity extends Activity {

    public final static int REQUEST_CODE_PICK_IMAGE = 0x00001;
    public final static int REQUEST_CODE_CAPTURE_CAMEIA = 0x00002;
    public final static int REQUEST_CODE_SEARCH_COMPANY = 0x00003;

    protected ProgressDialog pdialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String msg) {
        pdialog = new ProgressDialog(this);
        pdialog.setMessage(msg);
        pdialog.setCancelable(true);
        pdialog.show();
    }

    public void hideProgressDialog() {
        if (pdialog != null && pdialog.isShowing()) {
            pdialog.dismiss();
        }
    }

    public void showUnLoginAlertDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("温馨提示")
                .setMessage("该操作需要登录才可进行，请先登录！")
                .setPositiveButton("确定",null)
                .create();
        dialog.show();
    }
}
