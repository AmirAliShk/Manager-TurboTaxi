package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONObject;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.DialogSalaryBinding;
import ir.taxi1880.manager.helper.KeyBoardHelper;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.SalaryModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class SalaryDialog {

    Dialog dialog;
    DialogSalaryBinding binding;
    SalaryModel model;

    public interface Listener {
        void refresh(Boolean refresh);
    }

    private Listener listener;

    public void show(SalaryModel model, Listener listener) {
        dialog = new Dialog(MyApplication.currentActivity);
        binding = DialogSalaryBinding.inflate(LayoutInflater.from(dialog.getContext()));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
        dialog.setContentView(binding.getRoot());
        TypefaceUtil.overrideFonts(dialog.getWindow().getDecorView());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);

        this.listener = listener;
        this.model = model;

        if (model != null) {
            binding.edtFromTime.setText(StringHelper.toPersianDigits(model.getFromHour() + ""));
            binding.edtToTime.setText(StringHelper.toPersianDigits(model.getToHour() + ""));
            binding.edtCheckMistakePercent.setText(StringHelper.toPersianDigits(model.getErrorPer() + ""));
            binding.edtTripRegisterPercent.setText(StringHelper.toPersianDigits(model.getServicePer() + ""));
            binding.edtDeterminationPercent.setText(StringHelper.toPersianDigits(model.getStationPer() + ""));
            binding.edtDriverCallsPercent.setText(StringHelper.toPersianDigits(model.getAnswerDriverPer() + ""));
            binding.edtCheckComplaintPercent.setText(StringHelper.toPersianDigits(model.getComplaintPer() + ""));
        }

        binding.btnSubmit.setOnClickListener(view -> {
            if (model == null) {
                addSalary();
            } else {
                editSalary();
            }
        });

        binding.imgCancelDialog.setOnClickListener(view -> dismiss());

        dialog.show();
    }

    public void editSalary() {
        if (binding.vfSubmit != null) {
            binding.vfSubmit.setDisplayedChild(1);
        }
        RequestHelper.builder(EndPoints.SALARY)
                .addParam("id", model.getId())
                .addParam("fromHour", StringHelper.toEnglishDigits(binding.edtFromTime.getText() + ""))
                .addParam("toHour", StringHelper.toEnglishDigits(binding.edtToTime.getText() + ""))
                .addParam("service", StringHelper.toEnglishDigits(binding.edtCheckMistakePercent.getText() + ""))
                .addParam("error", StringHelper.toEnglishDigits(binding.edtTripRegisterPercent.getText() + ""))
                .addParam("station", StringHelper.toEnglishDigits(binding.edtDeterminationPercent.getText() + ""))
                .addParam("answerDriver", StringHelper.toEnglishDigits(binding.edtDriverCallsPercent.getText() + ""))
                .addParam("complaint", StringHelper.toEnglishDigits(binding.edtCheckComplaintPercent.getText() + ""))
                .listener(CallBack)
                .put();
    }

    public void addSalary() {
        if (binding.vfSubmit != null) {
            binding.vfSubmit.setDisplayedChild(1);
        }
        RequestHelper.builder(EndPoints.SALARY)
                .addParam("fromHour", StringHelper.toEnglishDigits(binding.edtFromTime.getText().toString()))
                .addParam("toHour", StringHelper.toEnglishDigits(binding.edtToTime.getText().toString()))
                .addParam("service", StringHelper.toEnglishDigits(binding.edtTripRegisterPercent.getText().toString()))
                .addParam("error", StringHelper.toEnglishDigits(binding.edtCheckMistakePercent.getText().toString()))
                .addParam("station", StringHelper.toEnglishDigits(binding.edtDeterminationPercent.getText().toString()))
                .addParam("answerDriver", StringHelper.toEnglishDigits(binding.edtDriverCallsPercent.getText().toString()))
                .addParam("complaint", StringHelper.toEnglishDigits(binding.edtCheckComplaintPercent.getText().toString()))
                .listener(CallBack)
                .post();
    }

    RequestHelper.Callback CallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    if (binding.vfSubmit != null) {
                        binding.vfSubmit.setDisplayedChild(0);
                    }
                    //{"status":true,"message":"با موفقیت اضافه شد"}
                    JSONObject object = new JSONObject(args[0].toString());

                    String message = object.getString("message");
                    boolean status = object.getBoolean("status");
                    if (status) {
                        dismiss();
                        listener.refresh(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            super.onFailure(reCall, e);
        }
    };

    private void dismiss() {
        try {
            Log.i("TAG", "dismiss run");
            if (dialog != null) {
                if (dialog.isShowing())
                    dialog.dismiss();
                KeyBoardHelper.hideKeyboard();
            }
        } catch (Exception e) {
            Log.e("TAG", "dismiss: " + e.getMessage());
        }
        dialog = null;
    }
}
