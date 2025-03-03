package ir.taxi1880.manager.okHttp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.fragment.LoginFragment;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.StringHelper;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 * Created by Amirreza Erfanian on 2018/01/12.
 * currentVersion 1.1.0
 ***************** version changes *******************
 * v : 1.0.0 create project
 * v : 1.0.1 fix bug run reCall with returnValue
 * v : 1.1.0 added Interceptor for append header to all api
 ****************** Readme *******************
 * add to your BuildGradle
 * implementation 'com.squareup.okhttp3:okhttp:3.10.0'
 */
public class RequestHelper implements okhttp3.Callback {

    public static final String TAG = ir.taxi1880.manager.okHttp.RequestHelper.class.getSimpleName();
    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";
    public static final String HEAD = "HEAD";
    public static final String PATCH = "PATCH";

    private static ir.taxi1880.manager.okHttp.RequestHelper instance;
    private String url = null;
    private String path = null;
    private Callback listener = null;
    private JSONObject params = null;
    private ArrayList<String> paths = null;
    private boolean errorHandling = true;
    private boolean hideNetworkError = false;
    private Request req = null;
    private Object[] object;
    private boolean ignore422 = false;
    private boolean doNotSendHeader = false;
    private Headers.Builder headers = new Headers.Builder();

    public static abstract class Callback {
        public void onReloadPress(boolean v) {
        }

        public void onFailure(Runnable reCall, Exception e) {
        }

        public abstract void onResponse(Runnable reCall, Object... args);
    }

