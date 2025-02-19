package com.xtree.live.uitl;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;

public class JsonUtil {
    public static JsonArray getJsonArray(JsonObject data, String key) {
        if (data != null) {
            try {
                JsonElement element = data.get(key);
                if (element == null) return new JsonArray();
                if (element.isJsonArray()) {
                    return element.getAsJsonArray();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new JsonArray();
    }

    public static JsonObject getJsonObject(JsonObject data, String key) {
        if (data != null) {
            try {
                JsonElement element = data.get(key);
                if (element == null) return new JsonObject();
                if (element.isJsonObject()) {
                    return element.getAsJsonObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new JsonObject();
    }

    public static int getInt(JsonObject data, String key, int defaultValue) {
        if (data != null) {
            try {
                JsonElement element = data.get(key);
                if (element == null) return defaultValue;
                if (element.isJsonPrimitive() && !element.isJsonNull()) {
                    return element.getAsInt();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static boolean getBoolean(JsonObject data, String key, boolean defaultValue) {
        if (data != null) {
            try {
                JsonElement element = data.get(key);
                if (element == null) return defaultValue;
                if (element.isJsonPrimitive() && !element.isJsonNull()) {
                    return element.getAsBoolean();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultValue;
    }

    public static String getString(JsonObject data, String key) {
        if (data != null) {
            try {
                JsonElement element = data.get(key);
                if (element == null) return "";
                if (element.isJsonPrimitive() && !element.isJsonNull()) {
                    return element.getAsString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean isJsonObject(String data) {
        if (data == null) return false;
        try {
            Object json = new JSONTokener(data).nextValue();
            return json instanceof JSONObject;
        } catch (JSONException e) {
            return false;
        }
    }

    public static boolean isJsonArray(String data) {
        if (data == null) return false;
        try {
            Object json = new JSONTokener(data).nextValue();
            return json instanceof JSONArray;
        } catch (JSONException e) {
            return false;
        }
    }

    public static @Nullable <T> T fromJson(Gson gson, String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonIOException | JsonSyntaxException e) {
            return null;
        }
    }

    public static <T> @Nullable T fromJson(Gson gson, String json, TypeToken<T> typeOfT){
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonIOException | JsonSyntaxException e) {
            return null;
        }
    }

    public static <T> @Nullable T fromJson(Gson gson, String json, Type typeOfT){
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonIOException | JsonSyntaxException e) {
            return null;
        }
    }


    public static @Nullable String toJson(Gson gson, Object src) {
        try {
            return gson.toJson(src);
        } catch (JsonIOException e) {
            return null;
        }
    }
}
