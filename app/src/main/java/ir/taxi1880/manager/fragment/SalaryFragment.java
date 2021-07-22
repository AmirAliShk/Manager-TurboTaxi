package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.adapter.SalaryAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentSalaryBinding;
import ir.taxi1880.manager.dialog.SalaryDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.SalaryModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class SalaryFragment extends Fragment {

    FragmentSalaryBinding binding;

    ArrayList<SalaryModel> salaryModels;

    SalaryAdapter salaryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSalaryBinding.inflate(getLayoutInflater());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());

        getSalary();

        binding.btnBack.setOnClickListener(view -> MyApplication.currentActivity.onBackPressed());

        binding.imgAddSalaryPlan.setOnClickListener(view -> new SalaryDialog().show(null, refresh -> getSalary()));

        return binding.getRoot();
    }

    public void getSalary() {
        if (binding.vfSalary != null) {
            binding.vfSalary.setDisplayedChild(0);
        }

        RequestHelper.builder(EndPoints.SALARY)
                .listener(salaryCallBack)
                .get();
    }

    RequestHelper.Callback salaryCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
// [{"id":2,"fromHour":23,"toHour":2,"servicePer":2,"errorPer":1,"stationPer":3,"answerDriverPer":4,"complaintPer":5}]
                    salaryModels = new ArrayList<>();
                    JSONArray salariesArr = new JSONArray(args[0].toString());
                    for (int i = 0; i < salariesArr.length(); i++) {
                        JSONObject salaryObj = salariesArr.getJSONObject(i);
                        SalaryModel salariesModel = new SalaryModel();
                        salariesModel.setId(salaryObj.getInt("id"));
                        salariesModel.setFromHour(salaryObj.getInt("fromHour"));
                        salariesModel.setToHour(salaryObj.getInt("toHour"));
                        salariesModel.setServicePer(salaryObj.getInt("servicePer"));
                        salariesModel.setErrorPer(salaryObj.getInt("errorPer"));
                        salariesModel.setStationPer(salaryObj.getInt("stationPer"));
                        salariesModel.setAnswerDriverPer(salaryObj.getInt("answerDriverPer"));
                        salariesModel.setComplaintPer(salaryObj.getInt("complaintPer"));
                        salariesModel.setServicePrice(salaryObj.getString("servicePrice"));
                        salariesModel.setErrorPrice(salaryObj.getString("errorPrice"));
                        salariesModel.setStationPrice(salaryObj.getString("stationPrice"));
                        salariesModel.setAnswerDriverPrice(salaryObj.getString("answerDriverPrice"));
                        salariesModel.setComplaintPrice(salaryObj.getString("complaintPrice"));
                        salaryModels.add(salariesModel);
                    }
                    if (salaryModels.size() == 0) {
                        if (binding.vfSalary != null) {
                            binding.vfSalary.setDisplayedChild(2);
                        }
                    } else {
                        if (binding.vfSalary != null) {
                            binding.vfSalary.setDisplayedChild(1);
                        }
                        salaryAdapter = new SalaryAdapter(MyApplication.currentActivity,salaryModels, refresh -> getSalary());
                        salaryAdapter.notifyDataSetChanged();
                        binding.salaryList.setAdapter(salaryAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (binding.vfSalary != null) {
                    binding.vfSalary.setDisplayedChild(3);
                }
            });
            super.onFailure(reCall, e);
        }
    };
}
