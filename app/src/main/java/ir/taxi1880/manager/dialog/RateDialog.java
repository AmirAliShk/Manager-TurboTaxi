package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class RateDialog {

    Unbinder unbinder;
    static Dialog dialog;
    int increaseRateId;
    int cityCode;
    int fromHour;
    int toHour;
    int stopPricePercent;
    int meterPricePercent;
    int entryPricePercent;
    int charterPricePercent;
    int minPricePercent;
    int carClass;

    @OnClick(R.id.btnSubmit)
    void onSubmit() {
        editRate();
    }

    @BindView(R.id.llCarClass)
    LinearLayout llCarClass;

    public void show(int increaseRateId, int cityCode, int fromHour, int toHour, int stopPricePercent, int meterPricePercent, int entryPricePercent, int charterPricePercent, int minPricePercent) {

        if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
            return;
        dialog = new Dialog(MyApplication.currentActivity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
        dialog.setContentView(R.layout.dialog_rate);
        unbinder = ButterKnife.bind(this, dialog);
        TypefaceUtil.overrideFonts(dialog.getWindow().getDecorView());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);

        this.increaseRateId = increaseRateId;
        this.cityCode = cityCode;
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.stopPricePercent = stopPricePercent;
        this.meterPricePercent = meterPricePercent;
        this.entryPricePercent = entryPricePercent;
        this.charterPricePercent = charterPricePercent;
        this.minPricePercent = minPricePercent;


        dialog.show();
    }

    private void editRate() {
        RequestHelper.builder(EndPoints.EDIT_RATE)
//            {put} /api/manager/v2/pricing/editIncreaseRate
//Params:
// * @apiParam {int} increaseRateId
// * @apiParam {int} cityCode
// * @apiParam {int} fromHour
// * @apiParam {int} toHour
// * @apiParam {int} stopPricePercent
// * @apiParam {int} metrPricePercent
// * @apiParam {int} entryPricePercent
// * @apiParam {int} charterPricePercent
// * @apiParam {int} minPricePercent
// * @apiParam {varchar(50)} carClass  seprate with ','

                .addParam("increaseRateId", increaseRateId)
                .addParam("cityCode", cityCode)
                .addParam("fromHour", fromHour)
                .addParam("toHour", toHour)
                .addParam("stopPricePercent", stopPricePercent)
                .addParam("metrPricePercent", meterPricePercent)
                .addParam("entryPricePercent", entryPricePercent)
                .addParam("charterPricePercent", charterPricePercent)
                .addParam("minPricePercent", minPricePercent)
                .addParam("carClass", carClass)
                .listener(editaRateCallBack)
                .put();
    }

    RequestHelper.Callback editaRateCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());

                    String message = object.getString("message");
                    boolean status = object.getBoolean("status");

                    new GeneralDialog()
                            .message(message)
                            .cancelable(false)
                            .firstButton("باشه", null)
                            .type(2)
                            .show();

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
}
