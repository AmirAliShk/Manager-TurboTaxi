package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.QueuesAdapter;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.QueuesModel;

public class ControlQueueFragment extends Fragment {
    ImageView btnBack;
    ArrayList<QueuesModel> arrayQueuesModel;
    ListView queuesList;
    QueuesAdapter queuesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_queues, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);

        queuesList = view.findViewById(R.id.queuesList);
        arrayQueuesModel = new ArrayList<>();

        arrayQueuesModel.add(new QueuesModel("1880", "15", "20"));
        arrayQueuesModel.add(new QueuesModel("1870", "32", "12"));
        arrayQueuesModel.add(new QueuesModel("1817", "18", "13"));

        queuesAdapter = new QueuesAdapter(MyApplication.context, arrayQueuesModel);
        queuesList.setAdapter(queuesAdapter);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());

        return view;
    }
}
