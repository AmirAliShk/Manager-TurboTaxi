package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.model.LinesModel;

import static ir.taxi1880.manager.app.MyApplication.context;

public class LinesAdapter extends BaseAdapter {

    private ArrayList<LinesModel> linesModels;
    private LayoutInflater layoutInflater;

    public LinesAdapter(ArrayList<LinesModel> linesModels, Context context) {
        this.linesModels = linesModels;
        this.layoutInflater=LayoutInflater.from(context);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_lines, viewGroup, false);
        }

        LinesModel currentLinesModel = linesModels.get(i);

        TextView lineTitle = view.findViewById(R.id.lineTitle);
        TextView address = view.findViewById(R.id.address);


        lineTitle.setText(currentLinesModel.lineTitle());
        address.setText(currentLinesModel.getAddress());

        imageView.setImageResource(currentWebViewModel.getImageView());


        return view;
    }
}
