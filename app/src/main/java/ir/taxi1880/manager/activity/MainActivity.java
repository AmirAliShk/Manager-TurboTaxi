package ir.taxi1880.manager.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.taxi1880.manager.R;
import ir.taxi1880.manager.app.MyApplication;
import ir.taxi1880.manager.helper.TypefaceUtil;
import lecho.lib.hellocharts.model.Axis;
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
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasLabelForSelected = false;
    private int dataType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryLighter));
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        unbinder = ButterKnife.bind(this, view);
        TypefaceUtil.overrideFonts(view);

        DrawerLayout drawer = findViewById(R.id.draw); //TODO
        NavigationView navigation = findViewById(R.id.navigation); //TODO

        chart = (ColumnChartView) findViewById(R.id.chart1);
        setParamsChart();
        chart = (ColumnChartView) findViewById(R.id.chart2);
        setParamsChart();
        chart = (ColumnChartView) findViewById(R.id.chart3);
        setParamsChart();


//
//        NavigationView navigationView = findViewById(R.id.nav_view);


        ImageView add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

    }


    public void setParamsChart() {

        int numSubcolumns = 1;
        int numColumns = 7;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisX.setTextColor(Color.BLACK);
            axisY.setTextColor(Color.BLACK);
            axisX.setName("car");
            axisX.setTextSize(16);
            chart.setZoomEnabled(false);
//                axisX.setTextColor()

            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

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
                    Log.i(TAG, "onBackPressed:exiiiiiiiiiiiiiiiiiiiiiiiiiiiiitte ");
//          super.onBackPressed();
                    finish();
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
