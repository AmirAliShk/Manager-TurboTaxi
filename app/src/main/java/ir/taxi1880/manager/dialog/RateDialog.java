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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.SpinnerAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.fragment.RateFragment;
import ir.taxi1880.manager.helper.KeyBoardHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;
import ir.taxi1880.manager.model.RateModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.fragment.RateFragment.getRates;
import static ir.taxi1880.manager.fragment.RateFragment.spCity1;

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
    String carType = "";
    private String cityName = "";
    private int cityCode2;
    ArrayList<CityModel> cityModels;
    String strCarClass = "";

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
        if (edtFromTime.getText() == null ||
                edtToTime.getText() == null ||
                spCity.getSelectedItemId() == 0 ||
                edtMeter.getText() == null ||
                edtStop.getText() == null ||
                edtDisposal.getText() == null ||
                edtMinimum.getText() == null
                || strCarClass.equals("")) {
            MyApplication.Toast("لطفا تمام فیلد ها رو کامل کنید.", Toast.LENGTH_SHORT);
        } else {
            if (model != null) {
                editRate();
            } else if (model == null) {
                addRates();
            }
        }
    }

    @OnClick(R.id.imgCancelDialog)
    void onCancel(){
        dismiss();
    }

    public void show(RateModel model) {

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
            if (carType.contains("اقتصادي")) {
                chbEconomical.setChecked(true);
                if (strCarClass.equals("")) {
                    strCarClass = "1";
                } else {
                    strCarClass += ",1";
                }
            }

            if (carType.contains("ممتاز")) {
                chbFormality.setChecked(true);
                if (strCarClass.equals("")) {
                    strCarClass = "2";
                } else {
                    strCarClass += ",2";
                }
            }

            if (carType.contains("تشريفات")) {
                chbCeremonies.setChecked(true);
                if (strCarClass.equals("")) {
                    strCarClass = "3";
                } else {
                    strCarClass += ",3";
                }
            }

            if (carType.contains("تاکسي")) {
                chbTaxi.setChecked(true);
                if (strCarClass.equals("")) {
                    strCarClass = "4";
                } else {
                    strCarClass += ",4";
                }
            }

            edtFromTime.setText(fromHour + "");
            edtToTime.setText(toHour + "");
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
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spCity.setSelection(cityCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        chbEconomical.setOnCheckedChangeListener((compoundButton, b) -> {
            if (chbEconomical.isChecked()) {
                if (strCarClass.equals("")) {
                    strCarClass = "1";
                } else {
                    strCarClass += ",1";
                }
            } else {
               strCarClass =   strCarClass.replace("1", "");
            }
        });

        chbCeremonies.setOnCheckedChangeListener((compoundButton, b) -> {
            if (chbCeremonies.isChecked()) {
                if (strCarClass.equals("")) {
                    strCarClass = "3";
                } else {
                    strCarClass += ",3";
                }
            } else {
                strCarClass =  strCarClass.replace(",3", "");
            }
        });

        chbTaxi.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (strCarClass.equals("")) {
                    strCarClass = "4";
                } else {
                    strCarClass += ",4";
                }
            } else {
              strCarClass =  strCarClass.replace(",4", "");
            }
        });

        chbFormality.setOnCheckedChangeListener((compoundButton, b) -> {
            if (chbFormality.isChecked()) {
                if (strCarClass.equals("")) {
                    strCarClass = "2";
                } else {
                    strCarClass += ",2";
                }
            } else {
                strCarClass =  strCarClass.replace(",2", "");
            }
        });

        edtFromTime.requestFocus();
        MyApplication.handler.postDelayed(() -> KeyBoardHelper.showKeyboard(MyApplication.context), 300);
        dialog.show();
    }

    private void editRate() {
        RequestHelper.builder(EndPoints.EDIT_RATE)
                .addParam("increaseRateId", increaseRateId + "")
                .addParam("cityCode", spCity.getSelectedItemId())
                .addParam("fromHour", edtFromTime.getText())
                .addParam("toHour", edtToTime.getText())
                .addParam("stopPricePercent", edtStop.getText())
                .addParam("metrPricePercent", edtMeter.getText())
                .addParam("charterPricePercent", edtDisposal.getText())
                .addParam("minPricePercent", edtMinimum.getText())
                .addParam("entryPricePercent", 0)
                .addParam("carClass", strCarClass)
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
                    boolean success = object.getBoolean("success");
                    spCity1.setSelection((int) spCity.getSelectedItemId());
                    getRates((int) spCity.getSelectedItemId());
                    new GeneralDialog()
                            .message(message)
                            .cancelable(false)
                            .firstButton("باشه", null)
                            .type(2)
                            .show();
                    dismiss();
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
                .addParam("cityCode", spCity.getSelectedItemId())
                .addParam("fromHour", edtFromTime.getText())
                .addParam("toHour", edtToTime.getText())
                .addParam("stopPricePercent", edtStop.getText())
                .addParam("metrPricePercent", edtMeter.getText())
                .addParam("charterPricePercent", edtDisposal.getText())
                .addParam("minPricePercent", edtMinimum.getText())
                .addParam("entryPricePercent", 0)
                .addParam("carClass", strCarClass)
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
                    boolean success = object.getBoolean("success");
                    spCity1.setSelection((int) spCity.getSelectedItemId());
                    getRates((int) spCity.getSelectedItemId());
                    if (success) {
                        new GeneralDialog()
                                .message(message)
                                .cancelable(false)
                                .firstButton("باشه", null)
                                .type(2)
                                .show();
                    } else {
                        new GeneralDialog()
                                .message(message)
                                .cancelable(false)
                                .firstButton("باشه", null)
                                .type(3)
                                .show();
                    }
                    dismiss();

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

    private static void dismiss() {
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
