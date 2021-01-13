package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class ControlLinesFragment extends Fragment {
    Unbinder unbinder;

    ViewFlipper vfBtnAll1880;
    ViewFlipper vfBtnAll1870;
    ViewFlipper vfBtnAll1817;
    ViewFlipper vfBtnAll950;

    ViewFlipper vfStatus1880;
    ViewFlipper vfStatus1870;
    ViewFlipper vfStatus1817;
    ViewFlipper vfStatus950;

    Button btnActiveAll1880;
    Button btnActiveAll1870;
    Button btnActiveAll1817;
    Button btnActiveAll950;

    Button btnDeactiveAll1880;
    Button btnDeactiveAll1870;
    Button btnDeactiveAll1817;
    Button btnDeactiveAll950;

    ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_lines, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        vfBtnAll1880 = view.findViewById(R.id.vfBtnAll1880);
        vfBtnAll1870 = view.findViewById(R.id.vfBtnAll1870);
        vfBtnAll1817 = view.findViewById(R.id.vfBtnAll1817);
        vfBtnAll950 = view.findViewById(R.id.vfBtnAll950);

        vfStatus1880 = view.findViewById(R.id.vfStatus1880);
        vfStatus1870 = view.findViewById(R.id.vfStatus1870);
        vfStatus1817 = view.findViewById(R.id.vfStatus1817);
        vfStatus950 = view.findViewById(R.id.vfStatus950);

        btnActiveAll1880 = view.findViewById(R.id.btnActiveAll1880);
        btnActiveAll1870 = view.findViewById(R.id.btnActiveAll1870);
        btnActiveAll1817 = view.findViewById(R.id.btnActiveAll1817);
        btnActiveAll950 = view.findViewById(R.id.btnActiveAll950);

        btnDeactiveAll1880 = view.findViewById(R.id.btnDeactiveAll1880);
        btnDeactiveAll1870 = view.findViewById(R.id.btnDeactiveAll1870);
        btnDeactiveAll1817 = view.findViewById(R.id.btnDeactiveAll1817);
        btnDeactiveAll950 = view.findViewById(R.id.btnDeactiveAll950);

        btnBack = view.findViewById(R.id.btnBack);

        btnActiveAll1880.setOnClickListener(view1 -> {
            vfBtnAll1880.setDisplayedChild(0);
            vfStatus1880.setDisplayedChild(0);
        });

        btnActiveAll1870.setOnClickListener(view1 -> {
            vfBtnAll1870.setDisplayedChild(0);
            vfStatus1870.setDisplayedChild(0);
        });

        btnActiveAll1817.setOnClickListener(view1 -> {
            vfBtnAll1817.setDisplayedChild(0);
            vfStatus1817.setDisplayedChild(0);
        });

        btnActiveAll950.setOnClickListener(view1 -> {
            vfBtnAll950.setDisplayedChild(0);
            vfStatus950.setDisplayedChild(0);
        });

        btnDeactiveAll1880.setOnClickListener(view13 -> {
            vfBtnAll1880.setDisplayedChild(1);
            vfStatus1880.setDisplayedChild(1);
        });

        btnDeactiveAll1870.setOnClickListener(view1 -> {
            vfBtnAll1870.setDisplayedChild(0);
            vfStatus1870.setDisplayedChild(0);
        });

        btnDeactiveAll1817.setOnClickListener(view1 -> {
            vfBtnAll1817.setDisplayedChild(0);
            vfStatus1817.setDisplayedChild(0);
        });

        btnDeactiveAll950.setOnClickListener(view1 -> {
            vfBtnAll950.setDisplayedChild(0);
            vfStatus950.setDisplayedChild(0);
        });


        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());

        return view;
    }
}
