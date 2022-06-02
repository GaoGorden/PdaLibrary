package p.gorden.pdalibrary.net;

import android.app.Activity;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import p.gorden.pdalibrary.base.BaseActivity;
import p.gorden.pdalibrary.common.CommonMethod;
import p.gorden.pdalibrary.config.PdaConfig;
import p.gorden.pdalibrary.utils.DialogUtils;

public class WebHelper {

    public static final String TAG = "WebHelper";

    private volatile static WebHelper instance = null;
    // 命名空间
    String nameSpace = "http://tempuri.org/";
    // EndPoint
    String url = PdaConfig.WEBSERVERURL;
    // 响应时间
    int TIME_OUT = 60 * 1_000;

    HttpTransportSE transport;

    static HashMap<String, Object> propertyMap = new HashMap<>();

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private WebQueryCallback webQueryCallback;

    public void setWebQueryCallback(WebQueryCallback webQueryCallback) {
        this.webQueryCallback = webQueryCallback;
    }

    private WebHelper() {
        init();
    }

    private void init() {
        transport = new HttpTransportSE(url, TIME_OUT);
    }

    public static WebHelper getInstance() {
        if (instance == null) {
            synchronized (WebHelper.class) {
                if (instance == null) {
                    instance = new WebHelper();
                }
            }
        }
        propertyMap.clear();
        return instance;
    }


    public WebHelper addProperty(String name, String value) {
        propertyMap.put(name, value);
        return instance;
    }

    @Deprecated
    public void call(BaseActivity activity, String methodName, WebCallback callback) {
        activity.runOnUiThread(activity::showAlertDialog);
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace + methodName;
        for (String s : propertyMap.keySet()) {
            rpc.addProperty(s, propertyMap.get(s));
        }
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(url);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 获取返回的数据
                if (envelope.bodyIn instanceof SoapFault) {
                    activity.runOnUiThread(activity::hideAlertDialog);
                    if (!PdaConfig.ISKANBAN) {
                        activity.runOnUiThread(() -> CommonMethod.showErrorDialog(activity, "后台请求失败，失败原因：" + ((SoapFault) envelope.bodyIn).getMessage()));
                    } else {
                        activity.runOnUiThread(() -> CommonMethod.showRightDialog(activity, "后台请求失败，失败原因：" + ((SoapFault) envelope.bodyIn).getMessage()));
                    }
                    return;
                }
                SoapObject object = (SoapObject) envelope.bodyIn;
                if (object == null) {
                    activity.runOnUiThread(activity::hideAlertDialog);
                    if (!PdaConfig.ISKANBAN) {
                        activity.runOnUiThread(() -> CommonMethod.showErrorDialog(activity, "网络请求失败，请检查网络~"));
                    } else {
                        activity.runOnUiThread(() -> CommonMethod.showRightDialog(activity, "网络请求失败，请检查网络~"));
                    }
                    return;
                }
                String result = object.getProperty(0).toString();

                activity.runOnUiThread(() -> {
                            activity.hideAlertDialog();
                            callback.result(result);
                            if (result.startsWith("[") || result.startsWith("0")) {
                                return;
                            }
                            if (result.equals("1")) {
                                callback.success(result);
                            } else {
                                callback.error(activity, result);

                            }
                        }
                );

            }
        });
    }

    public void callQuery(Activity activity, String flag, String sql) {
        String methodName = "query";
        String properName = "sql";
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        String soapAction = nameSpace + methodName;
        rpc.addProperty(properName, sql);

        envelope.bodyOut = rpc;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);


        NamedRunnable namedRunnable = new NamedRunnable(flag, activity, soapAction, envelope, transport);
        executorService.execute(namedRunnable);
    }

    class NamedRunnable implements Runnable {

        String flag;
        Activity activity;

        String mSoapAction;
        SoapSerializationEnvelope mEnvelope;
        HttpTransportSE mTransport;

        public NamedRunnable(String flag, Activity activity, String soapAction, SoapSerializationEnvelope envelope, HttpTransportSE transport) {
            this.flag = flag;
            this.activity = activity;

            this.mSoapAction = soapAction;
            this.mEnvelope = envelope;
            this.mTransport = transport;
            Log.e(TAG, "NamedRunnable: 初始化");
            Log.e(TAG, this.toString());
        }

        public void release() {
            this.activity = null;

            this.mSoapAction = null;
            this.mEnvelope = null;
            this.mTransport = null;
        }

        @Override
        public void run() {
            activity.runOnUiThread(() -> DialogUtils.showAlertDialog(activity));

            try {
                // 调用WebService
                mTransport.call(mSoapAction, mEnvelope);
                Log.e(TAG, Thread.currentThread().getName());
                Log.e(TAG, mEnvelope.toString());
            } catch (Exception e) {

                init();


                activity.runOnUiThread(() -> {
                    if (!PdaConfig.ISKANBAN) {
                        CommonMethod.showErrorDialog(activity, "连接失败，失败原因：" + e.getMessage());

                    } else {
                        CommonMethod.showToast(activity, "连接失败，失败原因：" + e.getMessage());
                        Log.e(TAG, mTransport.requestDump);
                        Log.e(TAG, mTransport.responseDump);
                    }
                    release();
                });
                e.printStackTrace();
            }

            // 获取返回的数据
            if (mEnvelope.bodyIn instanceof SoapFault) {
                activity.runOnUiThread(() -> {
                    DialogUtils.hideAlertDialog(activity);
                    if (!PdaConfig.ISKANBAN) {
                        CommonMethod.showErrorDialog(activity, "后台请求失败，失败原因：" + ((SoapFault) mEnvelope.bodyIn).getMessage());
                    } else {

                        CommonMethod.showToast(activity, "后台请求失败，失败原因：" + ((SoapFault) mEnvelope.bodyIn).getMessage());
                    }
                    release();
                });
                return;
            }
            SoapObject object = (SoapObject) mEnvelope.bodyIn;
            if (object == null) {
                activity.runOnUiThread(() -> {
                    DialogUtils.hideAlertDialog(activity);
                    if (!PdaConfig.ISKANBAN) {
                        CommonMethod.showErrorDialog(activity, "网络请求失败，请检查网络~");
                    } else {
                        CommonMethod.showToast(activity, "网络请求失败，请检查网络~");
                    }
                    release();
                });
                return;
            }

            String result = object.getProperty(0).toString();

            activity.runOnUiThread(() -> {
                        DialogUtils.hideAlertDialog(activity);
                        if (result.equals("0")) {
                            webQueryCallback.error(flag);
                        } else {
                            webQueryCallback.result(result, flag);
                        }
                        release();
                    }
            );

        }
    }


    public interface WebCallback {
        default void result(String result) {

        }

        default void success(String result) {

        }

        default void error(Activity activity, String result) {
            CommonMethod.showErrorDialog(activity, "操作失败：" + result);
        }
    }

    public interface WebQueryCallback {
        void result(String result, String flag);

        void error(String flag);
    }
}
