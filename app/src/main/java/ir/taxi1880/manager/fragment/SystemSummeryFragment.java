package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentSystemSummeryBinding;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class SystemSummeryFragment extends Fragment {

    FragmentSystemSummeryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSystemSummeryBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());

        binding.btnBack.setOnClickListener(view -> {
            MyApplication.currentActivity.onBackPressed();
        });

        binding.imgRefresh.setOnClickListener(view -> {
            getSystemSummery();
        });

        getSystemSummery();

        return binding.getRoot();
    }

    private void getSystemSummery() {
        RequestHelper.builder(EndPoints.SUMMERY)
                .listener(summeryCallBack)
                .get();
    }

    RequestHelper.Callback summeryCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
//            {"allWaitedTrip":15,"tripWaitForStation":10,"activeOperatorsForStation":8,"maxTripWaitedTime":2,"mistake":1,"complaint":1,"driverRegistration":1}
                    JSONObject object = new JSONObject(args[0].toString());

                    binding.txtAllWaitedTrip.setText(StringHelper.toPersianDigits(object.getInt("allWaitedTrip") + ""));
                    binding.txtTripWaitForStation.setText(StringHelper.toPersianDigits(object.getInt("tripWaitForStation") + ""));
                    binding.txtActiveOperatorsForStation.setText(StringHelper.toPersianDigits(object.getInt("activeOperatorsForStation") + ""));
                    binding.txtMaxTripWaitedTime.setText(StringHelper.toPersianDigits(object.getInt("maxTripWaitedTime") + ""));
                    binding.txtMistake.setText(StringHelper.toPersianDigits(object.getInt("mistake") + ""));
                    binding.txtComplaint.setText(StringHelper.toPersianDigits(object.getInt("complaint") + ""));
                    binding.txtDriverRegistration.setText(StringHelper.toPersianDigits(object.getInt("driverRegistration") + ""));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
