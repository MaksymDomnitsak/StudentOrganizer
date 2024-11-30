package com.studyorganizer.userstokens.serializers;

import com.google.gson.*;
import com.studmodel.User;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class UserSerializer implements Serializer<List<User>> {

    private final Gson gson;

    public UserSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new JsonSerializer<Instant>() {
                    @Override
                    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString());
                    }
                })
                .create();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, List<User> data) {
        return gson.toJson(data).getBytes();
    }

    @Override
    public void close() {
    }
}