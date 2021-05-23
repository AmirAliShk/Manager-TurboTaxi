package ir.taxi1880.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CityModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class RateAdapter extends BaseAdapter {

    private ArrayList<CityModel> cityModels;//todo
    LayoutInflater inflater;
    int position;
    Unbinder unbinder;

    @BindView(R.id.lineTitle)
    TextView lineTitle;

    @BindView(R.id.sbNew)
    SwitchButton sbNew;

    @BindView(R.id.sbSupport)
    SwitchButton sbSupport;

    public RateAdapter(ArrayList<CityModel> cityModels) {//todo
        this.cityModels = cityModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cityModels.size();
    }

    @Override
    public Object getItem(int i) {
        return cityModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = view;

        CityModel currentLinesModel = cityModels.get(i);//todo

        if (myView == null) {
            myView = inflater.inflate(R.layout.item_rate, viewGroup, false);
            TypefaceUtil.overrideFonts(myView);
        }
        unbinder = ButterKnife.bind(this, myView);

//        sbSupport.setChecked(currentLinesModel.getSupport());

        sbNew.setOnCheckedChangeListener((compoundButton, b) -> {
//            getLineInfo(currentLinesModel.getId(), sbSupport.isChecked(), b);
//            sbThird = sbNew;
            position = i;
        });

        sbSupport.setOnCheckedChangeListener((compoundButton, b) -> {
//            getLineInfo(currentLinesModel.getId(), b, sbNew.isChecked());
//            sbThird = sbSupport;
            position = i;
        });

        return myView;
    }


    private void deleteRates() {
        RequestHelper.builder(EndPoints.DELETE_RATE)
                .addParam("IncreaseRateId", IncreaseRateId)
                .listener(deleteRatesCallBack)
                .delete();
    }

    RequestHelper.Callback deleteRatesCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());

                    String message = object.getString("message");
                    boolean status = object.getBoolean("status");

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
