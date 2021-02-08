package ir.taxi1880.manager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.EndPoints;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.fragment.ControlLinesFragment;
import ir.taxi1880.manager.fragment.ControlQueueFragment;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.StringHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
import ir.taxi1880.manager.okHttp.RequestHelper;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    Unbinder unbinder;
    boolean doubleBackToExitPressedOnce = false;
    private boolean hasAxes = true;
    private boolean hasLabels = true;

    Timer timer;
    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasLabelForSelected = false;
    private int dataType = 0;

    TextView txtCancelTrip;
    TextView txtTripNum;
    TextView operatorNum;
    DrawerLayout drawer;
    RelativeLayout rlBtnWeather;

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
        TypefaceUtil.overrideFonts(txtCancelTrip, MyApplication.IraSanSBold);
        TypefaceUtil.overrideFonts(txtTripNum, MyApplication.IraSanSBold);
        TypefaceUtil.overrideFonts(operatorNum, MyApplication.IraSanSBold);

        getSummery();

        rlBtnWeather = findViewById(R.id.btnWeather);
        txtCancelTrip = findViewById(R.id.txtCancelTrip);
        txtTripNum = findViewById(R.id.txtTripNum);
        operatorNum = findViewById(R.id.operatorNum);

        txtCancelTrip.setText(StringHelper.toPersianDigits("100"));
        txtTripNum.setText(StringHelper.toPersianDigits("5,000"));
        operatorNum.setText(StringHelper.toPersianDigits("154"));


        drawer = findViewById(R.id.draw);
        NavigationView navigation = findViewById(R.id.navigation);

        chart = findViewById(R.id.chart1);
        setParamsChart();
        chart = findViewById(R.id.chart2);
        setParamsChart();
        chart = findViewById(R.id.chart3);
        setParamsChart();

        timer=new Timer();

        RelativeLayout openDrawer = findViewById(R.id.openDrawer);

        openDrawer.setOnClickListener(view12 -> {
            drawer.openDrawer(Gravity.RIGHT);
        });

        ImageView lines = findViewById(R.id.lines);

        lines.setOnClickListener(view1 -> {
            drawer.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ControlLinesFragment()).setAddToBackStack(true).replace();
        });

        ImageView queues = findViewById(R.id.queues);

        queues.setOnClickListener(view1 -> {
            drawer.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ControlQueueFragment()).setAddToBackStack(true).replace();
        });

        rlBtnWeather.setOnClickListener(view13 -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.accuweather.com/fa/ir/mashhad/209737/current-weather/209737"));
            MyApplication.currentActivity.startActivity(i);

        });

    }

    public void setParamsChart() {

        int numSubcolumns = 1;
        int numColumns = 8;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<>();

        int[] value = {10000, 3500, 2000, 1500, 1500, 1000, 500, 100};
        int[] color = {R.color.redChart, R.color.orangeChart, R.color.yellowChart, R.color.deepGreenChart, R.color.greenChart, R.color.greenChart, R.color.deepClueChart, R.color.blueChart, R.color.lightBlueChart};
        String[] city = {"مشهد", "خواف", "تربت جام", "تربت حیدریه", "فریمان", "گناباد", "نیشابور", "کاشمر", "تایباد"};

        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(value[i], MyApplication.currentActivity.getResources().getColor(color[i])));

            axisValues.add(new AxisValue(i).setLabel(city[i]));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis();
            axisX.setHasLines(false);
            axisY.setHasLines(false);

            data.setAxisXBottom(new Axis(axisValues));
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setZoomEnabled(false);
        chart.setColumnChartData(data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.currentActivity = this;
        MyApplication.prefManager.setAppRun(true);

        timer.schedule(timerTask, 2000,15000);
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

    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            getSummery();
        }
    };

    private void getSummery() {
        RequestHelper.builder(EndPoints.SUMMERY)
                .listener(summeryCallBack)
                .get();
    }

    RequestHelper.Callback summeryCallBack= new RequestHelper.Callback() {
        @Override
        public void onResponse(Runnable reCall, Object... args) {

        }
    };
}
