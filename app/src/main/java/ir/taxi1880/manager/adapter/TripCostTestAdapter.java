package ir.taxi1880.manager.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;

import ir.taxi1880.manager.databinding.ItemTestTripCostBinding;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.fragment.TripCostTestFragment;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.TripCostModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class TripCostTestAdapter extends BaseAdapter {

    private ArrayList<TripCostModel> tripCostModels;
    private LayoutInflater inflater;
    private TripCostModel tripCostModel;
    private ItemTestTripCostBinding binding;

    private int i;


    public TripCostTestAdapter(ArrayList<TripCostModel> tripCostModels) {
        this.tripCostModels = tripCostModels;
        this.inflater = LayoutInflater.from(MyApplication.context);
    }

    @Override
    public int getCount() {
        return tripCostModels.size();
    }

    @Override
    public Object getItem(int i) {
        return tripCostModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        tripCostModel = tripCostModels.get(position);
        binding = ItemTestTripCostBinding.inflate(inflater, viewGroup, false);
        TypefaceUtil.overrideFonts(binding.getRoot());
        TypefaceUtil.overrideFonts(binding.labelPriceItem,MyApplication.IraSanSBold);
        TypefaceUtil.overrideFonts((binding.priceItem),MyApplication.IraSanSBold);


        binding.wayItem.setText(tripCostModel.getWayName());
        binding.originItem.setText(tripCostModel.getOrigin()+"");
        binding.destItem.setText(tripCostModel.getDest()+"");
        binding.carTypeItem.setText(tripCostModel.getCarType());
        binding.timeItem.setText(tripCostModel.getTime()+"");
        binding.cityItem.setText(tripCostModel.getCity());
        binding.distanceItem.setText(tripCostModel.getDistance());
        binding.priceItem.setText(StringHelper.toPersianDigits(tripCostModel.getPrice()));

        binding.llmainItem.setOnClickListener(view1 -> {
            i=position;
            Log.i("DeleteTSTAdapter:","id =" + i);
        });
        binding.vfDelete.setDisplayedChild(1);
        binding.vfDelete.setOnClickListener(view1 -> {
            new GeneralDialog()
                    .type(1)
                    .message("آیا از حذف این مورد مطمپن هستید؟")
                    .firstButton("بله",()->deleteItem(tripCostModels.get(position).getId()))
                    .secondButton("خیر",null)
                    .show();
        });

        return binding.getRoot();
    }

    private void deleteItem(int id) {
        binding.vfDelete.setDisplayedChild(0);
        RequestHelper.builder(EndPoints.TRIP_COST_TEST)
                .addParam("id",id+"")
                .listener(deleteIdListener)
                .delete();

    }

    RequestHelper.Callback deleteIdListener = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(()-> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());
                    Log.i("DeleteTSTAdapter:",args[0].toString());

                    boolean success = object.getBoolean("success");
                    String message = object.getString("message");

                    if (success)
                    {

                        new GeneralDialog()
                                .type(2)
                                .message(message)
                                .firstButton("باشه",null)
                                .show();

                        binding.vfDelete.setDisplayedChild(1);
                        TripCostTestFragment.getTripCost();
                    }
                    else{
                        new GeneralDialog()
                                .type(3)
                                .message(message)
                                .firstButton("باشه",null)
                                .show();
                        binding.vfDelete.setDisplayedChild(1);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.vfDelete.setDisplayedChild(1);
                }

            });

        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(()->{
                binding.vfDelete.setDisplayedChild(1);
            });
        }
    };
}

