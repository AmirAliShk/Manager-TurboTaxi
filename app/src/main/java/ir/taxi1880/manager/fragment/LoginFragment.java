package ir.taxi1880.manager.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import ir.taxi1880.manager.activity.SplashActivity;
import ir.taxi1880.manager.app.Constant;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentLoginBinding;
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
    FragmentLoginBinding binding;
    String userName;
    String password;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());
        binding.edtUserName.requestFocus();
        KeyBoardHelper.showKeyboard(MyApplication.context);

        binding.btnLogin.setOnClickListener(view -> {
            userName = binding.edtUserName.getText().toString();
            password = binding.edtPassword.getText().toString();

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
        });

        binding.enterWithAnotherWay.setOnClickListener(view -> {
            FragmentHelper.toFragment(MyApplication.currentActivity, new VerificationFragment()).setAddToBackStack(false).replace();
        });

        return binding.getRoot();
    }

    private void logIn(String username, String password) {
        if (binding.vfEnter != null) {
            binding.vfEnter.setDisplayedChild(1);
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

                    if (binding.vfEnter != null) {
                        binding.vfEnter.setDisplayedChild(0);
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
                                    if (binding.edtUserName != null) {
                                        binding.edtUserName.requestFocus();
                                        KeyBoardHelper.showKeyboard(MyApplication.context);
                                    }
                                })
                                .cancelable(false)
                                .type(3)
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
                if (binding.vfEnter != null) {
                    binding.vfEnter.setDisplayedChild(0);
                }
            });
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        KeyBoardHelper.hideKeyboard();
        super.onPause();
    }
}
