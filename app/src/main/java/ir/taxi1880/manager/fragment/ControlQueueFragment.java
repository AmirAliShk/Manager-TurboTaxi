package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.ChangeLineCapacityDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class ControlQueueFragment extends Fragment {
    ImageView btnBack;
    Button btnReduce1880;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_queue, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);

        btnReduce1880 = view.findViewById(R.id.btnReduce1880);
        btnReduce1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show();
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());

        return view;
    }
}
