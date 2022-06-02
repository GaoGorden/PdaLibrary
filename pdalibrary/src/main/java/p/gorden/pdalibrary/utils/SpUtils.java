package p.gorden.pdalibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * @Description:
 * @author: GordenGao
 * @Email: gordengao124@gmail.com
 * @date: 2021年06月27日 21:36
 */
public class SpUtils {

    public static String USERNAME = "USERNAME";

    public static String SQLUSERNAME = "SQLUSERNAME";
    public static String SQLPWS = "SQLPWS";
    public static String IP = "IP";
    public static String DATABASE = "DATABASE";


    public static void saveStringValue(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName() +"userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveIntValue(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName() +"userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName() +"userInfo", MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName() +"userInfo", MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }
}
