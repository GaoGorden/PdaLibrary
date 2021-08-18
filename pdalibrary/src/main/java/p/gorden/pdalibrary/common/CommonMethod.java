package p.gorden.pdalibrary.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.view.NumberView;
import p.gorden.pdalibrary.view.ScannerView;
import p.gorden.pdalibrary.view.ScannerViewWide;
import p.gorden.pdalibrary.view.TextshowView;

public class CommonMethod {

    public static MediaPlayer mediaPlayerError;
    public static MediaPlayer mediaPlayerSuccess;

    public static void playMediaSuccess(Context context) {
        if (mediaPlayerSuccess == null) {
            mediaPlayerSuccess = MediaPlayer.create(context, R.raw.success);
        }
        mediaPlayerSuccess.start();
    }

    public static void playMediaError(Context context) {
        if (mediaPlayerError == null) {
            mediaPlayerError = MediaPlayer.create(context, R.raw.beep);
        }
        mediaPlayerError.start();
    }

    public static void releaseMedia() {
        if (mediaPlayerError != null) {
            mediaPlayerError.release();
            mediaPlayerError = null;
        }
        if (mediaPlayerSuccess != null) {
            mediaPlayerSuccess.release();
            mediaPlayerSuccess = null;
        }
    }

    public static void playMediaErrorLoop(Context context) {
        mediaPlayerError = MediaPlayer.create(context, R.raw.beep);
        mediaPlayerError.setLooping(true);
        mediaPlayerError.start();
    }

    public static void stopMediaError() {
        mediaPlayerError.setLooping(false);
        mediaPlayerError.stop();
    }

    public static void showResultDialog(Context context, boolean result, String errorMessage) {
        if (result) {
            new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("提示").setMessage("数据操作成功！").setIcon(R.drawable.ic_check_circle_black_24dp)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                    }).show();
        } else {
            new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("错误信息").setMessage(errorMessage).setIcon(R.drawable.ic_error_black_24dp)
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                    }).show();
        }
    }

    public static void showDialog(Context context, String message) {
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("提示").setMessage(message)
                .setIcon(R.drawable.ic_check_circle_black_24dp)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                }).show();
    }

    public static void showRightDialog(Context context, String message) {
//        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("提示").setMessage(message).setIcon(R.drawable.ic_check_circle_black_24dp)
//                .setPositiveButton("确定", (dialogInterface, i) -> {
//                }).show();

        playMediaSuccess(context);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showRightLongDialog(Context context, String message) {
//        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("提示").setMessage(message).setIcon(R.drawable.ic_check_circle_black_24dp)
//                .setPositiveButton("确定", (dialogInterface, i) -> {
//                }).show();

        playMediaSuccess(context);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showErrorDialog(Context context, String message) {
        playMediaError(context);
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("警告").setMessage(message).setIcon(R.drawable.ic_error_black_24dp)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                }).show();
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showErrorDialog(Context context, String message, String okMsg, DialogInterface.OnClickListener okListener, String ngMsg, DialogInterface.OnClickListener ngListener) {
        playMediaError(context);
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("警告").setMessage(message).setIcon(R.drawable.ic_error_black_24dp)
                .setPositiveButton(okMsg, okListener).setNegativeButton(ngMsg, ngListener).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断字符串是否全为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }

    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getSimpleTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getMinutes(int timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");// HH:mm:ss
        Date date = new Date(timeMillis);
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getListTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    public static String getFormatTime(String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    private final static int PLAYER_OK = 1;
    private final static int PLAYER_NG = 0;

    /**
     * 播放提示音
     *
     * @param context
     * @param sound
     */
    public static void soundPlayer(Context context, int sound) {
        MediaPlayer mediaPlayer = null;
        switch (sound) {
            case 1:
                mediaPlayer = MediaPlayer.create(context, R.raw.scanok);
                mediaPlayer.start();
                break;
            case 0:
                mediaPlayer = MediaPlayer.create(context, R.raw.beep);
                mediaPlayer.start();
                break;
        }
        mediaPlayer.release();
    }

    /**
     * 检查输入
     *
     * @param context
     * @param views
     */
    public static boolean checkScannerView(Context context, LinearLayout[] views) {
        for (LinearLayout view : views) {

            if (view instanceof ScannerViewWide) {
                ScannerViewWide scannerViewWide = (ScannerViewWide) view;
                if (scannerViewWide.getText().equals("")) {
                    showErrorDialog(context, scannerViewWide.getTitle().replace(" ", "") + "不能为空！");
                    return false;
                }
            } else if (view instanceof ScannerView) {
                ScannerView scannerView = (ScannerView) view;
                if (scannerView.getText().equals("")) {
                    showErrorDialog(context, scannerView.getTitle().replace(" ", "") + "不能为空！");
                    return false;
                }
            } else if (view instanceof TextshowView) {
                TextshowView textshowView = (TextshowView) view;
                if (textshowView.getText().equals("")) {
                    showErrorDialog(context, textshowView.getTitle().replace(" ", "") + "不能为空！");
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * 文本置空
     *
     * @param views
     */
    public static void setEmpty(LinearLayout[] views) {
        for (LinearLayout view : views) {
            if (view instanceof ScannerViewWide) {
                ScannerViewWide scannerViewWide = (ScannerViewWide) view;
                scannerViewWide.setText("");
            } else if (view instanceof ScannerView) {
                ScannerView scannerView = (ScannerView) view;
                scannerView.setText("");
            }else if (view instanceof TextshowView) {
                TextshowView textshowView = (TextshowView) view;
                textshowView.setText("");
            }
        }
    }

    public static void checkNumberView(Context context, NumberView[] views) {
        for (NumberView view : views) {
            String text = view.getTitle();
            if (view.getEdittext().getText().toString().equals("")) {
                showDialog(context, text + "不能为空！");
            }
        }
    }

    // 去掉字符串前的 0
    public static String deleteZero(String str) {
        while (str.startsWith("0")) {
            str = str.substring(1);
        }
        return str;
    }

    public static ArrayList<String> getKey(HashMap<String, String> map, String testValue) {
        ArrayList<String> keyList = new ArrayList<>();
        // 遍历 map
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value.contains(",")) {
                HashSet<String> set_wuliao = new HashSet<>(Arrays.asList(value.split(",")));
                if (setContain(set_wuliao, testValue)) {
                    keyList.add(key);
                }
            } else {
                if (testValue.equals(value) || testValue.contains(value)) {
                    keyList.add(key);
                }
            }
        }
        return keyList;
    }

    // 集合是否包含类似字符串
    public static boolean setContain(Set<String> set, String str) {
        for (String s : set) {
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查时间
     *
     * @param closedDate 截止时间
     */
    public static void checkDate(Context context, int closedDate) {
        long temp = getFirstInstallTime(context) / 1000;
        if (temp > closedDate) {
            new AlertDialog.Builder(context).setTitle("错误").setMessage("软件安装错误！").setCancelable(false).show();
        }
    }

    /**
     * 获取第一次安装的时间
     *
     * @return 第一次安装的时间
     */
    private static long getFirstInstallTime(Context context) {
        long firstInstallTime = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //应用装时间
            firstInstallTime = packageInfo.firstInstallTime;
            //应用最后一次更新时间
            long lastUpdateTime = packageInfo.lastUpdateTime;
            System.out.print("first install time : " + firstInstallTime + " last update time :" + lastUpdateTime);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return firstInstallTime;
    }
}
