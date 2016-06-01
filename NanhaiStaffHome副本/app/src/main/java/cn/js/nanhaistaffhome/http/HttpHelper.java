package cn.js.nanhaistaffhome.http;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cn.js.nanhaistaffhome.StaffApplication;
import cn.js.nanhaistaffhome.models.User;

public class HttpHelper {

    private String sessionId;
    private Map<String, String> tokenMap = new ConcurrentHashMap<>();

    private static HttpHelper instance;

    public static HttpHelper getInstance() {
        if (instance == null) {
            instance = new HttpHelper();
        }
        return instance;
    }

    private HttpHelper() {
    }

    private String getSessionId() {
        if (TextUtils.isEmpty(sessionId)) {
            sessionId = UUID.randomUUID().toString();
        }
        return sessionId;
    }

    /**
     * 处理这一类消息JSESSIONID=190085C3ADA1649539AE1ED3FE3E48CF; Path=/ApproveService
     *
     * @param param
     * @return
     */
    private Map<String, String> getParamForMap(String param) {
        Map<String, String> m = new HashMap<>();
        if (param != null && param.indexOf(";") > 0) {
            String[] params = param.split(";");
            for (int i = 0; i < params.length; i++) {
                String[] ps = params[i].split("=");
                if (ps.length>1) {
                    m.put(ps[0], ps[1]);
                }
            }
        }
        return m;
    }

    public String send(String url, Map<String, String> param) {
        int i = 0;
        String param_s = "";
        Iterator<String> it = param.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = param.get(key);
            if (i == 0) {
                param_s += key + "=" + value;
            } else {
                param_s += "&" + key + "=" + value;
            }
            i++;
        }
        return this.send(url, param_s);
    }

    public String send(String url, String param) {
        String result = "";
        try {
            String tokenParam = "token=" + this.tokenMap.get(getSessionId());
            result = this.sendPost(url, tokenParam + "&" + param);
            JSONObject json = new JSONObject(result);
            if ((Integer) json.get("code") == 1) {
                auth();
                result = this.sendPost(url, "token=" + this.tokenMap.get(getSessionId()) + "&" + param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean auth() throws Exception {
        String result;
        User user = StaffApplication.getInstance().getUser();
        result = sendPost(HttpConstant.URL_LOGIN, "username=" + user.getUserName() + "&password=" + user.getPassword());
        JSONObject json = new JSONObject(result);
        if ((Integer) json.get("code") == 0) {
            return true;
        } else {
            return false;
        }
    }

    private String sendPost(String url, String param) throws Exception {
        OutputStream out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        String old_sessionid;
        try {
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();

            old_sessionid = getSessionId();
            if (old_sessionid != null) {
                conn.setRequestProperty("Cookie", "JSESSIONID=" + old_sessionid);
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(HttpConstant.TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(HttpConstant.TIMEOUT_IN_MILLIONS);
            out = conn.getOutputStream();
            out.write(param.getBytes(HttpConstant.ENCODING));
            out.flush();

            String newSessionId = getParamForMap(conn.getHeaderField("Set-Cookie")).get("JSESSIONID");
            if (newSessionId != null) {
                if (!newSessionId.equals(old_sessionid)) {
                    sessionId = newSessionId;
                }
            }

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), HttpConstant.ENCODING));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            throw new Exception("请求失败！" + conn.getHeaderField(0));
        } finally {

            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
        JSONObject json = new JSONObject(result);
        if (json.has("token")){
            String token = json.optString("token");
            if (!TextUtils.isEmpty(token)){
                this.tokenMap.clear();
                this.tokenMap.put(getSessionId(), token);
            }
        }
        return result;
    }

    public String sendGet(String urlStr) throws Exception {
        URL url;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        String result = "";
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(HttpConstant.TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(HttpConstant.TIMEOUT_IN_MILLIONS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();

                baos = new ByteArrayOutputStream();
                int len;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                result = baos.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            conn.disconnect();
        }
        return result;
    }
}
