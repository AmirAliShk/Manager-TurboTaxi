package ir.taxi1880.manager.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.LinesAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.LinesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class ControlLinesFragment extends Fragment {

    ArrayList<LinesModel> arrayLinesModel;
    ListView linesList;
    LinesAdapter linesAdapter;
    ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_lines, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#2f2f2f"));
        }

        linesList = view.findViewById(R.id.linesList);
        arrayLinesModel = new ArrayList<>();

        getLines();

        arrayLinesModel.add(new LinesModel("1880", true, false));
        arrayLinesModel.add(new LinesModel("1870", true, true));
        arrayLinesModel.add(new LinesModel("1817", false, false));

        linesAdapter = new LinesAdapter(MyApplication.context, arrayLinesModel);
        linesList.setAdapter(linesAdapter);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());

        return view;
    }

private void getLines(){
    RequestHelper.builder(EndPoints.GET_LINE)
            .listener(linesCallBack)
            .get();
}

RequestHelper.Callback linesCallBack = new RequestHelper.Callback() {
    @Override
    public void onResponse(Runnable reCall, Object... args) {

    }

    @Override
    public void onFailure(Runnable reCall, Exception e) {
        super.onFailure(reCall, e);
    }
};


}
