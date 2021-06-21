package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.adapter.RecordTripCostAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentRecordTripCostBinding;
import ir.taxi1880.manager.dialog.AddTripCostDialog;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.TripCostModel;
import ir.taxi1880.manager.okHttp.RequestHelper;


public class RecordTripCostFragment extends Fragment {

    ArrayList<TripCostModel> tripCostModels;
    RecordTripCostAdapter tripCostAdapter;
    FragmentRecordTripCostBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        binding = FragmentRecordTripCostBinding.inflate(getLayoutInflater());
        TypefaceUtil.overrideFonts(binding.getRoot());

        getData();

        binding.btnBack.setOnClickListener(view -> {
            MyApplication.currentActivity.onBackPressed();
        });

        binding.imgaddtypeoftrip.setOnClickListener(view -> {
            new AddTripCostDialog().show();
        });

        return binding.getRoot();

    }


    private void getData() {
        RequestHelper.builder(EndPoints.TYPE_OF_WAY)
                .listener(getTypeOfWay)
                .addPath("0")
                .addPath("0")
                .get();
    }

        RequestHelper.Callback getTypeOfWay = new RequestHelper.Callback() {
            @Override
            public void onResponse(Runnable reCall, Object... args) {
                MyApplication.handler.post(() -> {
                    try {
                        tripCostModels=new ArrayList<>();
                        JSONObject JsonObj = new JSONObject(args[0].toString());
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
                                tripCostModel.setCarType(dataObj.getInt("carType"));
                                tripCostModel.setPrice(dataObj.getString("sellingPrice"));
                                int carType = dataObj.getInt("carType");
                                tripCostModels.add(tripCostModel);
                            }

                            tripCostAdapter = new RecordTripCostAdapter(tripCostModels);
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
                            binding.vfRTC.setDisplayedChild(1);
                        } else {
                            binding.vfRTC.setDisplayedChild(0);
                        }

                    } catch (JSONException e) {
                        binding.vfRTC.setDisplayedChild(2);
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