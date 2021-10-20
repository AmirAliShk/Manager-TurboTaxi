package ir.taxi1880.manager.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.acra.ACRA;
import org.json.JSONException;
import org.json.JSONObject;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.Constant;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ActivitySplashBinding;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.fragment.LoginFragment;
import ir.taxi1880.manager.helper.AppVersionHelper;
import ir.taxi1880.manager.helper.ContinueProcessing;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    ActivitySplashBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    SplashActivityCallback splashActivityCallback;

    public interface SplashActivityCallback {
        void onSuccess(boolean b);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(getResources().getColor(R.color.alsoBlack));
            window.setStatusBarColor(getResources().getColor(R.color.alsoBlack));
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        TypefaceUtil.overrideFonts(binding.getRoot());

        binding.txtVersionName.setText(StringHelper.toPersianDigits(new AppVersionHelper(context).getVerionName() + ""));

        ACRA.getErrorReporter().putCustomData("projectId", Constant.PUSH_PROJECT_ID);
        ACRA.getErrorReporter().putCustomData("LineCode", MyApplication.prefManager.getUserCode() + "");

        MyApplication.handler.postDelayed(this::checkPermission, 1000);

    }

    String[] permissionsRequired = new String[]{Manifest.permission.RECORD_AUDIO};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean recordAudioPermission = (ContextCompat.checkSelfPermission(MyApplication.currentActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED);
            if (recordAudioPermission) {

                new GeneralDialog()
                        .message("" +
                                "برای ورود به برنامه اجازه دسترسی به مجوزهای لازم را بدهید.")
                        .cancelable(false)
                        .firstButton("باشه", () -> ActivityCompat.requestPermissions(MyApplication.currentActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT))
                        .type(4)
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
        if (MyApplication.prefManager.getRefreshToken().equals("")) {
            FragmentHelper
                    .toFragment(MyApplication.currentActivity, new LoginFragment())
                    .setAddToBackStack(false)
                    .add();
        } else {
            RequestHelper.builder(EndPoints.APP_INFO)
                    .addPath(new AppVersionHelper(context).getVersionCode() + "")
                    .listener(onAppInfo)
                    .get();
        }
    }

    RequestHelper.Callback onAppInfo = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean success = object.getBoolean("success");
                    if (success) {
                        JSONObject data = object.getJSONObject("data");
                        String result = data.getString("result");

                        JSONObject getJResult = new JSONObject(result);

                        boolean block = getJResult.getBoolean("isBlock");
                        boolean updateAvailable = getJResult.getBoolean("updateAvailable");
                        boolean forceUpdate = getJResult.getBoolean("forceUpdate");
                        String updateUrl = getJResult.getString("updateUrl");
                        MyApplication.prefManager.setCarType(getJResult.getJSONArray("carClass") + "");

                        if (block) {
                            new GeneralDialog()
                                    .message("اکانت شما توسط سیستم مسدود شده است")
                                    .firstButton("خروج از برنامه", () -> MyApplication.currentActivity.finish())
                                    .type(3)
                                    .show();
                            return;
                        }

                        continueProcessing();

                        if (updateAvailable) {
                            updatePart(forceUpdate, updateUrl);
                            return;

                        }
                        if (splashActivityCallback != null)
                            splashActivityCallback.onSuccess(true);
                    }

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

    private void updatePart(boolean isForce, final String url) {
        GeneralDialog generalDialog = new GeneralDialog();
        if (isForce) {
            generalDialog.cancelable(false);
            generalDialog.message("برای برنامه نسخه جدیدی موجود است لطفا برنامه را به روز رسانی کنید");
            generalDialog.firstButton("به روز رسانی", () -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MyApplication.currentActivity.startActivity(i);
                MyApplication.currentActivity.finish();
            });
            generalDialog.secondButton("بستن برنامه", () -> MyApplication.currentActivity.finish());
            generalDialog.type(3);
            generalDialog.show();
        } else {
            generalDialog.cancelable(false);
            generalDialog.message("برای برنامه نسخه جدیدی موجود است در صورت تمایل میتوانید برنامه را به روز رسانی کنید");
            generalDialog.firstButton("به روز رسانی", () -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                MyApplication.currentActivity.startActivity(i);
                MyApplication.currentActivity.finish();
            });
            generalDialog.secondButton("فعلا نه", () -> continueProcessing());
            generalDialog.type(4);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
