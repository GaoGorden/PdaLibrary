package p.gorden.pdalibrary.ext

import android.app.Activity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import androidx.fragment.app.FragmentActivity
import p.gorden.pdalibrary.base.BaseActivity
import p.gorden.pdalibrary.common.CommonMethod
import p.gorden.pdalibrary.config.PdaConfig
import p.gorden.pdalibrary.view.ScannerView

fun AutoCompleteTextView.setEnterOrScanListener(callback: () -> Unit) {
    this.setOnEditorActionListener { v, actionId, event ->
        if (event != null && event.action == KeyEvent.ACTION_UP || actionId == EditorInfo.IME_ACTION_DONE) {
            callback()
        }
        true
    }
}


fun ScannerView.setEnterOrScanListener(activity: Activity, callback: () -> Unit) {
    val tempCallback: () -> Unit = {
        if (this.text.isEmpty()) {
            CommonMethod.showErrorDialog(context, this.title.replace(" ", "") + "不能为空！")
        } else {
            callback()
        }
    }

    this.editText.setOnEditorActionListener { v, actionId, event ->
        if (event != null && event.action == KeyEvent.ACTION_UP || actionId == EditorInfo.IME_ACTION_DONE) {
            tempCallback()
        }
        true
    }

    this.editText.setOnItemClickListener { parent, view, position, id ->
        tempCallback()
    }

    this.button.setOnClickListener {
        if (!PdaConfig.ISKANBAN) {
            CommonMethod.jumpToScan(activity as BaseActivity, this.hashCode() shr 16)
        } else {
            tempCallback()
        }
    }

    (activity as BaseActivity).addScanView(this, tempCallback)
}

fun ScannerView.cleanAndFocus() {
    this.text = ""
    this.editText.requestFocus()
}