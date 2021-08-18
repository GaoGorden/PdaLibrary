package p.gorden.pdalibrary.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.yzq.zxinglibrary.common.Constant;

import java.util.List;

import p.gorden.pdalibrary.MyScanActivity;
import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.common.CommonMethod;
import p.gorden.pdalibrary.view.ScannerView;

/**
 * Created by GORDENyou on 2020/2/2.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected AlertDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog = new AlertDialog.Builder(BaseActivity.this
                , R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("提示").setView(R.layout.dialog_wait).create();

        initView();
        dealData();
    }

    protected abstract void initView();

    protected abstract void dealData();


    public void showAlertDialog() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    public void hideAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    protected void jumpToScan(int scanCode) {
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (!allGranted) {
                            Toast.makeText(getBaseContext(), "扫描需要摄像头权限！", Toast.LENGTH_LONG).show();
                        } else {
                            startActivityForResult(new Intent(getBaseContext(), MyScanActivity.class), scanCode);
                        }
                    }
                });
    }

    protected String getBarcodeFromScan(Intent data) {
        if (data.getStringExtra(Constant.CODED_CONTENT) != null) {
            return data.getStringExtra(Constant.CODED_CONTENT);
        } else {
            return data.getStringExtra(MyScanActivity.BARCODE);
        }
    }

    protected void setBarcodeFromScan(ScannerView scannerView, Intent data) {
        scannerView.setText(getBarcodeFromScan(data));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonMethod.releaseMedia();
    }
}
