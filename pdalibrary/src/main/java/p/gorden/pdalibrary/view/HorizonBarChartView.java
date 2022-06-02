package p.gorden.pdalibrary.view;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HorizonBarChartView extends BarChart {
    public HorizonBarChartView(Context context) {
        this(context, null);
    }

    public HorizonBarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonBarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        myInit();
    }

    private void myInit() {
        getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        setPinchZoom(false);

        setDrawBarShadow(false);
        setDrawGridBackground(false);

        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        getAxisLeft().setDrawGridLines(false);
        

        // add a nice and smooth animation
        animateY(1500);

        getLegend().setEnabled(false);
        
        setMyData();
    }

    private void setMyData() {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            float multi = (100 + 1);
            float val = (float) (Math.random() * multi) + multi / 3;
            values.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (getData() != null &&
                getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) getData().getDataSetByIndex(0);
            set1.setValues(values);
            getData().notifyDataChanged();
            notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            setData(data);
            setFitBars(true);
        }

        invalidate();
    }
    
}
