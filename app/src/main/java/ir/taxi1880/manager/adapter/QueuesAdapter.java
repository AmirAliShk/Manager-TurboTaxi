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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ItemQueueBinding;
import ir.taxi1880.manager.dialog.ChangeQueueCapacityDialog;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.QueuesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

import static ir.taxi1880.manager.app.MyApplication.context;

public class QueuesAdapter extends BaseAdapter {
    ItemQueueBinding binding;
    private ArrayList<QueuesModel> queuesModels;
    LayoutInflater inflater;
    String newCapacity;
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
        QueuesModel currentQueuesModel = queuesModels.get(i);
        
        binding = ItemQueueBinding.inflate(inflater, viewGroup, false);
        TypefaceUtil.overrideFonts(binding.getRoot());

        binding.queueTitle.setText(currentQueuesModel.getName());
        binding.inLineNum.setText(currentQueuesModel.getActiveMember());
        binding.permittedNum.setText(currentQueuesModel.getCapacity());

        binding.llLimitation.setOnClickListener(view1 -> new ChangeQueueCapacityDialog().show(num -> {
            newCapacity = num;
            position = i;
            getQueueInfo(currentQueuesModel.getId(), currentQueuesModel.getActiveMember(), num);
        }, currentQueuesModel.getName(), currentQueuesModel.getCapacity()));

        return binding.getRoot();
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
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
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
                            })
                            .type(2)
                            .show();

                } catch (JSONException e) {
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
