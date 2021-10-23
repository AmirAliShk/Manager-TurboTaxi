package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.RateAdapter;
import ir.taxi1880.manager.adapter.SpinnerAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentRateBinding;
import ir.taxi1880.manager.dialog.RateDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;
import ir.taxi1880.manager.model.RateModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class RateFragment extends Fragment {

    public static FragmentRateBinding binding;
    ArrayList<CityModel> cityModels;
    static ArrayList<RateModel> rateModels;
    static RateAdapter rateAdapter;
    String cityName = "";
    int cityCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRateBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());

        if (binding.vfRate != null)
            binding.vfRate.setDisplayedChild(0);
        binding.fabAdd.setVisibility(View.GONE);

        binding.fabAdd.setOnClickListener(view -> {
            new RateDialog()
                    .show(null);
        });

        binding.imgRefresh.setOnClickListener(view -> {
            getRates(cityCode);
        });

        binding.btnBack.setOnClickListener(view -> {
            MyApplication.currentActivity.onBackPressed();
        });

        getCity();

        return binding.getRoot();
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
                    MyApplication.prefManager.setCity(args[0].toString());
                    initCity();
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

    public void initCity() {
        cityModels = new ArrayList<>();
        ArrayList<String> cityList = new ArrayList<String>();
        try {
            JSONArray cityArr = new JSONArray(MyApplication.prefManager.getCity());
            cityList.add(0, "انتخاب نشده");
            for (int i = 0; i < cityArr.length(); i++) {
                JSONObject citiesObj = cityArr.getJSONObject(i);
                CityModel cityModel = new CityModel();
                cityModel.setId(citiesObj.getInt("CityId"));
                cityModel.setCityName(citiesObj.getString("CityName"));
                cityModels.add(cityModel);
                cityList.add(i + 1, citiesObj.getString("CityName"));
            }
            if (binding.spCity == null) return;

            binding.spCity.setAdapter(new SpinnerAdapter(MyApplication.currentActivity, R.layout.item_spinner, cityList));
            binding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        cityName = null;
                        cityCode = -1;
                        return;
                    }
                    cityName = cityModels.get(position - 1).getCityName();
                    cityCode = cityModels.get(position - 1).getId();

//                    getRates(cityCode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getRates(int cityCode) {
        if (cityCode == 0 || cityCode == -1) {
            if (binding.vfRate != null) {
                binding.vfRate.setDisplayedChild(0);
            }
        } else {
            if (binding.vfRate != null)
                binding.vfRate.setDisplayedChild(1);
            RequestHelper.builder(EndPoints.GET_RATE + cityCode)
                    .listener(getRatesCallBack)
                    .get();
        }
    }

    static RequestHelper.Callback getRatesCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    rateModels = new ArrayList<RateModel>();
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");
                    if (success) {
                        binding.fabAdd.setVisibility(View.VISIBLE);
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
                            if (binding.vfRate != null)
                                binding.vfRate.setDisplayedChild(3);
                        } else {
                            if (binding.vfRate != null)
                                binding.vfRate.setDisplayedChild(2);
                            rateAdapter = new RateAdapter(MyApplication.currentActivity, rateModels);
                            binding.rateList.setAdapter(rateAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (binding.vfRate != null)
                        binding.vfRate.setDisplayedChild(4);
                }

            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            super.onFailure(reCall, e);
            if (binding.vfRate != null)
                binding.vfRate.setDisplayedChild(4);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }
}
