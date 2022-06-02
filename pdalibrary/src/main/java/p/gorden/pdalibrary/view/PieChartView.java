package p.gorden.pdalibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.utils.ChartUtils;

public class PieChartView extends PieChart {

    private Float textSize;
    private Float centerTextSize;
    private Float labelTextSize;
    private int textColor = getResources().getColor(R.color.chart_text_color);

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        textSize = a.getDimension(R.styleable.PieChartView_text_size_pie, 12f);
        centerTextSize = textSize;
        labelTextSize = textSize * 0.5f;
        a.recycle();
    }

    public void setTextSizeRate(float centerRate, float labelRate) {
        centerTextSize = textSize * centerRate;
        labelTextSize = textSize * labelRate;
    }

    private void myInit() {
        setUsePercentValues(false);
        getDescription().setEnabled(false);
        setExtraOffsets(5, 10, 5, 5);

        setDragDecelerationFrictionCoef(0.95f);

        setDrawHoleEnabled(true);
        setHoleColor(Color.TRANSPARENT);

        setTransparentCircleColor(Color.GRAY);
        setTransparentCircleAlpha(110);

        setHoleRadius(58f);
        setTransparentCircleRadius(61f);

        setDrawCenterText(true);

        setRotationAngle(0);
        // enable rotation of the chart by touch
        setRotationEnabled(true);
        setHighlightPerTapEnabled(true);

        // setUnit(" €");
        // setDrawUnitsInChart(true);

        // add a selection listener
//        setOnChartValueSelectedListener(this);

        animateY(1400, Easing.EaseInOutQuad);
        // spin(2000, 0, 360);

        Legend l = getLegend();
        l.setTextSize(textSize);
        l.setTextColor(textColor);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        setEntryLabelColor(Color.TRANSPARENT);
        setEntryLabelTypeface(ChartUtils.getTypeface(getContext(), ChartUtils.REGULAR));
        setEntryLabelTextSize(labelTextSize); // 扇形中字体大小
    }

    public void setData(ArrayList<PieEntry> entries, String label) {
//        ArrayList<PieEntry> entries = new ArrayList<>();
//
//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
//                    parties[i % parties.length], null));
//        }

        myInit();

        PieDataSet dataSet = new PieDataSet(entries, label);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

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

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        // 设置折线
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.6f);


        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(labelTextSize);
        data.setValueTextColor(textColor);
        data.setValueTypeface(ChartUtils.getTypeface(getContext(), ChartUtils.REGULAR));
        setData(data);

        // undo all highlights
        highlightValues(null);

        invalidate();
        animateY(1400, Easing.EaseInOutQuad);
    }

    public void setCenterSpannableText(String gongdan, String count) {

        SpannableString s = new SpannableString("当前工单:" + gongdan + "\n总数：" +
                count);
        s.setSpan(new RelativeSizeSpan(1.0f), 4, 9, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 5, gongdan.length() + 5, 0);
        s.setSpan(new RelativeSizeSpan(1.1f), 5, gongdan.length() + 5, 0);

        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - count.length(), s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.1f), s.length() - count.length(), s.length(), 0);

//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);

        setCenterTextTypeface(ChartUtils.getTypeface(getContext(), ChartUtils.LIGHT));
        setCenterTextSize(centerTextSize);
        setCenterText(s);
        setCenterTextColor(textColor);
    }

}
