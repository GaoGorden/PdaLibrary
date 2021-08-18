package p.gorden.pdalibrary.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: GordenGao
 * @Email: gordengao124@gmail.com
 * @date: 2021年06月27日 17:32
 */
public class JsonUtils {

    /**
     * 解析没有数据头的纯数组
     */
    public static <T> ArrayList<T> parseNoHeaderJArray(String strJsonArray, Class<T> clz) {

        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = JsonParser.parseString(strJsonArray).getAsJsonArray();

        Gson gson = new Gson();
        ArrayList<T> arrayList = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            T t = gson.fromJson(user, clz);
            arrayList.add(t);
        }
        return arrayList;
    }
}
