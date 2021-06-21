package ir.taxi1880.manager.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.adapter.SpinnerAdapter;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.DialogTripCostDialogBinding;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CarTypeModel;

public class AddTripCostDialog {

    Dialog dialog;
    DialogTripCostDialogBinding binding;

    int carType;

    public void show() {
        if (MyApplication.currentActivity == null || MyApplication.currentActivity.isFinishing())
            return;

        dialog = new Dialog(MyApplication.currentActivity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.ExpandAnimation;
        binding = DialogTripCostDialogBinding.inflate(LayoutInflater.from(MyApplication.context));
        dialog.setContentView(binding.getRoot());
        TypefaceUtil.overrideFonts(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.windowAnimations = R.style.ExpandAnimation;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes(wlp);
        dialog.setCancelable(true);
        initCarSpinner();

        binding.imgCancelDialog.setOnClickListener(view -> {
            dialog.dismiss();
        });
        binding.blrview.setOnClickListener(view -> {
            dialog.dismiss();
        });
        binding.llCarType.setOnClickListener(view -> binding.spCarType.performClick());
        binding.btnSubmit.setOnClickListener(view -> {
            String wayName = binding.edtNameWayRTCDialog.getText().toString().trim();
            String origin = binding.originRTCDialog.getText().toString().trim();
            String dest = binding.destRTCDialog.getText().toString().trim();

            if (wayName.isEmpty()) {
                MyApplication.Toast("مسیر را کامل کنید", 2);
                return;
            }

            if (origin.isEmpty()) {
                MyApplication.Toast("مبدا را کامل کنید", 2);
                return;

            }

            if (dest.isEmpty()) {
                MyApplication.Toast("مقصد را کامل کنید", 2);
                return;

            }

            if (carType == 0) {
                MyApplication.Toast("مقصد را کامل کنیدdgfcgcg", 2);
                return;

            }


            MyApplication.Toast("با موفقیت ثبت گردید", 2);
            dialog.dismiss();
        });


        dialog.show();
    }

    private void initCarSpinner() {
        ArrayList<CarTypeModel> carTypeModels = new ArrayList<>();
        ArrayList<String> carTypeList = new ArrayList<>();
        try {
            JSONArray carTypeJArray = new JSONArray(MyApplication.prefManager.getCarType());
            for (int i = 0; i < carTypeJArray.length(); i++) {
                JSONObject object = carTypeJArray.getJSONObject(i);

                CarTypeModel carTypeModel = new CarTypeModel();
                carTypeModel.setId(object.getInt("id"));
                carTypeModel.setCarName(object.getString("name"));
                carTypeModels.add(carTypeModel);

                carTypeList.add(i, object.getString("name"));
            }

            binding.spCarType.setAdapter(new SpinnerAdapter(MyApplication.context, R.layout.item_spinner, carTypeList));
            binding.spCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position == 0) {
                        carType = 0;
                        return;
                    }
                    carType = carTypeModels.get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void addTripCostType()
//    {
//        RequestHelper.builder(EndPoints.TYPE_OF_WAY)
//                .addParam()
//                .addParam()
//                .listener()
//                .post();
//    }


}
