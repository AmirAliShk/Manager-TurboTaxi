package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.util.Log;
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

import static ir.taxi1880.manager.fragment.LoginFragment.TAG;

public class ControlLinesFragment extends Fragment {

    ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_lines, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);


        ViewFlipper vfStatus1880 = view.findViewById(R.id.vfStatus1880);
        ViewFlipper vfStatus1870 = view.findViewById(R.id.vfStatus1870);
        ViewFlipper vfStatus1817 = view.findViewById(R.id.vfStatus1817);
        ViewFlipper vfStatus950 = view.findViewById(R.id.vfStatus950);
        ViewFlipper vfBtnNew1880 = view.findViewById(R.id.vfBtnNew1880);
        ViewFlipper vfBtnNew1870 = view.findViewById(R.id.vfBtnNew1870);
        ViewFlipper vfBtnNew1817 = view.findViewById(R.id.vfBtnNew1817);
        ViewFlipper vfBtnNew950 = view.findViewById(R.id.vfBtnNew950);
        ViewFlipper vfBtnAll1880 = view.findViewById(R.id.vfBtnAll1880);
        ViewFlipper vfBtnAll1870 = view.findViewById(R.id.vfBtnAll1870);
        ViewFlipper vfBtnAll1817 = view.findViewById(R.id.vfBtnAll1817);
        ViewFlipper vfBtnAll950 = view.findViewById(R.id.vfBtnAll950);
        Button btnDeactiveAll1880 = view.findViewById(R.id.btnDeactiveAll1880);
        Button btnDeactiveAll1870 = view.findViewById(R.id.btnDeactiveAll1870);
        Button btnDeactiveAll1817 = view.findViewById(R.id.btnDeactiveAll1817);
        Button btnDeactiveAll950 = view.findViewById(R.id.btnDeactiveAll950);
        Button btnActiveAll1880 = view.findViewById(R.id.btnActiveAll1880);
        Button btnActiveAll1870 = view.findViewById(R.id.btnActiveAll1870);
        Button btnActiveAll1817 = view.findViewById(R.id.btnActiveAll1817);
        Button btnActiveAll950 = view.findViewById(R.id.btnActiveAll950);
        Button btnDeactiveNew1880 = view.findViewById(R.id.btnDeactiveNew1880);
        Button btnDeactiveNew1870 = view.findViewById(R.id.btnDeactiveNew1870);
        Button btnDeactiveNew1817 = view.findViewById(R.id.btnDeactiveNew1817);
        Button btnDeactiveNew950 = view.findViewById(R.id.btnDeactiveNew950);
        Button btnActiveNew1880 = view.findViewById(R.id.btnActiveNew1880);
        Button btnActiveNew1870 = view.findViewById(R.id.btnActiveNew1870);
        Button btnActiveNew1817 = view.findViewById(R.id.btnActiveNew1817);
        Button btnActiveNew950 = view.findViewById(R.id.btnActiveNew950);


        btnDeactiveAll1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive all 1880");
                vfStatus1880.setDisplayedChild(1);
                vfBtnAll1880.setDisplayedChild(1);
                btnDeactiveNew1880.setEnabled(false);
                btnActiveNew1880.setEnabled(false);

            }
        });
        btnDeactiveAll1870.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive all 1880");
                vfStatus1870.setDisplayedChild(1);
                vfBtnAll1870.setDisplayedChild(1);
                btnDeactiveNew1870.setEnabled(false);
                btnActiveNew1870.setEnabled(false);

            }
        });
        btnDeactiveAll1817.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive all 1880");
                vfStatus1817.setDisplayedChild(1);
                vfBtnAll1817.setDisplayedChild(1);
                btnDeactiveNew1817.setEnabled(false);
                btnActiveNew1817.setEnabled(false);

            }
        });
        btnDeactiveAll950.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive all 1880");
                vfStatus950.setDisplayedChild(1);
                vfBtnAll950.setDisplayedChild(1);
                btnDeactiveNew950.setEnabled(false);
                btnActiveNew950.setEnabled(false);

            }
        });


        btnActiveAll1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vfStatus1880.setDisplayedChild(0);
                vfBtnAll1880.setDisplayedChild(0);
                btnDeactiveNew1880.setEnabled(true);
                btnActiveNew1880.setEnabled(true);
            }
        });
        btnActiveAll1870.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vfStatus1870.setDisplayedChild(0);
                vfBtnAll1870.setDisplayedChild(0);
                btnDeactiveNew1870.setEnabled(true);
                btnActiveNew1870.setEnabled(true);
            }
        });
        btnActiveAll1817.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vfStatus1817.setDisplayedChild(0);
                vfBtnAll1817.setDisplayedChild(0);
                btnDeactiveNew1817.setEnabled(true);
                btnActiveNew1817.setEnabled(true);
            }
        });
        btnActiveAll950.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vfStatus950.setDisplayedChild(0);
                vfBtnAll950.setDisplayedChild(0);
                btnDeactiveNew950.setEnabled(true);
                btnActiveNew950.setEnabled(true);
            }
        });


        btnDeactiveNew1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive new 1880");
                vfStatus1880.setDisplayedChild(2);
                vfBtnNew1880.setDisplayedChild(1);
            }
        });
        btnDeactiveNew1870.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive new 1880");
                vfStatus1870.setDisplayedChild(2);
                vfBtnNew1870.setDisplayedChild(1);
            }
        });
        btnDeactiveNew1817.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive new 1880");
                vfStatus1817.setDisplayedChild(2);
                vfBtnNew1817.setDisplayedChild(1);
            }
        });
        btnDeactiveNew950.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "deactive new 1880");
                vfStatus950.setDisplayedChild(2);
                vfBtnNew950.setDisplayedChild(1);
            }
        });


        btnActiveNew1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click active new 1880");
                vfStatus1880.setDisplayedChild(0);
                vfBtnNew1880.setDisplayedChild(0);
            }
        });
        btnActiveNew1870.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click active new 1880");
                vfStatus1870.setDisplayedChild(0);
                vfBtnNew1870.setDisplayedChild(0);
            }
        });
        btnActiveNew1817.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click active new 1880");
                vfStatus1817.setDisplayedChild(0);
                vfBtnNew1817.setDisplayedChild(0);
            }
        });
        btnActiveNew950.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "click active new 1880");
                vfStatus950.setDisplayedChild(0);
                vfBtnNew950.setDisplayedChild(0);
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());

        return view;
    }
}