    public ir.taxi1880.manager.okHttp.RequestHelper addHeader(String name, String value) {
        this.headers.add(name, value);
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper removeHeader(String name) {
        this.headers.removeAll(name);
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper hideNetworkError(boolean hideNetworkError) {
        this.hideNetworkError = hideNetworkError;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper setErrorHandling(Boolean v) {
        this.errorHandling = v;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper returnInResponse(Object... object) {
        this.object = object;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper addParam(String key, Object value) {
        if (params == null) {
            params = new JSONObject();
        }
        try {
            params.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper addParam(String key, String value) {
        if (params == null) {
            params = new JSONObject();
        }
        try {
            value = StringHelper.toEnglishDigits(value);
            params.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper addPath(String value) {
        if (paths == null)
            paths = new ArrayList<>();
        paths.add(value);
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper ignore422Error(boolean ignore) {
        this.ignore422 = ignore;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper doNotSendHeader(boolean doNotSendHeader) {
        this.doNotSendHeader = doNotSendHeader;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper listener(Callback listener) {
        this.listener = listener;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper connectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public ir.taxi1880.manager.okHttp.RequestHelper writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public static ir.taxi1880.manager.okHttp.RequestHelper builder(String url) {
        instance = new ir.taxi1880.manager.okHttp.RequestHelper();
        instance.url = url;
        return instance;
    }

    private String getUrl() {
        String url = this.url;
        if (path != null) {
            String address = EndPoints.IP;
            url = "http://" + address + this.path;
        }

        this.url = url;
        return url;
    }

    public static ir.taxi1880.manager.okHttp.RequestHelper loadBalancingBuilder(String path) {
        instance = new ir.taxi1880.manager.okHttp.RequestHelper();
        instance.path = path;
        return instance;
    }

    public void get() {
        url = getUrl();
        if (url == null) return;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            Iterator<String> iter = params.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String value = params.getString(key);
                    urlBuilder.addQueryParameter(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }
        if (paths != null) {
            for (String ob : paths) {
                if (ob == null) continue;
                urlBuilder.addPathSegment(ob);
            }
        }
        String url = urlBuilder.build().toString();

        req = new Request.Builder()
                .url(url)
                .build();

        request();

    }

    public void post() {
        url = getUrl();
        if (url == null) return;
        if (instance.params == null) params = new JSONObject();
        RequestBody body = RequestBody.create(JSON, params.toString());

        req = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        request();

    }

    public void put() {
        url = getUrl();
        if (url == null) return;

        RequestBody body = RequestBody.create(JSON, params.toString());
        req = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        request();

    }

    public void delete() {
        url = getUrl();
        if (url == null) return;

        RequestBody body = RequestBody.create(JSON, params.toString());
        req = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        request();
    }

    public OkHttpClient okHttpClient;
    OkHttpClient.Builder builder;

    private void request() {
        try {
            log("request url : " + req.url().toString());
            log("params : " + params);
            log("paths : " + path);
            builder = new OkHttpClient
                    .Builder()
                    .proxy(Proxy.NO_PROXY);

            okHttpClient = builder.connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                    .readTimeout(readTimeout, TimeUnit.SECONDS)
                    .addInterceptor(new AuthenticationInterceptor())
                    .build();

            call = okHttpClient.newCall(req);
            call.enqueue(this);

        } catch (final Exception e) {
            requestFailed(REQUEST_CRASH, e);

        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private int connectionTimeout = 20;
    private int writeTimeout = 20;
    private int readTimeout = 25;
    private Object object1 = null;
    private Object object2 = null;

    public static final int INTERNET_CONNECTION_EXCEPTION = -1;
    public static final int REQUEST_CRASH = -2;

    @Override
    public void onFailure(Call call, final IOException e) {
        this.call = call;
        log("request failed :  The requested URL can't be Reached The service took too long to respond.");
        if (listener != null)
            requestFailed(INTERNET_CONNECTION_EXCEPTION, e);
    }

    @Override
    public void onResponse(Call call, final Response response) {
        this.call = call;
        if (listener != null) {
            final String bodyStr;
            try {
                bodyStr = parseXML(response.body().string());
                log("request result : " + bodyStr);

                if (response.isSuccessful()) {
                    if (object == null)
                        object = new Object[0];
                    requestSuccess(bodyStr);
                } else {
                    try {
                        JSONObject responseObject = new JSONObject(bodyStr);
                        if (responseObject.has("refreshTokenError") && responseObject.getBoolean("refreshTokenError")) {
                            logout();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestFailed(response.code(), new Exception(response.message() + bodyStr));
                }

            } catch (final IOException e) {
                requestFailed(response.code(), e);
                if (listener != null)
                    listener.onFailure(runnable, e);
            }
        }
    }

    /**
     * manage log
     *
     * @param v
     */
    private void log(String v) {
        Log.i(TAG, "====> " + v);
    }

    /**
     * this function extract response from XML result
     *
     * @param str = response from EndPoints
     * @return
     */
    public static String parseXML(String str) {
        if (str == null) {
            return null;
        }

        str = str.replace("\n", "");
        str = str.replace("\r", "");
        Pattern pattern = Pattern.compile("\\<.*?\\>");
        Matcher matcher = pattern.matcher(str);

        if (matcher.matches()) {
            return matcher.replaceAll("").trim();
        }

        return str;
    }

    Call call;
    Runnable runnable = () -> request();

    private void requestSuccess(Object res) {
        if (listener != null) {
            Object[] resTemp = new Object[object.length + 1];
            resTemp[0] = res;
            for (int i = 0; i < object.length; i++) {
                resTemp[i + 1] = object[i];
            }
            listener.onResponse(runnable, resTemp);
        }
    }

    private void reloadPress(boolean v) {
        if (listener != null) {
            listener.onReloadPress(v);
        }
    }

    private void requestFailed(int code, Exception e) {

        if (listener != null)
            listener.onFailure(runnable, e);
        Log.e(TAG, "requestFailed: ", e);
        switch (code) {
            case -1:
//        DBIO.setFail(MyApplication.context, url);
                showError("عدم دسترسی به اینترنت لطفا پس از بررسی ارتباط دستگاه خود به اینترنت و اطمینان از ارتباط، مجدد تلاش نمایید.");
                break;
            case -3:
                showError("آدرس وارد شده نا معتبر میباشد لطفا با پشتیبانی تماس حاصل نمایید");
                break;
            case 400:
                showError("خطای 400 : مشکلی در ارسال داده به وجود آمده است لطفا پس از چند لحظه مجدد تلاش نمایید در صورت عدم برطرف شدن، لطفا با پشتیبانی تماس حاصل نمایید.");
                break;
            case 401:
//        DBIO.setFail(MyApplication.context, url);
                showError("خطای 401 : عدم دسترسی به اینترنت لطفا پس از بررسی ارتباط دستگاه خود به اینترنت و اطمینان از ارتباط، مجدد تلاش نمایید.");
                break;
            case 403:
                showError("خطای 403 : عدم مجوز دسترسی به شبکه لطفا با پشتیبانی تماس حاصل نمایید.");
                break;
            case 404:
//        DBIO.setFail(MyApplication.context, url);
                showError("خطای 404 : برای چنین درخواستی پاسخی وجود ندارد لطفا با پشتیبانی تماس حاصل نمایید.");
                break;
            case 422://error entity
                if (ignore422) {
                    showMessage();
                } else {
                    showError("خطای 422 : متاسفانه اطلاعات ارسالی ناقص است لطفا با پشتیبانی تماس بگیرد");
                }
                break;
            case 500:
                showError("خطای 500 : مشکلی در پردازش داده به وجود آمده است لطفا پس از چند لحظه مجدد تلاش نمایید در صورت عدم برطرف شدن، لطفا با پشتیبانی تماس حاصل نمایید.");
                break;
            default:
                showError("خطای " + code + " : خطایی تعریف نشده در سیستم به وجود آمده لطفا با پشتیبانی تماس حاصل نمایید.");
                break;
        }
    }

    private void showMessage() {
        MyApplication.handler.post(() -> {
            //TODO correct this in next version
//    Unprocessable Entity{"message":"Unprocessable Entity","data":[{"field":"stationCode","message":"کد ایستگاه صحیح نیست"}],"success":false}
//      try {
//        JSONObject dataObj = new JSONObject(error);
//        boolean success = dataObj.getBoolean("success");
//        JSONArray dataArr = dataObj.getJSONArray("data");
//        String message = "";
//        if (!success) {
//          for (int i = 0; i < dataArr.length(); i++) {
//            JSONObject object = dataArr.getJSONObject(i);
//            message = message + object.getString("message") + "\n";
//          }

            new GeneralDialog()
                    .message("اطلاعات صحیح نمیباشد")
                    .cancelable(false)
                    .firstButton("باشه", null)
                    .type(3)
                    .show();
//        }

//      } catch (JSONException e) {
//        e.printStackTrace();
//      }
        });
    }

    private static GeneralDialog errorDialog;

    public void showError(final String message) {
        MyApplication.currentActivity.runOnUiThread(() -> {
            if (!errorHandling) return;
            try {
                MyApplication.handler.post(() -> {
                    if (hideNetworkError)
                        return;
                    if (errorDialog == null) {
                        errorDialog = new GeneralDialog();
                        errorDialog.message(message);
                        errorDialog.cancelable(false);
                        errorDialog.secondButton("بستن", null);
                        errorDialog.firstButton("تلاش مجدد", () -> runnable.run());
                    }
                    errorDialog.dismiss();
                    errorDialog.type(3);
                    errorDialog.show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void logout() {
        MyApplication.handler.post(() -> {
            FragmentHelper
                    .toFragment(MyApplication.currentActivity, new LoginFragment())
                    .setAddToBackStack(false)
                    .replace();
        });
    }
}
