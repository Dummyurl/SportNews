package com.kunleen.sn.sportnewsapplication.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ysy on 2018/1/29.
 */

public class JsonUtils {
    private static Gson gson = new Gson();


    public static <T> List<T> getJsonArray(String jsonStr, Class<T> classOfT) {
        ArrayList<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(jsonStr);
        JsonArray jsonArray = null;
        if (el.isJsonArray()) {
            jsonArray = el.getAsJsonArray();
        }
        Iterator it = jsonArray.iterator();
        while (it.hasNext()) {
            JsonElement e = (JsonElement) it.next();
            //JsonElement转换为JavaBean对象
            list.add(gson.fromJson(e, classOfT));
        }
        return list;
    }
}
