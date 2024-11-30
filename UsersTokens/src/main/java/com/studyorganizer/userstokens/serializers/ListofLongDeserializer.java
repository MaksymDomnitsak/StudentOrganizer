package com.studyorganizer.userstokens.serializers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.util.List;

public class ListofLongDeserializer implements Deserializer<List<Long>> {
    private final Gson gson = new Gson();

    public ListofLongDeserializer() {
    }

    @Override
    public List<Long> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        String json = new String(data);

        // Десеріалізуємо JSON у List<Long>
        Type listType = new TypeToken<List<Long>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
