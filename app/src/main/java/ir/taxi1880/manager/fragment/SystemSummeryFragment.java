package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class SystemSummeryFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.txtAllWaitedTrip)
    TextView txtAllWaitedTrip;

    @BindView(R.id.txtTripWaitForStation)
    TextView txtTripWaitForStation;

    @BindView(R.id.txtActiveOperatorsForStation)
    TextView txtActiveOperatorsForStation;

    @BindView(R.id.txtMaxTripWaitedTime)
    TextView txtMaxTripWaitedTime;

    @BindView(R.id.txtMistake)
    TextView txtMistake;

    @BindView(R.id.txtComplaint)
    TextView txtComplaint;

    @BindView(R.id.txtDriverRegistration)
    TextView txtDriverRegistration;

    @OnClick(R.id.btnBack)
    void onBack() {
        MyApplication.currentActivity.onBackPressed();
    }

    @OnClick(R.id.imgRefresh)
    void onRefresh() {
        getSystemSummery();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_summery, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);
        unbinder = ButterKnife.bind(this, view);

        getSystemSummery();

        return view;
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

                    txtAllWaitedTrip.setText(StringHelper.toPersianDigits(object.getInt("allWaitedTrip") + ""));
                    txtTripWaitForStation.setText(StringHelper.toPersianDigits(object.getInt("tripWaitForStation") + ""));
                    txtActiveOperatorsForStation.setText(StringHelper.toPersianDigits(object.getInt("activeOperatorsForStation") + ""));
                    txtMaxTripWaitedTime.setText(StringHelper.toPersianDigits(object.getInt("maxTripWaitedTime") + ""));
                    txtMistake.setText(StringHelper.toPersianDigits(object.getInt("mistake") + ""));
                    txtComplaint.setText(StringHelper.toPersianDigits(object.getInt("complaint") + ""));
                    txtDriverRegistration.setText(StringHelper.toPersianDigits(object.getInt("driverRegistration") + ""));

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
        unbinder.unbind();
    }
}
