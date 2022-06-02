package p.gorden.pdalibrary.base

import android.Manifest
import android.app.Activity
import p.gorden.pdalibrary.net.JDBCHelper
import android.os.Bundle
import p.gorden.pdalibrary.R
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.yzq.zxinglibrary.common.Constant
import p.gorden.pdalibrary.MyScanActivity
import p.gorden.pdalibrary.view.ScannerView
import p.gorden.pdalibrary.common.CommonMethod
import p.gorden.pdalibrary.config.PdaConfig

/**
 * Created by gorden on 2020/2/2.
 * mailbox:1193688859@qq.com
 * have nothing but……
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val alertDialog by lazy {
        AlertDialog.Builder(
            this@BaseActivity, R.style.Theme_AppCompat_Light_Dialog_Alert
        ).setTitle("提示").setView(R.layout.dialog_wait).create()
    }

    protected val jdbcHelper by lazy {
        JDBCHelper.getJDBCHelper(alertDialog)
    }

    private val mapScanView = mutableMapOf<ScannerView, () -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        dealData()
    }

    protected abstract fun initView()
    protected abstract fun dealData()

    fun showAlertDialog() {
        if(!PdaConfig.ISKANBAN){
            alertDialog.show()
        }
    }

    fun hideAlertDialog() {
        if (!PdaConfig.ISKANBAN &&alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }

    fun addScanView(scannerView: ScannerView, action:()->Unit){
        mapScanView[scannerView] = action
    }

    protected fun jumpToScan(scanCode: Int) {
        CommonMethod.jumpToScan(this, scanCode)
    }

    protected fun getBarcodeFromScan(data: Intent?): String {
        return if (data?.getStringExtra(Constant.CODED_CONTENT) != null) {
            data!!.getStringExtra(Constant.CODED_CONTENT)
        } else {
            data!!.getStringExtra(MyScanActivity.BARCODE)
        }
    }

    protected fun setBarcodeFromScan(scannerView: ScannerView, data: Intent?) {
        scannerView.text = getBarcodeFromScan(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonMethod.releaseMedia()
        jdbcHelper!!.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            mapScanView.keys.forEach {
                if(it.hashCode() shr 16 == requestCode){
                    setBarcodeFromScan(it, data!!)
                    mapScanView[it]?.invoke()
                }
            }
        }
    }
}