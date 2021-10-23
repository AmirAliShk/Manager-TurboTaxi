package ir.taxi1880.manager.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.Calendar;

import ir.taxi1880.manager.activity.SplashActivity;
import ir.taxi1880.manager.app.Constant;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentCheckVerificationBinding;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class CheckVerificationFragment extends Fragment {

    public static final String TAG = ir.taxi1880.manager.fragment.CheckVerificationFragment.class.getSimpleName();
    FragmentCheckVerificationBinding binding;
    String code;
    String phoneNumber;
    static CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckVerificationBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());

        Bundle bundle = getArguments();
        if (bundle != null) {
            phoneNumber = bundle.getString("mobileNumber");
            binding.txtPhoneNumber.setText(phoneNumber);
        }

        binding.txtResendCode.setOnClickListener(view -> {
            verification(phoneNumber);
        });

        binding.llChangeNumber.setOnClickListener(view -> {
            if (countDownTimer != null)
                countDownTimer.cancel();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ir.taxi1880.manager.fragment.VerificationFragment()).setAddToBackStack(false).replace();
        });

        binding.btnLogin.setOnClickListener(view -> {
            code = binding.pin.getText().toString();

            if (code.isEmpty()) {
                MyApplication.Toast("کد را وارد کنید", Toast.LENGTH_SHORT);
                return;
            }

            checkVerification();
        });

        startWaitingTime();

        return binding.getRoot();
    }

    private void verification(String phoneNumber) {
        if (binding.vfTime != null) {
            binding.vfTime.setDisplayedChild(2);
        }

        RequestHelper.builder(EndPoints.VERIFICATION)
                .addParam("phoneNumber", phoneNumber)
                .listener(onVerificationCallBack)
                .post();

    }

    private RequestHelper.Callback onVerificationCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
//                    {"success":true,"message":"با موفقیت ارسال شد","data":{"repetitionTime":120}}
                    if (binding.vfTime != null) {
                        binding.vfTime.setDisplayedChild(0);
                    }
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");
                    if (success) {
                        JSONObject objData = object.getJSONObject("data");
                        int repetitionTime = objData.getInt("repetitionTime");
                        MyApplication.prefManager.setRepetitionTime(repetitionTime);
                        startWaitingTime();
                    } else {
                        MyApplication.Toast(message, Toast.LENGTH_SHORT);
//                        {"success":false,"message":"محدودیت زمانی","data":{}}
                        //TODO show dialog error
                    }
                } catch (Exception e) {
                    if (binding.vfTime != null) {
                        binding.vfTime.setDisplayedChild(1);
                    }
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (binding.vfTime != null) {
                    binding.vfTime.setDisplayedChild(1);
                }
            });
        }
    };

    private void checkVerification() {
        if (binding.vfEnter != null) {
            binding.vfEnter.setDisplayedChild(1);
        }

        RequestHelper.builder(EndPoints.CHECK)
                .addParam("phoneNumber", phoneNumber)
                .addParam("scope", Constant.SCOPE)
                .addParam("code", code)
                .listener(onCheckVerificationCallBack)
                .post();

    }

    private RequestHelper.Callback onCheckVerificationCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
//                  {"success":true,"message":"با موفقیت وارد شدید","data":{"id_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MTIzLCJ1c2VybmFtZSI6IjEyMzQiLCJpYXQiOjE2MDkzMjg2NTYsImV4cCI6MTYwOTMyODk1Nn0.u_twFCxWzu73CMkPtb73Q0WdgzozgWKbgZYSmzlIgHg","access_token":"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL3R1cmJvdGF4aS5pciIsImF1ZCI6IlVzZXJzIiwiZXhwIjoxNjA5MzI4OTU2LCJzY29wZSI6Im9wZXJhdG9yIiwic3ViIjoidHVyYm90YXhpIiwianRpIjoiNkQ0OTc3ODI3NzFGN0ZEMSIsImFsZyI6IkhTMjU2IiwiaWF0IjoxNjA5MzI4NjU2fQ.8Ssz4-AhK10cy8ma1635iIgquj9gtHHB4S1ETyioRN4","refresh_token":"kTDDNxxc4tQVrN1qQhQFBXZE5qFu3mbelgEbExnsnUElmZv0fFUDpOilLVeOegN5nDCX92mlahXHxP7hWjN52AoOZnZbDG7nz7mcqjowrpxiAgjWsHw5DeOW0RBvadgnRXGEYYS9YByTrYwTL3C4VZEY0DzeTzVyfZsRG2D8LX1jeE87yDx7Afe8D0em4htKfM1KvMWlptdMQbrZrE6yZRuvofubZAFgHgazoi8EDfiWtanu5jNiW86KuPJgbC0r"}}
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");

                    if (success) {
                        JSONObject data = object.getJSONObject("data");
                        MyApplication.prefManager.setIdToken(data.getString("id_token"));
                        MyApplication.prefManager.setAuthorization(data.getString("access_token"));
                        MyApplication.prefManager.setRefreshToken(data.getString("refresh_token"));
                        new SplashActivity().getAppInfo(b -> {
                            if (binding.vfEnter != null) {
                                binding.vfEnter.setDisplayedChild(0);
                            }
                        });
                    } else {
                        if (binding.vfEnter != null) {
                            binding.vfEnter.setDisplayedChild(0);
                        }
                        MyApplication.Toast(message, Toast.LENGTH_SHORT);
//                        {"success":false,"message":".اطلاعات صحیح نمی باشد","data":{}}
                        //TODO show dialog error
                    }

                } catch (Exception e) {
                    if (binding.vfEnter != null) {
                        binding.vfEnter.setDisplayedChild(0);
                    }
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

    private void startWaitingTime() {
        if (MyApplication.prefManager.getActivationRemainingTime() < Calendar.getInstance().getTimeInMillis())
            MyApplication.prefManager.setActivationRemainingTime(Calendar.getInstance().getTimeInMillis() + (MyApplication.prefManager.getRepetitionTime() * 1000));
        countDownTimer();
    }

    private void countDownTimer() {
        long remainingTime = MyApplication.prefManager.getActivationRemainingTime() - Calendar.getInstance().getTimeInMillis();

        countDownTimer = new CountDownTimer(remainingTime, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                if (binding.txtRetry != null) {
                    if (binding.vfTime != null)
                        binding.vfTime.setDisplayedChild(0);
                    binding.txtRetry.setText("تلاش مجدد برای ارسال کد پس از : " + millisUntilFinished / 1000 + " ثانیه");
                }
            }

            public void onFinish() {
                if (binding.txtRetry != null) {
                    if (binding.vfTime != null)
                        binding.vfTime.setDisplayedChild(1);
                }
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}