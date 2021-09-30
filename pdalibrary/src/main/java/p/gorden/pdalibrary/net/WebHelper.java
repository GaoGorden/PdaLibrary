package p.gorden.pdalibrary.net;

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

public class WebHelper {

    private volatile static WebHelper instance = null;
    // 命名空间
    String nameSpace = "http://tempuri.org/";
    // EndPoint
    String url = "http://39.99.34.177/app/WebService1.asmx";
    // SOAP Action


//    SoapObject rpc;
//    SoapSerializationEnvelope envelope;
//    HttpTransportSE transport;

    static HashMap<String, Object> propertyMap = new HashMap<>();

    ExecutorService executorService = Executors.newSingleThreadExecutor();


    private WebHelper() {
// 指定WebService的命名空间和调用的方法名


        // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
//        rpc.addProperty("mobileCode", phoneSec);
        //rpc.addProperty("userId", "");

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


    public WebHelper addProperty(String name, Object value) {
        propertyMap.put(name, value);
        return instance;
    }

    public void call(BaseActivity activity, String methodName, WebCallback callback) {
        activity.showAlertDialog();
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
                    activity.hideAlertDialog();
                    activity.runOnUiThread(() -> CommonMethod.showErrorDialog(activity, "后台请求失败，请检查服务~"));
                    return;
                }
                SoapObject object = (SoapObject) envelope.bodyIn;
                if (object == null) {
                    activity.hideAlertDialog();
                    activity.runOnUiThread(() -> CommonMethod.showErrorDialog(activity, "网络请求失败，请检查网络~"));
                    return;
                }
                String result = object.getProperty(0).toString();
                activity.hideAlertDialog();
                activity.runOnUiThread(() -> callback.result(result));

            }
        });
    }


    public interface WebCallback {
        void result(String result);
//        void error();
    }
}
