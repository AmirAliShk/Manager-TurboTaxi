package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.RecordTripCostAdapter;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.AddTripCostDialog;
import ir.taxi1880.manager.model.TripCostModel;


public class RecordTripCostFragment extends Fragment {

    Unbinder unbinder;
    ArrayList<TripCostModel> tripCostModels;
    RecordTripCostAdapter tripCostAdapter;
    @BindView(R.id.tripCostList)
    ListView tripCostList;

    @OnClick(R.id.btnBack)
    void onBack(){
        MyApplication.currentActivity.onBackPressed();
    }

    @OnClick(R.id.imgAddTypeOfTrip)
    void onAddClicked()
    {
        new AddTripCostDialog().show();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_record_trip_cost, container, false);
        unbinder = ButterKnife.bind(this, view);

        getData();

        tripCostAdapter = new RecordTripCostAdapter(tripCostModels);
        tripCostList.setAdapter(tripCostAdapter);

        return view;
    }

    private void getData()
    {
        tripCostModels = new ArrayList<>();

        TripCostModel tripCostModel1 = new TripCostModel();
        tripCostModel1.setWayName("طولانی");
        tripCostModel1.setOrigin("بلوار حر");
        tripCostModel1.setDest("احمد اباد");
        tripCostModel1.setTime("25 قیقه");
        tripCostModel1.setDistance("10 کیلومتر");
        tripCostModel1.setPrice("18000 تومان");



        TripCostModel tripCostModel2 = new TripCostModel();
        tripCostModel2.setWayName("کوتاه");
        tripCostModel2.setOrigin("سلمان فارسی");
        tripCostModel2.setDest("پاستور");
        tripCostModel2.setTime("10 دقیقه");
        tripCostModel2.setDistance("1 کیلومتر");
        tripCostModel2.setPrice("4000 تومان");



        TripCostModel tripCostModel3 = new TripCostModel();
        tripCostModel3.setWayName("خیلی طولانی");
        tripCostModel3.setOrigin("انتهای فداییان اسلام");
        tripCostModel3.setDest("بیمارستان طالقانی");
        tripCostModel3.setTime("55 قیقه");
        tripCostModel3.setDistance("20 کیلومتر");
        tripCostModel3.setPrice("28000 تومان");

        TripCostModel tripCostModel4 = new TripCostModel();
        tripCostModel4.setWayName("خیلی طولانی");
        tripCostModel4.setOrigin("انتهای دندان پزشکان");
        tripCostModel4.setDest("لاله دوازده");
        tripCostModel4.setTime("55 قیقه");
        tripCostModel4.setDistance("20 کیلومتر");
        tripCostModel4.setPrice("28000 تومان");

        tripCostModels.add(tripCostModel1);
        tripCostModels.add(tripCostModel2);
        tripCostModels.add(tripCostModel3);
        tripCostModels.add(tripCostModel4);


    }

}