package p.gorden.pdalibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.utils.ChartUtils;

public class BarChartView extends BarChart {

    private Typeface tfLight;
    private ArrayList<String> xStrings;

    private Float textSize;
    private Float yTextSize;
    private Float xTextSize;
    private Float barValueTextSize;

    private int textColor = getResources().getColor(R.color.chart_text_color);

    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarChartView);
        textSize = a.getDimension(R.styleable.BarChartView_text_size_bar, 12f);
        yTextSize = textSize * 0.4f;
        xTextSize = textSize;
        barValueTextSize = textSize * 0.4f;
        a.recycle();
    }

    public void setTextSizeRate(float xRate, float yRate, float barValueRate) {
        yTextSize = textSize * yRate;
        xTextSize = textSize * xRate;
        barValueTextSize = textSize * barValueRate;
    }

    private void myInit() {
        setDrawBarShadow(false);
        setDrawValueAboveBar(true);

        getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        setPinchZoom(false);

        setDrawGridBackground(false);
        // setDrawYLabels(false);
        Legend legend = getLegend();
        legend.setEnabled(false);

        tfLight = ChartUtils.getTypeface(getContext(), ChartUtils.LIGHT);

        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setTextSize(xTextSize);
        xAxis.setTextColor(textColor);

        DefaultValueFormatter custom = new DefaultValueFormatter(0);

        YAxis leftAxis = getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setTextSize(yTextSize);
        leftAxis.setTextColor(textColor);

        YAxis rightAxis = getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setTextSize(yTextSize);
        rightAxis.setTextColor(textColor);

        setMinOffset(30f); // 解决底部文字显示不全问题。


//        Legend l = getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.SQUARE);
//        l.setFormSize(9f);
//        l.setTextSize(11f);
//        l.setXEntrySpace(4f);

//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(chart); // For bounds control
//        setMarker(mv); // Set the marker to the chart
//        setData(7,100);
    }

    public void setData(ArrayList<BarEntry> values, ArrayList<String> xDisplayStrings) {

//        float start = 1f;

//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        for (int i = (int) start; i < start + count; i++) {
//            float val = (float) (Math.random() * (range + 1));
//
//            if (Math.random() * 100 < 25) {
//                values.add(new BarEntry(i, val, null));
//            } else {
//                values.add(new BarEntry(i, val));
//            }
//        }
        myInit();

        setXAxisRenderer(new MyXAxisRenderer(mViewPortHandler, mXAxis, mLeftAxisTransformer, xDisplayStrings));

        BarDataSet set1;
        set1 = new BarDataSet(values, "The year 2022");
        set1.setDrawIcons(false);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        set1.setColors(colors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(barValueTextSize);
        data.setValueTypeface(tfLight);
        data.setBarWidth(0.9f);
        data.setValueTextColor(textColor);

        setData(data);
        animateY(1400, Easing.EaseInOutQuad);
        invalidate();
    }


    class MyXAxisRenderer extends XAxisRenderer {
        ArrayList<String> labels;

        public MyXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans, ArrayList<String> labels) {
            super(viewPortHandler, xAxis, trans);
            this.labels = labels;
        }

        @Override
        protected void drawLabels(Canvas c, float pos, MPPointF anchor) {

            final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
            boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

            float[] positions = new float[mXAxis.mEntryCount * 2];

            for (int i = 0; i < positions.length; i += 2) {

                // only fill x values
                if (centeringEnabled) {
                    positions[i] = mXAxis.mCenteredEntries[i / 2];
                } else {
                    positions[i] = mXAxis.mEntries[i / 2];
                }
            }

            mTrans.pointValuesToPixel(positions);

            for (int i = 0; i < positions.length; i += 2) {

                float x = positions[i];

                if (mViewPortHandler.isInBoundsX(x)) {

                    String label = labels.get(i / 2);

                    if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                        // avoid clipping of the last
                        if (i / 2 == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);

                            if (width > mViewPortHandler.offsetRight() * 2
                                    && x + width > mViewPortHandler.getChartWidth())
                                x -= width / 2;

                            // avoid clipping of the first
                        } else if (i == 0) {

                            float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                            x += width / 2;
                        }
                    }

                    drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
                }
            }
        }
    }
}
