package ir.taxi1880.manager.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.dialog.GeneralDialog;
import ir.taxi1880.manager.fragment.ControlLinesFragment;
import ir.taxi1880.manager.fragment.ControlQueueFragment;
import ir.taxi1880.manager.fragment.SystemSummeryFragment;
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
import lecho.lib.hellocharts.view.ColumnChartView;

import static ir.taxi1880.manager.app.MyApplication.context;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    Unbinder unbinder;
    boolean doubleBackToExitPressedOnce = false;
    private boolean hasAxes = true;
    private boolean hasLabels = true;

    ArrayList<ChartsModel> chartsModels;
    ArrayList<ChartsModel> chartsModels2;
    ArrayList<ChartsModel> chartsModels3;

    Timer timer;

    private ColumnChartData data1;
    private ColumnChartData data2;
    private ColumnChartData data3;
    private boolean hasLabelForSelected = false;
    private int dataType = 0;

    @BindView(R.id.txtCancelTrip)
    TextView txtCancelTrip;

    @BindView(R.id.txtTripNum)
    TextView txtTripNum;

    @BindView(R.id.operatorNum)
    TextView operatorNum;

    @BindView(R.id.draw)
    DrawerLayout drawer;

    @BindView(R.id.txtVersionName)
    TextView txtVersionName;

    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;

    @BindView(R.id.navigation)
    NavigationView navigation;

    @BindView(R.id.chart1)
    ColumnChartView chart1;

    @BindView(R.id.chart2)
    ColumnChartView chart2;

    @BindView(R.id.chart3)
    ColumnChartView chart3;

    @OnClick(R.id.btnWeather)
    void OnWeather() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.accuweather.com/fa/ir/mashhad/209737/current-weather/209737"));
        MyApplication.currentActivity.startActivity(i);
    }

    @OnClick(R.id.lines)
    void onLines() {
        drawer.close();
        FragmentHelper.toFragment(MyApplication.currentActivity, new ControlLinesFragment()).setAddToBackStack(true).replace();
    }

    @OnClick(R.id.queues)
    void onQueues() {
        drawer.close();
        FragmentHelper.toFragment(MyApplication.currentActivity, new ControlQueueFragment()).setAddToBackStack(true).replace();
    }

    @OnClick(R.id.openDrawer)
    void onDrawer() {
        drawer.openDrawer(Gravity.RIGHT);
    }

    @OnClick(R.id.imgSystemSummery)
    void onSystemSummery() {
        drawer.close();
        FragmentHelper.toFragment(MyApplication.currentActivity, new SystemSummeryFragment()).setAddToBackStack(true).replace();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(getResources().getColor(R.color.alsoBlack));
            window.setStatusBarColor(getResources().getColor(R.color.alsoBlack));
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        txtVersionName.setText(StringHelper.toPersianDigits(new AppVersionHelper(context).getVerionName() + ""));

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
        chart1.setZoomEnabled(false);
        chart1.setColumnChartData(data1);

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
        chart2.setZoomEnabled(false);
        chart2.setColumnChartData(data2);

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
        chart3.setZoomEnabled(false);
        chart3.setColumnChartData(data3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentActivity = this;
        MyApplication.prefManager.setAppRun(true);
        startGetAddressTimer();
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
        unbinder.unbind();
        stopGetAddressTimer();
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
                    drawer.close();
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
        if (loader != null)
            loader.setVisibility(View.VISIBLE);
        RequestHelper.builder(EndPoints.SUMMERY_PATH)
                .listener(summeryCallBack)
                .get();
    }

    RequestHelper.Callback summeryCallBack = new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {
            MyApplication.handler.post(() -> {
                if (loader != null)
                    loader.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(args[0].toString());
                    JSONObject summeryObj = object.getJSONObject("summery");
                    String serviceCount = summeryObj.getString("serviceCount");
                    String cancelServiceCount = summeryObj.getString("cancelServiceCount");
                    String activeOperators = summeryObj.getString("activeOperators");

                    txtCancelTrip.setText(cancelServiceCount);
                    txtTripNum.setText(serviceCount);
                    operatorNum.setText(activeOperators);

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
                }
            });
        }

        @Override
        public void onFailure(Runnable reCall, Exception e) {
            MyApplication.handler.post(() -> {
                if (loader != null)
                    loader.setVisibility(View.GONE);
            });
        }
    };

    private void stopGetAddressTimer() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startGetAddressTimer() {
        try {
            if (timer != null) {
                return;
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    MyApplication.currentActivity.runOnUiThread(() -> {
                        getSummery();
                    });
                }
            }, 0, 15000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
