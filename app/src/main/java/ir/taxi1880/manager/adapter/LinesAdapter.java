package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.LinesModel;

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
//        CheckBox cbNewCall = view.findViewById(R.id.cbNewCall);
//        CheckBox cbSupportCall = view.findViewById(R.id.cbSupportCall);

        lineTitle.setText(currentLinesModel.getLineTitle());
//        cbNewCall.setChecked(currentLinesModel.getStatusNewCall());
//        cbSupportCall.setChecked(currentLinesModel.getStatusSupportCall());

        return view;
    }
}
