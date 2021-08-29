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

    public static final String USERNAME = "username";
    public static final String JDBCUSER = "jdbc_user";
    public static final String JDBCPWB = "jdbc_pwb";
    public static final String JDBCURL = "jdbc_url";

    public static void saveStringValue(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringValue(Context context, String key, String defaultValue){
        SharedPreferences pref = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        return pref.getString(key, defaultValue);
    }
}
