package p.gorden.pdalibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.decode.ImageUtil;

import java.lang.ref.WeakReference;

import p.gorden.pdalibrary.utils.QRCodeParseUtils;

/**
 * @Description:
 * @author: GordenGao
 * @Email: gordengao124@gmail.com
 * @date: 2021年08月15日 17:41
 */
public class MyScanActivity extends CaptureActivity {

    public static final int BARCODE_RESULT = 0x001;
    public static final String BARCODE = "barcode";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_IMAGE && resultCode == RESULT_OK) {
            String path = ImageUtil.getImageAbsolutePath(this, data.getData());
            parsePhoto(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 启动线程解析二维码图片
     *
     * @param path
     */
    private void parsePhoto(String path) {
        //启动线程完成图片扫码
        new QrCodeAsyncTask(this, path).execute(path);
    }

    /**
     * AsyncTask 静态内部类，防止内存泄漏
     */
    static class QrCodeAsyncTask extends AsyncTask<String, Integer, String> {
        private WeakReference<Activity> mWeakReference;
        private String path;

        public QrCodeAsyncTask(Activity activity, String path) {
            mWeakReference = new WeakReference<>(activity);
            this.path = path;
        }

        @Override
        protected String doInBackground(String... strings) {
            // 解析二维码/条码
            return QRCodeParseUtils.syncDecodeQRCode(path);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //识别出图片二维码/条码，内容为s
            Activity activity = mWeakReference.get();
//            if (activity != null) {
//                activity.handleQrCode(s);
//            }
            if (null == s) {
                Toast.makeText(activity, "该图片无法识别二维码", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra(BARCODE, s);
                activity.setResult(Activity.RESULT_OK);
            }
        }
    }
}
