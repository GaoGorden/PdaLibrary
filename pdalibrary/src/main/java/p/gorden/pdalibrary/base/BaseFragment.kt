package p.gorden.pdalibrary.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.yzq.zxinglibrary.common.Constant
import p.gorden.pdalibrary.MyScanActivity
import p.gorden.pdalibrary.R
import p.gorden.pdalibrary.common.CommonMethod
import p.gorden.pdalibrary.net.JDBCHelper
import p.gorden.pdalibrary.view.ScannerView

abstract class BaseFragment : Fragment() {

    protected val alertDialog by lazy {
        AlertDialog.Builder(
            requireContext(), R.style.Theme_AppCompat_Light_Dialog_Alert
        ).setTitle("提示").setView(R.layout.dialog_wait).create()
    }

    protected val jdbcHelper by lazy {
        JDBCHelper.getJDBCHelper(alertDialog)
    }

    private val mapScanView = mutableMapOf<ScannerView, () -> Unit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return getBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        dealData()
    }

    abstract fun getBinding(): View

    abstract fun initViews()

    abstract fun dealData()

    fun showAlertDialog() {
        alertDialog.show()
    }

    fun hideAlertDialog() {
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }

    fun addScanView(scannerView: ScannerView, action:()->Unit){
        mapScanView[scannerView] = action
    }

    protected fun jumpToScan(scanCode: Int) {
        CommonMethod.jumpToScan(requireActivity(), scanCode)
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