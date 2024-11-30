package com.studyorganizer.googleschedule.serializers;

import com.google.gson.*;
import com.studmodel.Schedule;
import com.studmodel.User;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ListOfScheduleDeserializer implements Deserializer<List<Schedule>> {

    private final Gson gson;

    public ListOfScheduleDeserializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new JsonDeserializer<Instant>() {
                    @Override
                    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return Instant.parse(json.getAsString());
                    }
                })
                .create();
    }

    @Override
    public List<Schedule> deserialize(String topic, byte[] data) {
        String json = new String(data);
        Schedule[] schedule = gson.fromJson(json, Schedule[].class);
        return Arrays.asList(schedule);
    }

}
