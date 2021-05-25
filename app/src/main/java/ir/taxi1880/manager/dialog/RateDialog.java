package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.SpinnerAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;
import ir.taxi1880.manager.model.RateModel;
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
    CityModel cityModel;
    RateModel rateModel;
    String carType = "";
    private String cityName = "";
    private int cityCode2;
    ArrayList<CityModel> cityModels;

    public interface RateDialogListener {
        void rateModel(RateModel model);
    }

    RateDialogListener listener;

    RateModel model;

    @BindView(R.id.edtFromTime)
    EditText edtFromTime;

    @BindView(R.id.edtToTime)
    EditText edtToTime;

    @BindView(R.id.edtMeter)
    EditText edtMeter;

    @BindView(R.id.edtStop)
    EditText edtStop;

    @BindView(R.id.edtDisposal)
    EditText edtDisposal;

    @BindView(R.id.edtMinimum)
    EditText edtMinimum;

    @BindView(R.id.spCity)
    Spinner spCity;

    @BindView(R.id.llCarClass)
    LinearLayout llCarClass;

    @BindView(R.id.chbEconomical)
    CheckBox chbEconomical;

    @BindView(R.id.chbCeremonies)
    CheckBox chbCeremonies;

    @BindView(R.id.chbTaxi)
    CheckBox chbTaxi;

    @BindView(R.id.chbFormality)
    CheckBox chbFormality;

    @OnClick(R.id.btnSubmit)
    void onSubmit() {
        //TODO check condition here

        listener.rateModel(rateModel);

        if (model != null) {
            editRate();
        } else if (model == null) {
            addRates();
        }
    }

    public void show(RateModel model, RateDialogListener listener) {

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

        this.listener = listener;
        this.model = model;
        if (model != null) {
            this.increaseRateId = model.getId();
            this.cityCode = model.getCityCode();
            this.fromHour = model.getFromHour();
            this.toHour = model.getToHour();
            this.stopPricePercent = model.getStopPricePercent();
            this.meterPricePercent = model.getMeterPricePercent();
            this.entryPricePercent = model.getEntryPricePercent();
            this.charterPricePercent = model.getCharterPricePercent();
            this.minPricePercent = model.getMinPricePercent();

            try {
                JSONArray carArr = new JSONArray(model.getCarClass());
                for (int c = 0; c < carArr.length(); c++) {
                    JSONObject carObj = carArr.getJSONObject(c);
                    if (c == 0) {
                        carType = carType + carObj.getString("ClassName");
                    } else {
                        carType = carType + " ," + carObj.getString("ClassName");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (carType.matches("اقتصادی"))
                chbEconomical.setChecked(true);

            if (carType.matches("تشریفات"))
                chbCeremonies.setChecked(true);

            if (carType.matches("تاکسی"))
                chbTaxi.setChecked(true);

            if (carType.matches("ممتاز"))
                chbFormality.setChecked(true);

            edtFromTime.setText(fromHour + "");
            edtToTime.setText(toHour + "");
            spCity.setSelection(cityCode);
            edtMeter.setText(meterPricePercent + "");
            edtStop.setText(stopPricePercent + "");
            edtDisposal.setText(charterPricePercent + "");
            edtMinimum.setText(minPricePercent + "");
        }

        try {
            cityModels = new ArrayList<>();
            ArrayList<String> cityList = new ArrayList<>();
            cityList.add(0, "انتخاب نشده");
            JSONArray cityArr = new JSONArray(MyApplication.prefManager.getCity());
            for (int c = 0; c < cityArr.length(); c++) {
                JSONObject citiesObj = cityArr.getJSONObject(c);
                CityModel cityModel = new CityModel();
                cityModel.setId(citiesObj.getInt("CityId"));
                cityModel.setCityName(citiesObj.getString("CityName"));
                cityModels.add(cityModel);
                cityList.add(c + 1, citiesObj.getString("CityName"));
            }
            if (spCity == null) return;
            spCity.setAdapter(new SpinnerAdapter(MyApplication.currentActivity, R.layout.item_spinner, cityList));
            spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        cityName = null;
                        cityCode2 = -1;
                        return;
                    }
                    cityName = cityModels.get(position - 1).getCityName();
                    cityCode2 = cityModels.get(position - 1).getId();
//                    getRates(cityCode);//todo
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

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


    private void addRates() {
        RequestHelper.builder(EndPoints.ADD_RATE)
//        {post} /api/manager/v2/pricing/increaseRate increaseRate
//        Params:
//                * @apiParam {int} cityCode
//                * @apiParam {int} fromHour
//                * @apiParam {int} toHour
//                * @apiParam {int} stopPricePercent
//                * @apiParam {int} metrPricePercent
//                * @apiParam {int} entryPricePercent
//                * @apiParam {int} charterPricePercent
//                * @apiParam {int} minPricePercent
//                * @apiParam {varchar(50)} carClass seprate with ','
                .addParam("cityCode", spCity.getSelectedItemId())
                .addParam("fromHour", edtFromTime.getText())
                .addParam("toHour", edtToTime.getText())
                .addParam("stopPricePercent", edtStop.getText())
                .addParam("metrPricePercent", edtMeter.getText())
                .addParam("charterPricePercent", edtDisposal.getText())
                .addParam("minPricePercent", edtMinimum.getText())
                .addParam("carClass", carClass)//todo
                .listener(addRatesCallBack)
                .post();
    }

    RequestHelper.Callback addRatesCallBack = new RequestHelper.Callback() {
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
