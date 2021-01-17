package ir.taxi1880.manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.ChangeLineCapacityDialog;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class ControlQueueFragment extends Fragment {
    ImageView btnBack;
    Button btnReduce1880;
    Button btnReduce1870;
    Button btnReduce1817;
    Button btnReduce950;

    Button btnLimitation1880;
    Button btnLimitation1870;
    Button btnLimitation1817;
    Button btnLimitation950;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control_queue, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(view);

        TextView txtReduce1880 = view.findViewById(R.id.txtReduce1880);
        TextView txtReduce1870 = view.findViewById(R.id.txtReduce1870);
        TextView txtReduce1817 = view.findViewById(R.id.txtReduce1817);
        TextView txtReduce950 = view.findViewById(R.id.txtReduce950);

        TextView txtLimitation1880 = view.findViewById(R.id.txtLimitation1880);
        TextView txtLimitation1870 = view.findViewById(R.id.txtLimitation1870);
        TextView txtLimitation1817 = view.findViewById(R.id.txtLimitation1817);
        TextView txtLimitation950 = view.findViewById(R.id.txtLimitation950);

        btnReduce1880 = view.findViewById(R.id.btnReduce1880);
        btnReduce1870 = view.findViewById(R.id.btnReduce1870);
        btnReduce1817 = view.findViewById(R.id.btnReduce1817);
        btnReduce950 = view.findViewById(R.id.btnReduce950);

        btnLimitation1880 = view.findViewById(R.id.btnLimitation1880);
        btnLimitation1870 = view.findViewById(R.id.btnLimitation1870);
        btnLimitation1817 = view.findViewById(R.id.btnLimitation1817);
        btnLimitation950 = view.findViewById(R.id.btnLimitation950);

        btnReduce1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtReduce1880.setText(num);
                    }
                });
            }
        });
        btnReduce1870.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtReduce1870.setText(num);
                    }
                });
            }
        });
        btnReduce1817.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtReduce1817.setText(num);
                    }
                });
            }
        });
        btnReduce950.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtReduce950.setText(num);
                    }
                });
            }
        });


        btnLimitation1880.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtLimitation1880.setText(num);
                    }
                });
            }
        });
        btnLimitation1870.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtLimitation1870.setText(num);
                    }
                });
            }
        });
        btnLimitation1817.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtLimitation1817.setText(num);
                    }
                });
            }
        });
        btnLimitation950.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangeLineCapacityDialog().show(new ChangeLineCapacityDialog.Listener() {
                    @Override
                    public void num(String num) {
                        txtLimitation950.setText(num);
                    }
                });
            }
        });

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view12 -> MyApplication.currentActivity.onBackPressed());





        return view;
    }
}
