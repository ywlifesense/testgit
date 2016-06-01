package cn.js.nanhaistaffhome.http;

import android.os.AsyncTask;

import cn.js.nanhaistaffhome.interfaces.OnHttpRequestListener;

/**
 * Created by JS on 8/13/15.
 */
abstract class HttpAsyncTask extends AsyncTask<String, Integer, String> {

    private OnHttpRequestListener listener;

    @Override
    protected void onPreExecute() {
        if (listener != null) {
            listener.onRequestStart();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onRequestEnd(result);
        }
    }

    @Override
    protected void onCancelled() {
        if (listener != null) {
            listener.onRequestCancal();
        }
    }

    protected HttpAsyncTask setOnHttpRequestListener(OnHttpRequestListener listener) {
        this.listener = listener;
        return this;
    }
}
