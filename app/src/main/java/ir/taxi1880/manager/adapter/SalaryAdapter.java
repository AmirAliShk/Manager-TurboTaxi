package ir.taxi1880.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ItemSalaryBinding;
import ir.taxi1880.manager.dialog.ChangeQueueCapacityDialog;
import ir.taxi1880.manager.dialog.SalaryDialog;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.SalaryModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class SalaryAdapter extends BaseAdapter {

    ArrayList<SalaryModel> salaryModels;

    LayoutInflater inflater;

    SalaryModel salaryModel;

    public interface Listener {
        void refresh(boolean refresh);
    }

    private Listener listener;

    public SalaryAdapter(ArrayList<SalaryModel> salaryModels, Listener listener) {
        this.salaryModels = salaryModels;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return salaryModels.size();
    }

    @Override
    public Object getItem(int i) {
        return salaryModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        View myView = view;
        ItemSalaryBinding binding;
        binding = ItemSalaryBinding.inflate(inflater, viewGroup, false);
        salaryModel = salaryModels.get(i);
        Object item = getItem(i);
        TypefaceUtil.overrideFonts(binding.getRoot());

        binding.txtFromTime.setText(StringHelper.toPersianDigits(salaryModel.getFromHour() + ""));
        binding.txtToTime.setText(StringHelper.toPersianDigits(salaryModel.getToHour() + ""));
        binding.txtCheckMistakePercent.setText(StringHelper.toPersianDigits(salaryModel.getErrorPer() + ""));
        binding.txtTripRegisterPercent.setText(StringHelper.toPersianDigits(salaryModel.getServicePer() + ""));
        binding.txtDeterminationPercent.setText(StringHelper.toPersianDigits(salaryModel.getStationPer() + ""));
        binding.txtDriverCallPercent.setText(StringHelper.toPersianDigits(salaryModel.getAnswerDriverPer() + ""));
        binding.txtCheckComplaintPercent.setText(StringHelper.toPersianDigits(salaryModel.getComplaintPer() + ""));
        binding.txtCheckMistake.setText(StringHelper.toPersianDigits(salaryModel.getErrorPrice()));
        binding.txtTripRegister.setText(StringHelper.toPersianDigits(salaryModel.getServicePrice()));
        binding.txtDetermination.setText(StringHelper.toPersianDigits(salaryModel.getStationPrice()));
        binding.txtDriverCall.setText(StringHelper.toPersianDigits(salaryModel.getAnswerDriverPrice()));
        binding.txtCheckComplaint.setText(StringHelper.toPersianDigits(salaryModel.getComplaintPrice()));

        binding.imgDelete.setOnClickListener(view1 -> deleteSalary());

        binding.imgEdit.setOnClickListener(view1 -> new SalaryDialog().show(salaryModel, refresh -> listener.refresh(true)));

        return binding.getRoot();
    }

    public void deleteSalary() {
        RequestHelper.builder(EndPoints.SALARY)
                .addParam("id", salaryModel.getId())
                .listener(deleteCallBack)
                .delete();
    }

    RequestHelper.Callback deleteCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {

//            {"status":true,"message":"با موفقیت ویرایش شد"}
                try {
                    JSONObject object = new JSONObject(args[0].toString());

                    String message = object.getString("message");
                    boolean status = object.getBoolean("status");
                    if (status) {
                        listener.refresh(true);
                    }
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
