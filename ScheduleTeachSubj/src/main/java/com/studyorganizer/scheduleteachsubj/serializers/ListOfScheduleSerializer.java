package com.studyorganizer.scheduleteachsubj.serializers;

import com.google.gson.*;
import com.studmodel.Schedule;
import com.studmodel.User;
import org.apache.kafka.common.serialization.Serializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ListOfScheduleSerializer implements Serializer<List<Schedule>> {

    private final Gson gson;

    public ListOfScheduleSerializer() {
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
    public byte[] serialize(String topic, List<Schedule> data) {
        return gson.toJson(data).getBytes();
    }

}
