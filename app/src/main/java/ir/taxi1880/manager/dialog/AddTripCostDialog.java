package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class AddTripCostDialog {

    String way, origin, dest;
    Dialog dialog;
    Unbinder unbinder;

    @BindView(R.id.edtNameWayRTCDialog)
    EditText edtWayName;

    @BindView(R.id.originRTCDialog)
    EditText edtOrigin;

    @BindView(R.id.destRTCDialog)
    EditText edtDest;

    @OnClick(R.id.btnSubmit)
    void onSubmit() {

        String wayName = edtWayName.getText().toString().trim();
        String origin = edtOrigin.getText().toString().trim();
        String dest = edtDest.getText().toString().trim();

        if (wayName.isEmpty()) {
            MyApplication.Toast("مسیر را کامل کنید", 2);
            return;
        }

        if (origin.isEmpty()) {
            MyApplication.Toast("مبدا را کامل کنید", 2);
            return;

        }

        if (dest.isEmpty()) {
            MyApplication.Toast("مقصد را کامل کنید", 2);
            return;

        }

        MyApplication.Toast("با موفقیت ثبت گردید",2);
        dialog.dismiss();
    }

    public void show() {
        if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
            return;

        dialog = new Dialog(MyApplication.currentActivity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
        dialog.setContentView(R.layout.dialog_trip_cost_dialog);
        unbinder = ButterKnife.bind(this, dialog.getWindow().getDecorView());
        TypefaceUtil.overrideFonts(dialog.getWindow().getDecorView());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);
        dialog.show();
    }

}
