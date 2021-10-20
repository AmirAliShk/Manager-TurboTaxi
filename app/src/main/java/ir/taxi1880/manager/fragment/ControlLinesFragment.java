package ir.taxi1880.manager.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.adapter.LinesAdapter;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.FragmentControlLinesBinding;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.LinesModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class ControlLinesFragment extends Fragment {

    FragmentControlLinesBinding binding;
    ArrayList<LinesModel> arrayLinesModel;
    LinesAdapter linesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentControlLinesBinding.inflate(inflater, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#2f2f2f"));
        }

        arrayLinesModel = new ArrayList<>();

        getLines();

        binding.btnBack.setOnClickListener(view -> MyApplication.currentActivity.onBackPressed());

        return binding.getRoot();
    }

    private void getLines() {
        RequestHelper.builder(EndPoints.GET_LINE)
                .listener(linesCallBack)
                .get();
    }

    RequestHelper.Callback linesCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                try {
                    JSONArray linesArr = new JSONArray(args[0].toString());
                    for (int i = 0; i < linesArr.length(); i++) {
                        JSONObject linesObj = linesArr.getJSONObject(i);
                        LinesModel linesModel = new LinesModel();
                        linesModel.setId(linesObj.getInt("id"));
                        linesModel.setName(linesObj.getString("name"));
                        linesModel.setNumber(linesObj.getString("number"));
                        linesModel.setNew(linesObj.getInt("new") == 1);
                        linesModel.setSupport(linesObj.getInt("support") == 1);
                        arrayLinesModel.add(linesModel);
                    }

                    linesAdapter = new LinesAdapter(arrayLinesModel);
                    binding.linesList.setAdapter(linesAdapter);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
