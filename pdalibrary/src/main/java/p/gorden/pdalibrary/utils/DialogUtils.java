package p.gorden.pdalibrary.utils;

import android.app.AlertDialog;
import android.content.Context;

import p.gorden.pdalibrary.R;
import p.gorden.pdalibrary.config.PdaConfig;

public class DialogUtils {

    private static AlertDialog alertDialog;

    public static void showAlertDialog(Context context) {
        if (alertDialog != null) {
            if (!PdaConfig.ISKANBAN)
                alertDialog.show();
        } else {
            alertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle("提示").setView(R.layout.dialog_wait).create();
        }
    }

    public static void hideAlertDialog(Context context) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
