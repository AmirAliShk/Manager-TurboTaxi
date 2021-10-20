package ir.taxi1880.manager.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ItemLineBinding;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.LinesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class LinesAdapter extends BaseAdapter {
    private ArrayList<LinesModel> linesModels;
    LayoutInflater inflater;
    SwitchButton sbThird;
    int position;
    ItemLineBinding binding;

    public LinesAdapter(ArrayList<LinesModel> linesModels) {
        this.linesModels = linesModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return linesModels.size();
    }

    @Override
    public Object getItem(int i) {
        return linesModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinesModel currentLinesModel = linesModels.get(i);

        binding = ItemLineBinding.inflate(inflater, viewGroup, false);
        TypefaceUtil.overrideFonts(binding.getRoot());

        binding.lineTitle.setText(currentLinesModel.getName());
        binding.sbNew.setChecked(currentLinesModel.getNew());
        binding.sbSupport.setChecked(currentLinesModel.getSupport());

        binding.sbNew.setOnCheckedChangeListener((compoundButton, b) ->

        {
            getLineInfo(currentLinesModel.getId(), binding.sbSupport.isChecked(), b);
            sbThird = binding.sbNew;
            position = i;
        });

        binding.sbSupport.setOnCheckedChangeListener((compoundButton, b) ->

        {
            getLineInfo(currentLinesModel.getId(), b, binding.sbNew.isChecked());
            sbThird = binding.sbSupport;
            position = i;
        });

        return binding.getRoot();
    }

    private void getLineInfo(int id, boolean support, boolean newCall) {
        RequestHelper.builder(EndPoints.GET_LINE)
                .listener(lineInfoCallBack)
                .addParam("id", id)
                .addParam("support", support)
                .addParam("new", newCall)
                .put();
    }

    RequestHelper.Callback lineInfoCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(args[0].toString());
                    boolean status = object.getBoolean("status");
                    if (!status) {
                        new GeneralDialog().message("خطایی پیش آمده، لطفا دوباره امتحان کنید.")
                                .cancelable(false)
                                .secondButton("بستن", null)
                                .type(3)
                                .show();
                    }
                } catch (JSONException e) {
                    new GeneralDialog().message("خطایی پیش آمده، لطفا دوباره امتحان کنید.")
                            .cancelable(false)
                            .secondButton("بستن", null)
                            .type(3)
                            .show();
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                new GeneralDialog().message("خطایی پیش آمده، لطفا دوباره امتحان کنید.")
                        .cancelable(false)
                        .secondButton("بستن", null)
                        .type(3)
                        .show();
            });
        }
    };
}
