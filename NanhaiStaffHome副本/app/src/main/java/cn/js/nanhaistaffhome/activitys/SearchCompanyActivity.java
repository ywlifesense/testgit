package cn.js.nanhaistaffhome.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.js.nanhaistaffhome.R;
import cn.js.nanhaistaffhome.http.HttpClient;
import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;
import cn.js.nanhaistaffhome.interfaces.OnNaviBarActionListener;
import cn.js.nanhaistaffhome.views.main.NavigationBar;

/**
 * Created by JS on 2015/12/28.
 */
public class SearchCompanyActivity extends BaseActivity {

    private NavigationBar navigationBar;
    private ListView listView;
    private String[] companyList;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);

        navigationBar = (NavigationBar)findViewById(R.id.top_bar);
        navigationBar.setOnNaviBarActionListener(new OnNaviBarActionListener() {
            @Override
            public void onLeftBtnClick() {
                finish();
            }

            @Override
            public void onRightBtnClick() {
                search(navigationBar.getSearchContent());
            }

            @Override
            public void onSearch(String text) {
                if (TextUtils.isEmpty(text)){
                    companyList = new String[0];
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            SearchCompanyActivity.this,R.layout.item_search_company,companyList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFocusChange(boolean b) {
            }

        });
        navigationBar.setRightBtnTitle("搜索");

        listView = (ListView)findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent data = new Intent();
                data.putExtra("result", companyList[i]);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    private void search(String key){
        HttpClient.getCompanyList(key,new OnHttpRequestListener() {
            @Override
            public void onRequestStart() {
                showProgressDialog("正在加载数据，请稍候...");
            }

            @Override
            public void onRequestEnd(String result) {
                hideProgressDialog();
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.optInt("code", 1) == 0) {
                            JSONArray array = obj.getJSONArray("data");
                            if (array != null && array.length() > 0) {
                                int len = array.length();
                                companyList = new String[len];
                                for (int i = 0; i < len; i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    companyList[i] = jsonObject.optString("NAME");
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        SearchCompanyActivity.this,R.layout.item_search_company,companyList);
                                listView.setAdapter(adapter);
                            }else {
                                companyList = new String[0];
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        SearchCompanyActivity.this,R.layout.item_search_company,companyList);
                                listView.setAdapter(adapter);
                            }
                        } else {
                            showToast(obj.optString("msg"));
                        }
                    } catch (Exception e) {
                        showToast("企业列表获取失败，请稍后再试！");
                    }
                } else {
                    showToast("企业列表获取失败，请稍后再试！");
                }
            }

            @Override
            public void onRequestCancal() {
                hideProgressDialog();
            }
        });
    }
}
