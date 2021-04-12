package io.infinitestrike.flatpixel.json;

import com.google.gson.*;

public class JSON {

    public static JsonObject parse(String s){
        JsonElement element = JsonParser.parseString(s);
        return  element.getAsJsonObject();
    }

    public static <T> T parse(String s, Class<T> clazz){
        return new Gson().fromJson(s,clazz);
    };

    public static String stringify(Object o){
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return json;
    }
}
