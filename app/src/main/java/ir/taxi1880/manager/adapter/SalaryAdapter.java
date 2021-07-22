package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ItemSalaryBinding;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.dialog.SalaryDialog;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.SalaryModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<SalaryModel> salaryModelArrayList;
    SalaryModel salaryModel;
    ItemSalaryBinding binding;

    public interface Listener {
        void refresh(boolean refresh);
    }

    private Listener listener;

    public SalaryAdapter(Context context, ArrayList<SalaryModel> salaryModelArrayList, Listener listener) {
        this.inflater = LayoutInflater.from(context);
        this.salaryModelArrayList = salaryModelArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SalaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemSalaryBinding.inflate(inflater, parent, false);
        salaryModel = salaryModelArrayList.get(viewType);
        TypefaceUtil.overrideFonts(binding.getRoot());

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryAdapter.ViewHolder holder, int position) {
        SalaryModel salaryModel = salaryModelArrayList.get(position);

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

        binding.imgDelete.setOnClickListener(view1 -> new GeneralDialog()
                .message("آیا میخواهید این مدل دستمزد را حذف کنید؟")
                .firstButton("بله", this::deleteSalary)
                .secondButton("خیر", null)
                .type(1)
                .show());

        binding.imgEdit.setOnClickListener(view1 -> new SalaryDialog().show(salaryModel, refresh -> listener.refresh(true)));

    }

    @Override
    public int getItemCount() {
        return salaryModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
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
