package ir.taxi1880.manager.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.adapter.TripCostTestAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentRecordTripCostBinding;
import ir.taxi1880.manager.dialog.AddTripCostDialog;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.TripCostModel;
import ir.taxi1880.manager.okHttp.RequestHelper;


public class TripCostTestFragment extends Fragment {

    static ArrayList<TripCostModel> tripCostModels;
    static TripCostTestAdapter tripCostAdapter;
    @SuppressLint("StaticFieldLeak")
    static FragmentRecordTripCostBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        binding = FragmentRecordTripCostBinding.inflate(getLayoutInflater());
        TypefaceUtil.overrideFonts(binding.getRoot());

        getTripCost();

        binding.btnBack.setOnClickListener(view -> {
            MyApplication.currentActivity.onBackPressed();
        });

        binding.imgaddtypeoftrip.setOnClickListener(view -> {
            new AddTripCostDialog().show(isCreated -> {
                if (isCreated) {
                    getTripCost();
                }
            });
        });

        return binding.getRoot();

    }


    public static void getTripCost() {
        if (tripCostModels == null) {
            binding.vfTCT.setDisplayedChild(0);
        } else
            binding.vfTCT.setDisplayedChild(0);
            RequestHelper.builder(EndPoints.TRIP_COST_TEST)
                    .listener(TRIPCostListener)
                    .addPath("0")
                    .addPath("-1")
                    .get();
    }

    static RequestHelper.Callback TRIPCostListener = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    tripCostModels = new ArrayList<>();
                    JSONObject JsonObj = new JSONObject(args[0].toString());

                    Log.i("Salam", args[0].toString());

                    boolean success = JsonObj.getBoolean("success");
                    String message = JsonObj.getString("message");
                    if (success) {
                        JSONArray data = JsonObj.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataObj = data.getJSONObject(i);
                            TripCostModel tripCostModel = new TripCostModel();

                            tripCostModel.setId(dataObj.getInt("id"));
                            tripCostModel.setWayName(dataObj.getString("name"));
                            tripCostModel.setOrigin(dataObj.getInt("fromStation"));
                            tripCostModel.setDest(dataObj.getInt("toStation"));
                            tripCostModel.setCarType(dataObj.getString("ClassName"));
                            tripCostModel.setDistance(dataObj.getString("distance"));
                            tripCostModel.setTime(dataObj.getString("duration"));
                            tripCostModel.setPrice(dataObj.getString("tripPrice"));
                            tripCostModel.setCity(dataObj.getString("cityName"));

//                            tripCostModel.setPrice(dataObj.getString("sellingPrice"));
//                            int carType = dataObj.getInt("carType");
                            tripCostModels.add(tripCostModel);
                        }

                        tripCostAdapter = new TripCostTestAdapter(tripCostModels);
                        binding.tripCostList.setAdapter(tripCostAdapter);
                    } else {
                        new GeneralDialog()
                                .type(1)
                                .message(message)
                                .secondButton("باشه", null)
                                .cancelable(false)
                                .show();
                    }

                    if (tripCostModels.size() == 0) {
                        binding.vfTCT.setDisplayedChild(2);
                    } else {
                        binding.vfTCT.setDisplayedChild(1);
                    }

                } catch (JSONException e) {
                    binding.vfTCT.setDisplayedChild(3);
                    e.printStackTrace();
                }
            });


        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(()->
            {
                binding.vfTCT.setDisplayedChild(3);
            });
        }

    };


}