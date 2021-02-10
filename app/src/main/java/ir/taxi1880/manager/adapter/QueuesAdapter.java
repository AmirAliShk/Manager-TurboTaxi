package ir.taxi1880.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.ChangeQueueCapacityDialog;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.QueuesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class QueuesAdapter extends BaseAdapter {

    private ArrayList<QueuesModel> queuesModels;
    LayoutInflater inflater;
    String newCapacity;
    TextView permittedNum;
    int position;

    public QueuesAdapter(ArrayList<QueuesModel> queuesModels) {
        this.queuesModels = queuesModels;
        this.inflater = LayoutInflater.from(context);
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
        View myView = view;

        QueuesModel currentQueuesModel = queuesModels.get(i);

        if (myView == null) {
            myView = inflater.inflate(R.layout.item_queue, viewGroup, false);
            TypefaceUtil.overrideFonts(myView);
        }

        TextView queueTitle = myView.findViewById(R.id.queueTitle);
        TextView inLineNum = myView.findViewById(R.id.inLineNum);
        permittedNum = myView.findViewById(R.id.permittedNum);

        LinearLayout llLimitation = myView.findViewById(R.id.llLimitation);

        queueTitle.setText(currentQueuesModel.getName());
        inLineNum.setText(currentQueuesModel.getActiveMember());
        permittedNum.setText(currentQueuesModel.getCapacity());


        llLimitation.setOnClickListener(view1 -> new ChangeQueueCapacityDialog().show(num -> {
            newCapacity = num;
            position = i;
            getQueueInfo(currentQueuesModel.getName(), currentQueuesModel.getActiveMember(), num);
        }, currentQueuesModel.getName()));

        return myView;
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
            MyApplication.handler.post(new Runnable() {
                @Override
                public void run() {
//                    {"status":true,"message":"با موفقیت بروزرسانی شد"}
                    try {
                        JSONObject object = new JSONObject(args[0].toString());

                        String message = object.getString("message");
                        boolean status = object.getBoolean("status");


                        new GeneralDialog()
                                .message(message)
                                .cancelable(false)
                                .firstButton("باشه", () -> {
                                    queuesModels.get(position).setCapacity(newCapacity);
                                    notifyDataSetChanged();

//                                    if (status) {
//                                        permittedNum.setText(newCapacity+ " changed ");
//                                    }
                                })
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

}
