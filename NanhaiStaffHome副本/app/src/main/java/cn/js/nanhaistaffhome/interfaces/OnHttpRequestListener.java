package cn.js.nanhaistaffhome.interfaces;

/**
 * Created by JS on 8/12/15.
 */
public interface OnHttpRequestListener {

    void onRequestStart();
    void onRequestEnd(String result);
    void onRequestCancal();
}
