package p.gorden.pdalibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：admin
 * 时间：2018/5/23 0023
 * 用途：
 */

public class UtilsOKHttp {
    private static OkHttpClient client;
    private volatile static UtilsOKHttp manager;
    private Handler handler;

    private UtilsOKHttp(Context context) {
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
        handler = new Handler(Looper.getMainLooper());
    }

    public static UtilsOKHttp getInstance(Context context) {
        if (manager == null) {
            manager = new UtilsOKHttp(context);
        }
        return manager;
    }

    public void get(String url, final OKCallback callBack) {
        get(url, null, callBack);
    }

    public void get(String url, HashMap<String, String> params, final OKCallback callBack) {
        if (params != null && params.size() > 0) {
            url = makeGetUrl(url, params);
        }
        final Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onFailJsonStringMethod("请求失败", callBack);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                } else {
                    onFailJsonStringMethod("请求失败", callBack);
                }
            }
        });
    }

    /**
     * 下载地址
     *
     * @param url
     * @param savePath
     * @throws Exception
     */
    public void downFile(final String url, final String savePath, final OKCallback okCallback) throws Exception {
        if (url != null && savePath != null) {
            if (new File(savePath).exists()) {
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Connection", "close")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        onFailJsonStringMethod(e.getMessage(), okCallback);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            is = response.body().byteStream();
                            long total = response.body().contentLength();
                            File file = new File(savePath, url.substring(url.lastIndexOf("/") + 1));
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                Log.e("aaa", "download progress : " + progress);
                                onProcessMethod(progress, okCallback);
                            }
                            fos.flush();
                            fos.close();
                            //成功以后返回file的地址
                            onSuccessJsonStringMethod(file.getAbsolutePath(), okCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                throw new FileNotFoundException("找不到存放地址");
            }
        } else {
            throw new NullPointerException("参数为空");
        }
    }

    /**
     * post 模拟表单提交
     */
    public void post(String url, HashMap<String, String> params, final OKCallback callBack) {
        FormBody.Builder form_builder = new FormBody.Builder();
        //表单对象，包含以input开始的对象，以html表单为主
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody request_body = form_builder.build();
        Request request = new Request.Builder().url(url).post(request_body).build();
        //采用post方式提交
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onFailJsonStringMethod("请求失败", callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                } else {
                    onFailJsonStringMethod("请求失败", callBack);
                }
            }
        });
    }

    /**
     * post请求上传文件....包括图片....流的形式传任意文件...
     * 参数1 url
     * file表示上传的文件
     * fileName....文件的名字,,例如aaa.jpg
     * params ....传递除了file文件 其他的参数放到map集合
     */
    public static void uploadBase64Img(String url, String img, String fileName, Map<String, String> params, Callback callback) {
        //创建OkHttpClient请求对象

        //MultipartBody多功能的请求实体对象,,,formBody只能传表单形式的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"fileBytes\""),
                RequestBody.create(MediaType.parse("text/plain;charset=ISO-8859-1"), img))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"serviceType\""),
                        RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), fileName + ""));

        //参数
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        //构建
        MultipartBody multipartBody = builder.build();
        //创建Request
        Request request = new Request.Builder().url(url).post(multipartBody).build();
        //得到Call
        Call call = client.newCall(request);
        //执行请求
        call.enqueue(callback);

    }


    /**
     * post请求上传文件....包括图片....流的形式传任意文件...
     * 参数1 url
     * file表示上传的文件
     * fileName....文件的名字,,例如aaa.jpg
     * params ....传递除了file文件 其他的参数放到map集合
     */
    public static void uploadFile(String url, File file, String fileName, Map<String, String> params, Callback callback) {
        //创建OkHttpClient请求对象

        //MultipartBody多功能的请求实体对象,,,formBody只能传表单形式的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        //参数
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
        //文件...参数name指的是请求路径中所接受的参数...如果路径接收参数键值是fileeeee,
        //此处应该改变
        builder.addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("application/octet-stream"), file));
        //构建
        MultipartBody multipartBody = builder.build();
        //创建Request
        Request request = new Request.Builder().url(url).post(multipartBody).build();
        //得到Call
        Call call = client.newCall(request);
        //执行请求
        call.enqueue(callback);

    }


    /**
     * 请求返回的结果是json字符串 * * @param jsonValue * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final OKCallback callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onSuccess(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onProcessMethod(final int i, final OKCallback callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onProcess(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void onFailJsonStringMethod(final String str, final OKCallback callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onFail(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface OKCallback {
        void onSuccess(String result);

        void onProcess(int i);

        void onFail(String failResult);
    }

    /**
     * 拼接get的url * * @param params * @return
     */
    private String makeGetUrl(String oldUrl, HashMap<String, String> params) {
        String url = oldUrl;
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }
}