package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.QueuesModel;

import static ir.taxi1880.manager.app.MyApplication.context;

public class QueuesAdapter extends BaseAdapter {
    private ArrayList<QueuesModel> queuesModels;
    private Context mContext;

    public QueuesAdapter(Context mContext, ArrayList<QueuesModel> queuesModels) {
        this.mContext = mContext;
        this.queuesModels = queuesModels;
    }

    @Override
    public int getCount() {
        return queuesModels.size();
    }

    @Override
    public Object getItem(int i) {
        return queuesModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_queue, viewGroup, false);
            TypefaceUtil.overrideFonts(view);
        }

        QueuesModel currentQueuesModel = queuesModels.get(i);

        TextView queueTitle = view.findViewById(R.id.queueTitle);
        TextView inLineNum = view.findViewById(R.id.inLineNum);
        TextView permittedNum = view.findViewById(R.id.permittedNum);

        queueTitle.setText(currentQueuesModel.getQueueTitle());
        inLineNum.setText(currentQueuesModel.getInLineNum());
        permittedNum.setText(currentQueuesModel.getPermittedNum());

        return view;
    }
}
