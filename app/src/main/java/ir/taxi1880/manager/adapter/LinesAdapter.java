package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.LinesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class LinesAdapter extends BaseAdapter {

    private ArrayList<LinesModel> linesModels;
    private Context mContext;

    public LinesAdapter(Context mContext, ArrayList<LinesModel> linesModels) {
        this.mContext = mContext;
        this.linesModels = linesModels;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_line, viewGroup, false);
            TypefaceUtil.overrideFonts(view);
        }

        LinesModel currentLinesModel = linesModels.get(i);

        TextView lineTitle = view.findViewById(R.id.lineTitle);
        SwitchButton sbNew = view.findViewById(R.id.sbNew);
        SwitchButton sbSupport = view.findViewById(R.id.sbSupport);

        lineTitle.setText(currentLinesModel.getLineTitle());
        sbNew.setChecked(currentLinesModel.getStatusNewCall());
        sbSupport.setChecked(currentLinesModel.getStatusSupportCall());

        sbNew.setOnCheckedChangeListener((compoundButton, b) -> getLineInfo(currentLinesModel.getLineTitle(), sbSupport.isChecked(), b));

        sbSupport.setOnCheckedChangeListener((compoundButton, b) -> getLineInfo(currentLinesModel.getLineTitle(), b, sbNew.isChecked()));

        return view;
    }

    private void getLineInfo(String id, boolean support, boolean newCall) {
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

        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            super.onFailure(reCall, e);
        }
    };
}
