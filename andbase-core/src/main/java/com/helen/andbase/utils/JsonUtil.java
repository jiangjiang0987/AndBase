package com.helen.andbase.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/***
 * JSON转换工具类
 */
public class JsonUtil {

	/**
	 * 将对象转换成json格式
	 * 
	 */
	public static String objectToJson(Object ts) {
		Gson gson = new Gson();
		return gson.toJson(ts);
	}


	/**
	 * 将json格式转换成list对象，并准确指定类型
	 */
	public static <T> List<T> json2List(String json, Class<T> clazz) {
		if(!TextUtils.isEmpty(json)) {
			Gson gson = new Gson();
			try {
				List<JsonObject> list = gson.fromJson(json, new TypeToken<List<JsonObject>>() {}.getType());
				if (list != null) {
					List<T> resultList = new ArrayList<>();
					for (JsonObject t : list) {
						resultList.add(gson.fromJson(t, clazz));
					}
					return resultList;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将json格式转换成map对象
	 * 
	 */
	public static Map<?, ?> jsonToMap(String jsonStr) {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<?, ?>>() {}.getType();
		return gson.fromJson(jsonStr, type);
	}

	/**
	 * 将json转换成bean对象
	 * 
	 */
	public static Object jsonToBean(String jsonStr, Class<?> cl) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, cl);
	}
}
