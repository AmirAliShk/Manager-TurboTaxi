package ir.taxi1880.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.dialog.RateDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;
import ir.taxi1880.manager.model.RateModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;
import static ir.taxi1880.manager.fragment.RateFragment.getRates;

public class RateAdapter extends BaseAdapter {

    private ArrayList<RateModel> rateModels;
    LayoutInflater inflater;
    int position;
    Unbinder unbinder;
    RateModel rateModel;
    @BindView(R.id.llRateItem)
    LinearLayout llRateItem;

    @BindView(R.id.txtFromTime)
    TextView txtFromTime;

    @BindView(R.id.txtToTime)
    TextView txtToTime;

    @BindView(R.id.txtStop)
    TextView txtStop;

    @BindView(R.id.txtMeter)
    TextView txtMeter;

    @BindView(R.id.txtMinimumPrice)
    TextView txtMinimumPrice;

    @BindView(R.id.txtDisposal)
    TextView txtDisposal;

    @BindView(R.id.txtCarClass)
    TextView txtCarClass;

    @BindView(R.id.imgDelete)
    ImageView imgDelete;

    public RateAdapter(ArrayList<RateModel> rateModels) {
        this.rateModels = rateModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rateModels.size();
    }

    @Override
    public Object getItem(int i) {
        return rateModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = view;

        rateModel = rateModels.get(i);

        if (myView == null) {
            myView = inflater.inflate(R.layout.item_rate, viewGroup, false);
            TypefaceUtil.overrideFonts(myView);
        }
        unbinder = ButterKnife.bind(this, myView);

        txtFromTime.setText(rateModel.getFromHour() + "");
        txtToTime.setText(rateModel.getToHour() + "");
        txtStop.setText(rateModel.getStopPricePercent() + "");
        txtMeter.setText(rateModel.getMeterPricePercent() + "");
        txtMinimumPrice.setText(rateModel.getMinPricePercent() + "");
        txtDisposal.setText(rateModel.getCharterPricePercent() + "");

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
            txtCarClass.setText(carClass);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        llRateItem.setOnClickListener((view1) -> {
            new RateDialog()
                    .show(rateModel);
            position = i;
        });

        imgDelete.setOnClickListener((view1 -> {
            new GeneralDialog()
                    .message("آیا میخواهید این مدل قیمت دهی را حذف کنید؟")
                    .firstButton("بله", () -> deleteRates(rateModel.getId()))
                    .secondButton("خیر", null)
                    .type(1)
                    .show();
        }));

        return myView;
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
                        rateModels.remove(position);
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
