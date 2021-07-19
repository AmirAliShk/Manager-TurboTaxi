package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ir.taxi1880.manager.adapter.SalaryAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentSalaryBinding;
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

        return binding.getRoot();
    }

    public void getSalary() {
        if (binding.vfSalary != null) {
            binding.vfSalary.setDisplayedChild(0);
        }

        RequestHelper.builder(EndPoints.CITY)
                .addParam("", "")
                .listener(salaryCallBack)
                .get();
    }

    RequestHelper.Callback salaryCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {



                    salaryAdapter = new SalaryAdapter(salaryModels);
                    binding.salaryList.setAdapter(salaryAdapter);



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
