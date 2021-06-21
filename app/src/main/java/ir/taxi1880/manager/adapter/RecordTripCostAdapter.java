package ir.taxi1880.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ItemTestTripCostBinding;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.TripCostModel;

public class RecordTripCostAdapter extends BaseAdapter {

    ArrayList<TripCostModel> tripCostModels;
    LayoutInflater inflater;
    int position;
    TripCostModel tripCostModel;
    Unbinder unbinder;
    ItemTestTripCostBinding binding;

    @BindView(R.id.wayRTCItem)
    TextView wayTxt;

    @BindView(R.id.originRTCItem)
    TextView originTxt;

    @BindView(R.id.destRTCItem)
    TextView destTxt;

    @BindView(R.id.timeRTCItem)
    TextView timeTxt;

    @BindView(R.id.distanceRTCItem)
    TextView distanceTxt;

    @BindView(R.id.priceRTCItem)
    TextView priceTxt;

    public RecordTripCostAdapter(ArrayList<TripCostModel> tripCostModels) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = view;

        tripCostModel = tripCostModels.get(i);

        if (myView == null) {
            myView = inflater.inflate(R.layout.item_test_trip_cost, viewGroup, false);
            TypefaceUtil.overrideFonts(myView);
        }

        unbinder = ButterKnife.bind(this, myView);

        wayTxt.setText(tripCostModel.getWayName());
        originTxt.setText(tripCostModel.getOrigin());
        destTxt.setText(tripCostModel.getDest());
        timeTxt.setText(tripCostModel.getTime());
        distanceTxt.setText(tripCostModel.getDistance());
        priceTxt.setText(tripCostModel.getPrice());

        return myView;


//        View myView = view;
//
//        tripCostModel = tripCostModels.get(i);
//
//        if (myView == null) {
//            binding = RecordTripCostItemBinding.inflate(inflater);
////            myView = inflater.inflate(R.layout.record_trip_cost_item, viewGroup, false);
//            TypefaceUtil.overrideFonts(binding.getRoot());
//        }
//
//
//
//        binding.wayRTCItem.setText(tripCostModel.getWayName());
//        binding.originRTCItem.setText(tripCostModel.getOrigin());
//        binding.destRTCItem.setText(tripCostModel.getDest());
//        binding.timeRTCItem.setText(tripCostModel.getTime());
//        binding.distanceRTCItem.setText(tripCostModel.getDistance());
//        binding.priceRTCItem.setText(tripCostModel.getPrice());
//
//        return binding.getRoot();
    }
}
