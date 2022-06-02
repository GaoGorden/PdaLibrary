package p.gorden.pdalibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import androidx.core.graphics.TypefaceCompat;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.utils.Utils;

import java.lang.reflect.Field;

public class ChartUtils {

    public static final String REGULAR = "regular";
    public static final String LIGHT = "light";

    public static Typeface getTypeface(Context context, String typefaceName) {
        switch (typefaceName) {
            case REGULAR:
                return Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");

            case LIGHT:
                return Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");

            default:
                return Typeface.defaultFromStyle(Typeface.NORMAL);
        }
    }

    public static void setNoDataTextSize(Chart chart, Float textSize) {
        try {
            Field mInfoPaint = Chart.class.getDeclaredField("mInfoPaint");
            mInfoPaint.setAccessible(true);

            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(247, 189, 51)); // orange
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(Utils.convertDpToPixel(textSize));

            mInfoPaint.set(chart, paint);
            chart.invalidate();

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
