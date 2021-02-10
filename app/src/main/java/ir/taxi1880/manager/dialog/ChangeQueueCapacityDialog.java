package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;

import ir.taxi1880.manager.helper.KeyBoardHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class ChangeQueueCapacityDialog {
    EditText txtNumber;

    private static final String TAG = ChangeQueueCapacityDialog.class.getSimpleName();

    public interface Listener {
        void num(String num);
    }

    private Listener listener;
    static Dialog dialog;

    public void show(Listener listener, String title) {
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
        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);
        this.listener = listener;

        ImageView imgClose = dialog.findViewById(R.id.btnClose);
        txtNumber = dialog.findViewById(R.id.txtNumber);
        ImageView btnSubmit = dialog.findViewById(R.id.btnSubmit);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);

        txtTitle.setText(title);

        txtNumber.requestFocus();

        txtNumber.setOnClickListener(v -> KeyBoardHelper.showKeyboard(MyApplication.context));

        btnSubmit.setOnClickListener(view -> {
            String num = txtNumber.getText().toString();
            listener.num(num);

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
