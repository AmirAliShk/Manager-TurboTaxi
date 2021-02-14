package ir.taxi1880.manager.fragment;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.LinesAdapter;
import ir.taxi1880.manager.adapter.QueuesAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.LinesModel;
import ir.taxi1880.manager.model.QueuesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class ControlQueueFragment extends Fragment {
    ArrayList<QueuesModel> arrayQueuesModel;
    QueuesAdapter queuesAdapter;
    Unbinder unbinder;

    @BindView(R.id.queuesList)
    ListView queuesList;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_queues, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);
        unbinder = ButterKnife.bind(this, view);

        arrayQueuesModel = new ArrayList<>();

        getQueue();

        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());

        return view;
    }

    private void getQueue() {
        RequestHelper.builder(EndPoints.GET_QUEUE)
                .listener(queueCallBack)
                .get();
    }

    RequestHelper.Callback queueCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONArray queuesArr = new JSONArray(args[0].toString());
                    for (int i = 0; i < queuesArr.length(); i++) {
                        JSONObject queuesObj = queuesArr.getJSONObject(i);
                        QueuesModel queuesModel = new QueuesModel();
                        queuesModel.setName(queuesObj.getString("name"));
                        queuesModel.setQueueCode(queuesObj.getString("QueueCode"));
                        queuesModel.setId(queuesObj.getString("id"));
                        queuesModel.setCapacity(queuesObj.getString("capacity"));
                        queuesModel.setActiveMember(queuesObj.getString("activeMembers"));
                        arrayQueuesModel.add(queuesModel);
                    }

                    queuesAdapter = new QueuesAdapter(arrayQueuesModel);
                    queuesList.setAdapter(queuesAdapter);

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
