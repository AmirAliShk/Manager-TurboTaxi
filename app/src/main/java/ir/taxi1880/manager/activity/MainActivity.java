package ir.taxi1880.manager.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.fragment.ControlLinesFragment;
import ir.taxi1880.manager.fragment.ControlQueueFragment;
import ir.taxi1880.manager.helper.FragmentHelper;
import ir.taxi1880.manager.helper.TypefaceUtil;
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

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasLabelForSelected = false;
    private int dataType = 0;

    DrawerLayout drawer;

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

        drawer = findViewById(R.id.draw);
        NavigationView navigation = findViewById(R.id.navigation);

        chart = findViewById(R.id.chart1);
        setParamsChart();
        chart = findViewById(R.id.chart2);
        setParamsChart();
        chart = findViewById(R.id.chart3);
        setParamsChart();


        ImageView openDrawer = findViewById(R.id.openDrawer);

        openDrawer.setOnClickListener(view12 -> {
            drawer.openDrawer(Gravity.RIGHT);
        });

        LinearLayout llLines = findViewById(R.id.llLines);

        llLines.setOnClickListener(view1 -> {
            drawer.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ControlLinesFragment()).setAddToBackStack(true).replace();
        });

        LinearLayout llQueue = findViewById(R.id.llQueue);

        llQueue.setOnClickListener(view1 -> {
            drawer.close();
            FragmentHelper.toFragment(MyApplication.currentActivity, new ControlQueueFragment()).setAddToBackStack(true).replace();
        });
    }


    public void setParamsChart() {

        int numSubcolumns = 1;
        int numColumns = 8;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList<>();

        int[] value = {10000 , 3500 , 2000 , 1500 , 1500 , 1000 , 500 , 100};
        int[] color = {R.color.redChart , R.color.orangeChart ,  R.color.yellowChart ,  R.color.deepGreenChart ,  R.color.greenChart ,  R.color.greenChart ,  R.color.deepClueChart ,  R.color.blueChart , R.color.lightBlueChart};
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
                    this.doubleBackToExitPressedOnce = true;
                    MyApplication.Toast(getString(R.string.txt_please_for_exit_reenter_back), Toast.LENGTH_SHORT);
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
