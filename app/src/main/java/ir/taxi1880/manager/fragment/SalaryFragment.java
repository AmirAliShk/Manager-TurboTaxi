package ir.taxi1880.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.databinding.FragmentRecordTripCostBinding;
import ir.taxi1880.manager.databinding.FragmentSalaryBinding;
import ir.taxi1880.manager.helper.TypefaceUtil;

public class SalaryFragment extends Fragment {

    FragmentSalaryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSalaryBinding.inflate(getLayoutInflater());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        TypefaceUtil.overrideFonts(binding.getRoot());


        return binding.getRoot();
    }
}
