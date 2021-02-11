package ir.taxi1880.manager.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.activity.SplashActivity;
import ir.taxi1880.manager.app.Constant;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.KeyBoardHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public static final String TAG = ir.taxi1880.manager.fragment.LoginFragment.class.getSimpleName();
    Unbinder unbinder;
    String userName;
    String password;

    @BindView(R.id.edtUserName)
    EditText edtUserName;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.vfEnter)
    ViewFlipper vfEnter;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @OnClick(R.id.btnLogin)
    void onLogin() {
        userName = edtUserName.getText().toString();
        password = edtPassword.getText().toString();

        if (userName.isEmpty()) {
            MyApplication.Toast("لطفا نام کاربری خود را وارد نمایید", Toast.LENGTH_SHORT);
            return;
        }
        if (password.isEmpty()) {
            MyApplication.Toast("لطفا رمز عبور خود را وارد نمایید", Toast.LENGTH_SHORT);
            return;
        }

        logIn(userName, password);
        KeyBoardHelper.hideKeyboard();
    }

    @OnClick(R.id.enterWithAnotherWay)
    void onEnterWithAnotherWay() {
        FragmentHelper.toFragment(MyApplication.currentActivity, new VerificationFragment()).setAddToBackStack(false).replace();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        edtUserName.requestFocus();
        KeyBoardHelper.showKeyboard(MyApplication.context);

        return view;
    }

    private void logIn(String username, String password) {
        if (vfEnter != null) {
            vfEnter.setDisplayedChild(1);
        }
        RequestHelper.builder(EndPoints.LOGIN)
                .addParam("username", username)
                .addParam("password", password)
                .addParam("scope", Constant.SCOPE)
                .doNotSendHeader(true)
                .listener(onLogIn)
                .post();

    }

    RequestHelper.Callback onLogIn = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");

                    if (vfEnter != null) {
                        vfEnter.setDisplayedChild(0);
                    }

                    if (success) {
                        JSONObject data = object.getJSONObject("data");
                        MyApplication.prefManager.setUserName(userName);
                        MyApplication.prefManager.setPassword(password);
                        MyApplication.prefManager.setIdToken(data.getString("id_token"));
                        MyApplication.prefManager.setAuthorization(data.getString("access_token"));
                        MyApplication.prefManager.setRefreshToken(data.getString("refresh_token"));
                        new SplashActivity().getAppInfo(b -> {

                        });
                    } else {
                        new GeneralDialog()
                                .message(message)
                                .secondButton("بستن", null)
                                .firstButton("تلاش مجدد", () -> {
                                    if (edtUserName != null) {
                                        edtUserName.requestFocus();
                                        KeyBoardHelper.showKeyboard(MyApplication.context);
                                    }
                                })
                                .cancelable(false)
                                .show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (vfEnter != null) {
                    vfEnter.setDisplayedChild(0);
                }
            });
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
