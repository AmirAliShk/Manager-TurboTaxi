package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.KeyBoardHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class ChangeQueueCapacityDialog {

    private static final String TAG = ChangeQueueCapacityDialog.class.getSimpleName();

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
        dialog.setContentView(R.layout.dialog_change_queue_capacity);
        TypefaceUtil.overrideFonts(dialog.getWindow().getDecorView());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);
        this.listener = listener;

        ImageView imgClose = dialog.findViewById(R.id.btnClose);
        ImageView btnSubmit = dialog.findViewById(R.id.btnSubmit);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        NumberPicker numberPicker = dialog.findViewById(R.id.numberPicker);

        numberPicker.setVerticalFadingEdgeEnabled(true);
        numberPicker.setTypeface(MyApplication.IRANSANS_BOLD);

        txtTitle.setText(title);
        numberPicker.setValue(Integer.parseInt(value));
        btnSubmit.setOnClickListener(view -> {
            int num = numberPicker.getValue();
            listener.num(num + "");

            dismiss();
        });

        imgClose.setOnClickListener(view -> dismiss());

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
