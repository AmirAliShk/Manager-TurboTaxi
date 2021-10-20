package ir.taxi1880.manager.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.databinding.ActivityMainBinding;
import ir.taxi1880.manager.fragment.ControlLinesFragment;
import ir.taxi1880.manager.fragment.ControlQueueFragment;
import ir.taxi1880.manager.fragment.RateFragment;
import ir.taxi1880.manager.fragment.SalaryFragment;
import ir.taxi1880.manager.fragment.SystemSummeryFragment;
import ir.taxi1880.manager.fragment.TripCostTestFragment;
import ir.taxi1880.manager.helper.AppVersionHelper;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.model.ChartsModel;
import ir.taxi1880.manager.okHttp.RequestHelper;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;

import static ir.taxi1880.manager.app.MyApplication.context;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;
    private boolean hasAxes = true;
    private boolean hasLabels = true;
    ArrayList<ChartsModel> chartsModels;
    ArrayList<ChartsModel> chartsModels2;
    ArrayList<ChartsModel> chartsModels3;
    private ColumnChartData data1;
    private ColumnChartData data2;
    private ColumnChartData data3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(getResources().getColor(R.color.alsoBlack));
            window.setStatusBarColor(getResources().getColor(R.color.alsoBlack));
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        TypefaceUtil.overrideFonts(binding.getRoot());

        getSummery();

        binding.txtVersionName.setText(StringHelper.toPersianDigits(new AppVersionHelper(context).getVerionName() + ""));

        binding.btnWeather.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.accuweather.com/fa/ir/mashhad/209737/current-weather/209737"));
            MyApplication.currentActivity.startActivity(i);
        });

        binding.lines.setOnClickListener(view -> {
            binding.draw.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ControlLinesFragment()).setAddToBackStack(true).replace();
        });

        binding.imgRate.setOnClickListener(view -> {
            binding.draw.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new RateFragment()).setAddToBackStack(true).replace();
        });

        binding.queues.setOnClickListener(view -> {
            binding.draw.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ControlQueueFragment()).setAddToBackStack(true).replace();
        });

        binding.imgTripSubmit.setOnClickListener(view -> {
            binding.draw.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new TripCostTestFragment()).setAddToBackStack(true).replace();
        });

        binding.openDrawer.setOnClickListener(view -> {
            binding.draw.openDrawer(Gravity.RIGHT);
        });

        binding.imgSystemSummery.setOnClickListener(view -> {
            binding.draw.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new SystemSummeryFragment()).setAddToBackStack(true).replace();
        });

        binding.imgSalary.setOnClickListener(view -> {
            binding.draw.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new SalaryFragment()).setAddToBackStack(true).replace();
        });

        binding.imgRefresh.setOnClickListener(view -> {
            getSummery();
        });
    }

    public void setParamsChart1(ArrayList<ChartsModel> tripsModels) {

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<>();

        int[] color = {R.color.redChart, R.color.orangeChart, R.color.yellowChart, R.color.deepGreenChart, R.color.greenChart, R.color.greenChart, R.color.deepClueChart, R.color.blueChart, R.color.lightBlueChart};

        for (int i = 0; i < tripsModels.size(); ++i) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(tripsModels.get(i).getServiceCount(), MyApplication.currentActivity.getResources().getColor(color[i])));
            axisValues.add(new AxisValue(i).setLabel(tripsModels.get(i).getCityName()));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            columns.add(column);
        }

        data1 = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis();
            axisX.setHasLines(false);
            axisY.setHasLines(false);

            data1.setAxisXBottom(new Axis(axisValues));
        } else {
            data1.setAxisXBottom(null);
            data1.setAxisYLeft(null);
        }
        binding.chart1.setZoomEnabled(false);
        binding.chart1.setColumnChartData(data1);

    }

    public void setParamsChart2(ArrayList<ChartsModel> tripsModels) {

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<>();

        int[] color = {R.color.redChart, R.color.orangeChart, R.color.yellowChart, R.color.deepGreenChart, R.color.greenChart, R.color.greenChart, R.color.deepClueChart, R.color.blueChart, R.color.lightBlueChart};

        for (int i = 0; i < tripsModels.size(); ++i) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(tripsModels.get(i).getServiceCount(), MyApplication.currentActivity.getResources().getColor(color[i])));

            axisValues.add(new AxisValue(i).setLabel(tripsModels.get(i).getCityName()));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            columns.add(column);
        }

        data2 = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis();
            axisX.setHasLines(false);
            axisY.setHasLines(false);

            data2.setAxisXBottom(new Axis(axisValues));
        } else {
            data2.setAxisXBottom(null);
            data2.setAxisYLeft(null);
        }
        binding.chart2.setZoomEnabled(false);
        binding.chart2.setColumnChartData(data2);

    }

    public void setParamsChart3(ArrayList<ChartsModel> tripsModels) {

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<>();

        int[] color = {R.color.redChart, R.color.orangeChart, R.color.yellowChart, R.color.deepGreenChart, R.color.greenChart, R.color.greenChart, R.color.deepClueChart, R.color.blueChart, R.color.lightBlueChart};

        for (int i = 0; i < tripsModels.size(); ++i) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(tripsModels.get(i).getServiceCount(), MyApplication.currentActivity.getResources().getColor(color[i])));

            axisValues.add(new AxisValue(i).setLabel(tripsModels.get(i).getCityName()));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            columns.add(column);
        }

        data3 = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis();
            axisX.setHasLines(false);
            axisY.setHasLines(false);

            data3.setAxisXBottom(new Axis(axisValues));
        } else {
            data3.setAxisXBottom(null);
            data3.setAxisYLeft(null);
        }
        binding.chart3.setZoomEnabled(false);
        binding.chart3.setColumnChartData(data3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentActivity = this;
        MyApplication.prefManager.setAppRun(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.prefManager.setAppRun(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.currentActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            if (getFragmentManager().getBackStackEntryCount() > 0 || getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                if (doubleBackToExitPressedOnce) {
                    MyApplication.currentActivity.finish();
                } else {
                    binding.draw.close();
                    this.doubleBackToExitPressedOnce = true;
                    MyApplication.Toast(getString(R.string.txt_please_for_exit_reenter_back), Toast.LENGTH_SHORT);
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSummery() {
        if (binding.vfLoader != null)
            binding.vfLoader.setDisplayedChild(1);
        RequestHelper.builder(EndPoints.MANAGER_PATH)
                .listener(summeryCallBack)
                .get();
    }

    RequestHelper.Callback summeryCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                if (binding.vfLoader != null)
                    binding.vfLoader.setDisplayedChild(0);
                try {
                    JSONObject object = new JSONObject(args[0].toString());
                    JSONObject summeryObj = object.getJSONObject("summery");
                    String serviceCount = summeryObj.getString("serviceCount");
                    String cancelServiceCount = summeryObj.getString("cancelServiceCount");
                    String activeOperators = summeryObj.getString("activeOperators");

                    binding.txtCancelTrip.setText(cancelServiceCount);
                    binding.txtTripNum.setText(serviceCount);
                    binding.operatorNum.setText(activeOperators);

                    chartsModels = new ArrayList<>();
                    chartsModels2 = new ArrayList<>();
                    chartsModels3 = new ArrayList<>();

                    JSONArray todayTripsArr = object.getJSONArray("todayTrips");
                    for (int i = 0; i < todayTripsArr.length(); i++) {
                        ChartsModel model = new ChartsModel();
                        JSONObject tripObj = todayTripsArr.getJSONObject(i);
                        model.setCityName(tripObj.getString("CityName"));
                        model.setServiceCount(tripObj.getInt("serviceCount"));
                        chartsModels.add(model);
                    }
                    setParamsChart1(chartsModels);

                    JSONArray waitingTripsArr = object.getJSONArray("waitingTrips");
                    for (int i = 0; i < waitingTripsArr.length(); i++) {
                        ChartsModel model = new ChartsModel();
                        JSONObject waitingTripsObj = waitingTripsArr.getJSONObject(i);
                        model.setServiceCount(waitingTripsObj.getInt("timedServiceCount"));
                        model.setCityName(waitingTripsObj.getString("CityName"));
                        chartsModels2.add(model);
                    }
                    setParamsChart2(chartsModels2);

                    JSONArray activeDriversArr = object.getJSONArray("activeDrivers");
                    for (int i = 0; i < activeDriversArr.length(); i++) {
                        ChartsModel model = new ChartsModel();
                        JSONObject driverObj = activeDriversArr.getJSONObject(i);
                        model.setServiceCount(driverObj.getInt("driverCount"));
                        model.setCityName(driverObj.getString("CityName"));
                        chartsModels3.add(model);
                    }
                    setParamsChart3(chartsModels3);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (binding.vfLoader != null)
                        binding.vfLoader.setDisplayedChild(0);
                    MyApplication.Toast("خطایی پیش آمده، لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT);
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (binding.vfLoader != null)
                    binding.vfLoader.setDisplayedChild(0);
                MyApplication.Toast("خطایی پیش آمده، لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT);
            });
        }
    };

}
