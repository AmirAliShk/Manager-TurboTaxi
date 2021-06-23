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
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.DialogTripCostDialogBinding;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.CarTypeModel;
import ir.taxi1880.manager.okHttp.RequestHelper;

public class AddTripCostDialog {

    Dialog dialog;
    DialogTripCostDialogBinding binding;
    Listener listener;

    int carType;

    public interface Listener {
        void onGetData(boolean isCreated);
    }

    public void show(Listener listener) {
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

        this.listener = listener;
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
                MyApplication.Toast("نوع ماشین را انتخاب کنید", 2);
                return;

            }

            RequestHelper.builder(EndPoints.TRIP_COST_TEST)
                    .addParam("fromStation",Integer.parseInt(origin))
                    .addParam("toStation",Integer.parseInt(dest))
                    .addParam("carType",carType)
                    .addParam("name", wayName)
                    .listener(addListener)
                    .post();
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


    RequestHelper.Callback addListener = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(()->
            {
                try {
                    JSONObject addObj = new JSONObject(args[0].toString());
                    boolean success = addObj.getBoolean("success");
                    String message = addObj.getString("message");

                    if (success)
                    {
                        dialog.dismiss();
                        listener.onGetData(true);
                        new GeneralDialog()
                                .type(2)
                                .message(message)
                                .secondButton("بستن", null)
                                .show();

                    }
                    else
                    {
                        new GeneralDialog()
                                .type(3)
                                .message(message)
                                .secondButton("بستن", null)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            });

        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            super.onFailure(reCall, e);
//            MyApplication.handler.post()
        }
    };
}
