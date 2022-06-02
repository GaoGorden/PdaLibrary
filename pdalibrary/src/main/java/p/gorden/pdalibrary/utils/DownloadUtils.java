package p.gorden.pdalibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;

public class DownloadUtils {

    public static void showDownloadDialog(Context context, String downloadUrl){
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("目前有新版本，已优化，请点击确定升级")
                .setPositiveButton("确定", (dialog, which) -> {
                    downApk(context, downloadUrl);
                })
                .create()
                .show();
    }


    /**
     * 联网下载apk
     * @param context
     * @param downloadUrl
     */
    private static void downApk(Context context, String downloadUrl) {
        try {
            UtilsOKHttp.getInstance(context).downFile(downloadUrl, context.getCacheDir().getPath(), new UtilsOKHttp.OKCallback() {
                @Override
                public void onSuccess(String result) {
                    //成功返回文件目录
                    if (result != null && result.length() > 0) {
                        installApk(result, context);
                    }
                }

                @Override
                public void onProcess(int i) {
                    //过程
//                    pb1.setProgress(i);
//                    if (i == 100) {
//                        Toast.makeText(mActivity, "下载成功", Toast.LENGTH_SHORT).show();
//                        pb1.setAnimRun(false);
//                    }
                }

                @Override
                public void onFail(String failResult) {
                    //失败
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装 apk
     *
     * @param path
     */
    private static void installApk(String path, Context context) {
        try {
            Log.e("aaa", "installApk: " + path);
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
                // 生成文件的uri，，
                // 注意 下面参数com.com.ljp.downdemo 为apk的包名加上.fileprovider，
                data = FileProvider.getUriForFile(context, "com.gorden.fileprovider", file);
            } else {
                data = Uri.fromFile(file);
            }
            Log.e("aaa", "installApk: " + data);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
