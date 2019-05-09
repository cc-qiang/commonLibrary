package com.chen.library.json;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * @author DELL
 */
public class GsonFormat {
    private static final Gson GSON = new Gson();

    public static String map2Json(HashMap<String, String> map) {
        return GSON.toJson(map);
    }

    public static <T> String bean2Json(T bean) {
        return GSON.toJson(bean);
    }

    public static <T> T json2Object(String json, Class<T> c) {
        if (!TextUtils.isEmpty(json)) {
            return GSON.fromJson(json, c);
        }
        return null;
    }
}
