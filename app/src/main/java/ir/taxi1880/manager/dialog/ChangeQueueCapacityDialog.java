package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.DialogChangeQueueCapacityBinding;
import ir.taxi1880.manager.helper.KeyBoardHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class ChangeQueueCapacityDialog {

    private static final String TAG = ChangeQueueCapacityDialog.class.getSimpleName();
    DialogChangeQueueCapacityBinding binding;

    public interface Listener {
        void num(String num);
    }

    private Listener listener;
    static Dialog dialog;

    public void show(Listener listener, String title, String value) {
        if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
            return;
        dialog = new Dialog(MyApplication.currentActivity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
        binding = DialogChangeQueueCapacityBinding.inflate(LayoutInflater.from(MyApplication.context));
        dialog.setContentView(binding.getRoot());
        TypefaceUtil.overrideFonts(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);
        this.listener = listener;

        binding.numberPicker.setVerticalFadingEdgeEnabled(true);
        binding.numberPicker.setTypeface(MyApplication.IRANSANS_BOLD);

        binding.txtTitle.setText(title);
        binding.numberPicker.setValue(Integer.parseInt(value));
        binding.btnSubmit.setOnClickListener(view -> {
            int num = binding.numberPicker.getValue();
            listener.num(num + "");

            dismiss();
        });

        binding.btnClose.setOnClickListener(view -> dismiss());

        dialog.show();
    }

    private static void dismiss() {
        try {
            Log.i(TAG, "dismiss run");
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
