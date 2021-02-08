package ir.taxi1880.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.dialog.ChangeQueueCapacityDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.QueuesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

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

        Button btnLimitation = view.findViewById(R.id.btnLimitation);
        Button btnReduce = view.findViewById(R.id.btnReduce);

        queueTitle.setText(currentQueuesModel.getQueueTitle());
        inLineNum.setText(currentQueuesModel.getInLineNum());
        permittedNum.setText(currentQueuesModel.getPermittedNum());

        btnReduce.setOnClickListener(view1 -> new ChangeQueueCapacityDialog().show(num -> {
                getQueueInfo(currentQueuesModel.getQueueTitle() ,num , currentQueuesModel.getPermittedNum());
        }));
        btnLimitation.setOnClickListener(view1 -> new ChangeQueueCapacityDialog().show(num -> {
            getQueueInfo(currentQueuesModel.getQueueTitle(), currentQueuesModel.getInLineNum() ,num);
        }));

        return view;
    }

    private void getQueueInfo(String id, String activeMembers, String capacity) {
        RequestHelper.builder(EndPoints.GET_QUEUE)
                .addParam("id", id)
                .addParam("activeMembers", activeMembers)
                .addParam("capacity", capacity)
                .listener(queueInfoCallBack)
                .put();
    }

    RequestHelper.Callback queueInfoCallBack = new RequestHelper.Callback() {
        @Override
        public void onFailure(Runnable reCall, Exception e) {
            super.onFailure(reCall, e);
        }

        @Override
        public void onResponse(Runnable reCall, Object... args) {

        }
    };

}
