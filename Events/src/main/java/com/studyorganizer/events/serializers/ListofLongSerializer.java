package com.studyorganizer.events.serializers;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.util.List;

import static com.studyorganizer.events.configuration.GsonConfig.getGson;

public class ListofLongSerializer implements Serializer<List<Long>> {
    private final Gson gson = getGson();

    public ListofLongSerializer() {
    }

    @Override
    public byte[] serialize(String topic, List<Long> data) {
        if (data == null) {
          return null;
        }
        String json = gson.toJson(data);
        return json.getBytes();
    }
}
