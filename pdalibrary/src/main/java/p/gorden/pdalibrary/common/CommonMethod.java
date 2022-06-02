package p.gorden.pdalibrary.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import p.gorden.pdalibrary.MyScanActivity;
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

    public static String getFormatTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }

    // 生成自定义的单号
    public static String getFormatReceipt(String prefix) {
        return prefix + CommonMethod.getFormatTime("yyMMddHHmmss");
    }


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
     * 检查输入并报错
     *
     * @param context
     * @param views
     */
    @Deprecated
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
     * 检查输入并报错
     *
     * @param views
     */
    public static boolean checkScannerView(LinearLayout... views) {
        for (LinearLayout view : views) {
            Context context = view.getContext();
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
     * 检查输入
     *
     * @param views
     */
    public static boolean checkScannerViewWithoutAlarm(LinearLayout[] views) {
        for (LinearLayout view : views) {

            if (view instanceof ScannerViewWide) {
                ScannerViewWide scannerViewWide = (ScannerViewWide) view;
                if (scannerViewWide.getText().equals("")) {
                    return false;
                }
            } else if (view instanceof ScannerView) {
                ScannerView scannerView = (ScannerView) view;
                if (scannerView.getText().equals("")) {
                    return false;
                }
            } else if (view instanceof TextshowView) {
                TextshowView textshowView = (TextshowView) view;
                if (textshowView.getText().equals("")) {
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
    @Deprecated
    public static void setEmpty(LinearLayout[] views) {
        for (LinearLayout view : views) {
            if (view instanceof ScannerViewWide) {
                ScannerViewWide scannerViewWide = (ScannerViewWide) view;
                scannerViewWide.setText("");
            } else if (view instanceof ScannerView) {
                ScannerView scannerView = (ScannerView) view;
                scannerView.setText("");
            } else if (view instanceof TextshowView) {
                TextshowView textshowView = (TextshowView) view;
                textshowView.setText("");
            }
        }
    }

    /**
     * 文本置空
     *
     * @param views
     */
    public static void setViewTextEmpty(LinearLayout ... views) {
        for (LinearLayout view : views) {
            if (view instanceof ScannerViewWide) {
                ScannerViewWide scannerViewWide = (ScannerViewWide) view;
                scannerViewWide.setText("");
            } else if (view instanceof ScannerView) {
                ScannerView scannerView = (ScannerView) view;
                scannerView.setText("");
            } else if (view instanceof TextshowView) {
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

    //根据map的value获取map的key
    public static String getKey(Map<String, String> map, String value) {
        String key = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                key = entry.getKey();
            }
        }
        return key;
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


    public static <T> ArrayList<T> getJsonArrayString(String json, Class<T> claz) {

        json = jsonString(json);

        json = replaceBlank(json);

        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        Gson gson = new Gson();
        ArrayList<T> resultList = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement element : jsonArray) {
            //使用GSON，直接转成Bean对象
            T userBean = gson.fromJson(element, claz);
            resultList.add(userBean);
        }
        return resultList;
    }

    // 去除json数据中的引号
    private static String jsonString(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            temp[j] = '”';
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }

    // 去除字符串中的空格、回车、换行符、制表符
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
//            Pattern p = Pattern.compile("\\s*|\\t|\\r|\\n");
            Pattern p = Pattern.compile("\\t|\\r|\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            // 处理字符串中的中文斜杠
            dest = dest.replace("\\", "/");
        }
        return dest;
    }


    public static void showDataSelector(Context context, ScannerView scannerView, int year, int month, int day) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder date = new StringBuilder();
                scannerView.setText(date.append(String.valueOf(year)).append("年").append(month).append("月").append(day).append("日").toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(context, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(year, month - 1, day, null);
    }

    public static String getExecuteSqlstr(ArrayList<String> sqlList) {
        StringBuilder builder = new StringBuilder();
        for (String s : sqlList) {
            builder.append(s).append("ç");
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

    public static void jumpToScan(FragmentActivity activity, int requestCode){
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (!allGranted) {
                            Toast.makeText(activity, "扫描需要摄像头权限！", Toast.LENGTH_LONG).show();
                        } else {
                            activity.startActivityForResult(new Intent(activity, MyScanActivity.class),requestCode);
                        }
                    }
                });
    }
}
