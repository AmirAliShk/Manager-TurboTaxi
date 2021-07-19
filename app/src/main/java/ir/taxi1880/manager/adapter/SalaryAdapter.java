package ir.taxi1880.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.SalaryModel;

import static ir.taxi1880.manager.app.MyApplication.context;

public class SalaryAdapter extends BaseAdapter {

    ArrayList<SalaryModel> salaryModels;

    LayoutInflater inflater;

    SalaryModel salaryModel;

    public SalaryAdapter(ArrayList<SalaryModel> salaryModels) {
        this.salaryModels = salaryModels;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return salaryModels.size();
    }

    @Override
    public Object getItem(int i) {
        return salaryModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView = view;

        salaryModel = salaryModels.get(i);

        if (myView == null) {
            myView = inflater.inflate(R.layout.item_salary, viewGroup, false);
            TypefaceUtil.overrideFonts(myView);
        }


        return myView;
    }
}
