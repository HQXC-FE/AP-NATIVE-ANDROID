package com.xtree.service.message;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RemoteMessageDeserializer implements JsonDeserializer<RemoteMessage> {

    @Override
    public RemoteMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        int fd = jsonObject.get("fd").getAsInt();
        long timestamp = jsonObject.get("timestamp").getAsLong();

        List<MessageData> dataList = new ArrayList<>();
        JsonElement dataElement = jsonObject.get("data");

        if (dataElement.isJsonObject()) {
            // 如果 `data` 是对象
            MessageData data = context.deserialize(dataElement, MessageData.class);
            dataList.add(data);
        } else if (dataElement.isJsonArray()) {
            // 如果 `data` 是数组
            for (JsonElement element : dataElement.getAsJsonArray()) {
                MessageData data = context.deserialize(element, MessageData.class);
                dataList.add(data);
            }
        }

        return new RemoteMessage(type, fd, timestamp, dataList);
    }
}

