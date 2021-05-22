package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;

import static android.content.ContentValues.TAG;

public class RateFragment extends Fragment {

    Unbinder unbinder;
    ArrayList<CityModel> cityModels;
    private String cityName = "";
    private String cityLatinName = "";
    private int cityCode;

    @BindView(R.id.spCity)
    Spinner spCity;

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

        MyApplication.handler.postDelayed(() -> initCitySpinner(), 200);

        return view;
    }

    private void initCitySpinner() {
        cityModels = new ArrayList<>();
        ArrayList<String> cityList = new ArrayList<String>();
        try {
            JSONArray cityArr = new JSONArray();//todo
            cityList.add(0, "انتخاب نشده");
            for (int i = 0; i < cityArr.length(); i++) {
                JSONObject cityObj = cityArr.getJSONObject(i);
                CityModel cityModel = new CityModel();
                cityModel.setCity(cityObj.getString("cityname"));
                cityModel.setId(cityObj.getInt("cityid"));
                cityModel.setCityLatin(cityObj.getString("latinName"));
                cityModels.add(cityModel);
                cityList.add(i + 1, cityObj.getString("cityname"));
            }
            if (spCity == null) return;
            spCity.setAdapter(new SpinnerAdapter(MyApplication.currentActivity, R.layout.item_spinner, cityList));
            spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        cityName = null;
                        cityLatinName = null;
                        cityCode = -1;
                        return;
                    }
                    cityName = cityModels.get(position - 1).getCity();
                    cityLatinName = cityModels.get(position - 1).getCityLatin();
                    cityCode = cityModels.get(position - 1).getId();
                    Log.i(TAG, "onItemSelected: " + cityName);
                    Log.i(TAG, "onItemSelected: " + cityLatinName);
                    Log.i(TAG, "onItemSelected: " + cityCode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
