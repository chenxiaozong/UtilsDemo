package com.chenlibrary.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComTools {
    /**
     * 使用Gson将Json字符串转换为对象
     * 将字符串转换为 对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T JsonToObject(String json, Class<T> type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (Throwable e) {
            e.printStackTrace();
            ChenLog.i("JsonToObject: json:", json, " error:", e.getMessage());
            return null;
        }

    }

    public static String parseArray(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append("[" + i + "]").append(array[i]).append(" ");
        }
        return sb.toString();
    }

    public static String mapToString(String name, Map<?, ?> map) {
        StringBuilder result = new StringBuilder(name).append(">>>>>>>>:\n");

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            result.append(entry.getKey()).append(":").append(entry.getValue()).append("  ");
        }
        result.append("\n----------->end");
        return result.toString();
    }


    public static String listMapToString(String name, List<Map<String, String>> lisMap) {
        StringBuilder result = new StringBuilder(name).append(">：");

        for (Map<String, String> map : lisMap) {
            String s = mapToString("", map);
            result.append(s);
        }




        return result.toString();
    }

    private static final Map<String, Object> values = new HashMap<>();

    public static  <T> void put( String key, T value, Class<T> valueType ) {
        values.put( key, value );
    }

    public static  <T> T get( String key, Class<T> valueType ) {
        return ( T )values.get( key );
    }




}
