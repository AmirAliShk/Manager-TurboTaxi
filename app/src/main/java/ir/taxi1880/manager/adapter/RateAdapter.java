package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ItemRateBinding;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.dialog.RateDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.RateModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.fragment.RateFragment.getRates;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<RateModel> rateModelArrayList;
    RateModel rateModel;
    ItemRateBinding binding;

    public RateAdapter(Context context, ArrayList<RateModel> salaryModelArrayList) {
        this.rateModelArrayList = salaryModelArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRateBinding.inflate(inflater, parent, false);
        rateModel = rateModelArrayList.get(viewType);
        TypefaceUtil.overrideFonts(binding.getRoot());

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RateAdapter.ViewHolder holder, int position) {

        RateModel rateModel = rateModelArrayList.get(position);

        binding.txtFromTime.setText(rateModel.getFromHour() + "");
        binding.txtToTime.setText(rateModel.getToHour() + "");
        binding.txtStop.setText(rateModel.getStopPricePercent() + "");
        binding.txtMeter.setText(rateModel.getMeterPricePercent() + "");
        binding.txtMinimumPrice.setText(rateModel.getMinPricePercent() + "");
        binding.txtDisposal.setText(rateModel.getCharterPricePercent() + "");
        binding.txtEntry.setText(rateModel.getEntryPricePercent() + "");

        String carClass = "";
        try {
            JSONArray carArr = new JSONArray(rateModel.getCarClass());
            for (int c = 0; c < carArr.length(); c++) {
                JSONObject carObj = carArr.getJSONObject(c);
                if (c == 0) {
                    carClass = carClass + carObj.getString("ClassName");
                } else {
                    carClass = carClass + " ," + carObj.getString("ClassName");
                }
            }
            binding.txtCarClass.setText(carClass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.llRateItem.setOnClickListener((view1) -> new RateDialog().show(rateModel));

        binding.imgDelete.setOnClickListener((view1 -> new GeneralDialog()
                .message("آیا میخواهید این مدل قیمت دهی را حذف کنید؟")
                .firstButton("بله", () -> deleteRates(rateModel.getId()))
                .secondButton("خیر", null)
                .type(1)
                .show()));
    }

    @Override
    public int getItemCount() {
        return rateModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void deleteRates(int id) {
        RequestHelper.builder(EndPoints.DELETE_RATE)
                .addParam("increaseRateId", id)
                .listener(deleteRatesCallBack)
                .delete();
    }

    RequestHelper.Callback deleteRatesCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());

                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");
                    if (success) {
                        notifyDataSetChanged();
                        getRates(rateModel.getCityCode());
                    }
                    new GeneralDialog()
                            .message(message)
                            .cancelable(false)
                            .firstButton("باشه", null)
                            .type(2)
                            .show();

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
