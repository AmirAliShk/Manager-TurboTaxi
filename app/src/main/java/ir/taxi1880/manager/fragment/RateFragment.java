package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.QueuesAdapter;
import ir.taxi1880.manager.adapter.RateAdapter;
import ir.taxi1880.manager.adapter.SpinnerAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.app.PrefManager;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.dialog.RateDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;
import ir.taxi1880.manager.model.QueuesModel;
import ir.taxi1880.manager.model.RateModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class RateFragment extends Fragment {

    Unbinder unbinder;
    ArrayList<CityModel> cityModels;
    ArrayList<RateModel> rateModels;
    RateAdapter rateAdapter;
    static String cityName = "";
    static int cityCode;

    @BindView(R.id.rateList)
    ListView rateList;

    @BindView(R.id.spCity)
    Spinner spCity;

    @BindView(R.id.vfRate)
    ViewFlipper vfRate;

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    @OnClick(R.id.fabAdd)
    void onAdd() {
        new RateDialog()
                .show(null, new RateDialog.RateDialogListener() {
                    @Override
                    public void rateModel(RateModel model) {

                    }
                });  // TODO check null value
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        MyApplication.currentActivity.onBackPressed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);
        unbinder = ButterKnife.bind(this, view);
        if (vfRate != null)
            vfRate.setDisplayedChild(0);
        fabAdd.setVisibility(View.GONE);
        MyApplication.handler.postDelayed(() -> getCity(), 200);

        return view;
    }

    private void getCity() {
        RequestHelper.builder(EndPoints.CITY)
                .listener(getCityCallBack)
                .get();
    }

    RequestHelper.Callback getCityCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    cityModels = new ArrayList<>();
                    ArrayList<String> cityList = new ArrayList<String>();
                    JSONArray citiesArr = new JSONArray(args[0].toString());
                    MyApplication.prefManager.setCity(args[0].toString());
                    cityList.add(0, "انتخاب نشده");
                    for (int i = 0; i < citiesArr.length(); i++) {
                        JSONObject citiesObj = citiesArr.getJSONObject(i);
                        CityModel cityModel = new CityModel();
                        cityModel.setId(citiesObj.getInt("CityId"));
                        cityModel.setCityName(citiesObj.getString("CityName"));
                        cityModels.add(cityModel);
                        cityList.add(i + 1, citiesObj.getString("CityName"));
                    }
                    if (spCity == null) return;

                    spCity.setAdapter(new SpinnerAdapter(MyApplication.currentActivity, R.layout.item_spinner, cityList));
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                cityName = null;
                                cityCode = -1;
                                return;
                            }
                            cityName = cityModels.get(position - 1).getCityName();
                            cityCode = cityModels.get(position - 1).getId();

                            getRates(cityCode);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
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

    private void getRates(int cityCode) {
        if (vfRate != null)
            vfRate.setDisplayedChild(1);
        RequestHelper.builder(EndPoints.GET_RATE + cityCode)
                .listener(getRatesCallBack)
                .get();
    }

    RequestHelper.Callback getRatesCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    rateModels = new ArrayList<RateModel>();
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");
                    if (success) {
                        fabAdd.setVisibility(View.VISIBLE);
                        JSONArray dataArr = object.getJSONArray("data");
                        for (int i = 0; i < dataArr.length(); i++) {
                            JSONObject dataObj = dataArr.getJSONObject(i);
                            RateModel model = new RateModel();

                            model.setId(dataObj.getInt("id"));
                            model.setFromHour(dataObj.getInt("fromHour"));
                            model.setToHour(dataObj.getInt("toHour"));
                            model.setStopPricePercent(dataObj.getInt("stopPricePercent"));
                            model.setMeterPricePercent(dataObj.getInt("metrPricePercent"));
                            model.setEntryPricePercent(dataObj.getInt("entryPricePercent"));
                            model.setCharterPricePercent(dataObj.getInt("charterPricePercent"));
                            model.setMinPricePercent(dataObj.getInt("minPricePercent"));
                            model.setCityCode(dataObj.getInt("cityCode"));
                            model.setCarClass(dataObj.getString("carClass"));

                            rateModels.add(model);
                        }

                        if (rateModels.size() == 0) {
                            if (vfRate != null)
                                vfRate.setDisplayedChild(3);
                        } else {
                            if (vfRate != null)
                                vfRate.setDisplayedChild(2);
                            rateAdapter = new RateAdapter(rateModels);
                            rateList.setAdapter(rateAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (vfRate != null)
                        vfRate.setDisplayedChild(4);
                }

            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            super.onFailure(reCall, e);
            if (vfRate != null)
                vfRate.setDisplayedChild(4);
        }
    };

}
