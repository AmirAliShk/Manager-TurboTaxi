package ir.taxi1880.manager.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.Constant;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.fragment.LoginFragment;
import ir.taxi1880.manager.helper.AppVersionHelper;
import ir.taxi1880.manager.helper.ContinueProcessing;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.ScreenHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;
    Unbinder unbinder;
    SplashActivityCallback splashActivityCallback;

    public interface SplashActivityCallback {
        void onSuccess(boolean b);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View view = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        MyApplication.handler.postDelayed(() -> {
            checkPermission();
            if (!MyApplication.prefManager.getLoggedIn()) {
                FragmentHelper.toFragment(MyApplication.currentActivity, new LoginFragment())
                        .setAddToBackStack(false)
                        .replace();
            } else {
                startActivity(new Intent(MyApplication.currentActivity, MainActivity.class));
                MyApplication.currentActivity.finish();
            }
        }, 2000);

    }

    String[] permissionsRequired = new String[]{Manifest.permission.RECORD_AUDIO};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean recordAudioPermission = (ContextCompat.checkSelfPermission(MyApplication.currentActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED);
            if (recordAudioPermission) {

                new GeneralDialog()
                        .title("دسترسی")
                        .message("برای ورود به برنامه ضروری است تا دسترسی های لازم را برای عملکرد بهتر به برنامه داده شود لطفا جهت بهبود عملکرد دسترسی های لازم را اعمال نمایید")
                        .cancelable(false)
                        .firstButton("باشه", new Runnable() {
                            @Override
                            public void run() {
                                ActivityCompat.requestPermissions(MyApplication.currentActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                            }
                        })
                        .show();
            } else {
                getAppInfo(splashActivityCallback);
            }
        } else {
            getAppInfo(splashActivityCallback);
        }
    }

    public void continueProcessing() {
        ContinueProcessing.runMainActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.currentActivity = this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
    }

    public void getAppInfo(SplashActivityCallback splashActivityCallback) {
        this.splashActivityCallback = splashActivityCallback;
        try {
            if (MyApplication.prefManager.getRefreshToken().equals("")) {
                FragmentHelper
                        .toFragment(MyApplication.currentActivity, new LoginFragment())
                        .setAddToBackStack(false)
                        .add();
            } else {
                JSONObject deviceInfo = new JSONObject();
                @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(MyApplication.currentActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
                deviceInfo.put("MODEL", Build.MODEL);
                deviceInfo.put("HARDWARE", Build.HARDWARE);
                deviceInfo.put("BRAND", Build.BRAND);
                deviceInfo.put("DISPLAY", Build.DISPLAY);
                deviceInfo.put("BOARD", Build.BOARD);
                deviceInfo.put("SDK_INT", Build.VERSION.SDK_INT);
                deviceInfo.put("BOOTLOADER", Build.BOOTLOADER);
                deviceInfo.put("DEVICE", Build.DEVICE);
                deviceInfo.put("DISPLAY_HEIGHT", ScreenHelper.getRealDeviceSizeInPixels(MyApplication.currentActivity).getHeight());
                deviceInfo.put("DISPLAY_WIDTH", ScreenHelper.getRealDeviceSizeInPixels(MyApplication.currentActivity).getWidth());
                deviceInfo.put("DISPLAY_SIZE", ScreenHelper.getScreenSize(MyApplication.currentActivity));
                deviceInfo.put("ANDROID_ID", android_id);

                RequestHelper.builder(EndPoints.GET_APP_INFO)
                        .addParam("versionCode", new AppVersionHelper(context).getVerionCode())
                        .addParam("deviceInfo", deviceInfo)
                        .listener(onAppInfo)
                        .post();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    RequestHelper.Callback onAppInfo = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    Log.i(TAG, "onResponse: " + args[0].toString());
                    JSONObject object = new JSONObject(args[0].toString());
                    int block = object.getInt("isBlock");
                    int updateAvailable = object.getInt("updateAvailable");
                    int forceUpdate = object.getInt("forceUpdate");
                    String updateUrl = object.getString("updateUrl");
                    int changePass = object.getInt("changePassword");

                    if (block == 1) {
                        new GeneralDialog()
                                .title("هشدار")
                                .message("اکانت شما توسط سیستم مسدود شده است")
                                .firstButton("خروج از برنامه", () -> MyApplication.currentActivity.finish())
                                .show();
                        return;
                    }

                    if (changePass == 1) {
                        FragmentHelper
                                .toFragment(MyApplication.currentActivity, new LoginFragment())
                                .setAddToBackStack(false)
                                .replace();
                        return;
                    }

                    continueProcessing();

                    if (updateAvailable == 0) {
                        updatePart(forceUpdate, updateUrl);
                        return;
                    }

                    MyApplication.prefManager.setCountRequest(object.getInt("countRequest"));

                    if (splashActivityCallback != null)
                        splashActivityCallback.onSuccess(true);

                    NotificationManager notificationManager = (NotificationManager) MyApplication.currentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(Constant.USER_STATUS_NOTIFICATION_ID);

                } catch (JSONException e) {
                    if (splashActivityCallback != null)
                        splashActivityCallback.onSuccess(false);
                    e.printStackTrace();
                }

            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (splashActivityCallback != null)
                    splashActivityCallback.onSuccess(false);
            });
        }
    };

    private void updatePart(int isForce, final String url) {
        GeneralDialog generalDialog = new GeneralDialog();
        if (isForce == 0) {
            generalDialog.title("به روز رسانی");
            generalDialog.cancelable(false);
            generalDialog.message("برای برنامه نسخه جدیدی موجود است لطفا برنامه را به روز رسانی کنید");
            generalDialog.firstButton("به روز رسانی", () -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MyApplication.currentActivity.startActivity(i);
                MyApplication.currentActivity.finish();
            });
            generalDialog.secondButton("بستن برنامه", () -> MyApplication.currentActivity.finish());
            generalDialog.show();
        } else {
            generalDialog.title("به روز رسانی");
            generalDialog.cancelable(false);
            generalDialog.message("برای برنامه نسخه جدیدی موجود است در صورت تمایل میتوانید برنامه را به روز رسانی کنید");
            generalDialog.firstButton("به روز رسانی", () -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MyApplication.currentActivity.startActivity(i);
                MyApplication.currentActivity.finish();
            });
//            generalDialog.secondButton("فعلا نه", () -> );
            generalDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0 || getFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                } else {
                    this.doubleBackToExitPressedOnce = true;
                    MyApplication.Toast(getString(R.string.txt_please_for_exit_reenter_back), Toast.LENGTH_SHORT);
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
