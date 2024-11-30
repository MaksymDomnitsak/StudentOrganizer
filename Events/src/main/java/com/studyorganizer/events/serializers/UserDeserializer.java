package com.studyorganizer.events.serializers;

import com.google.gson.*;
import com.studmodel.User;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserDeserializer implements Deserializer<List<User>> {

    private final Gson gson;

    public UserDeserializer() {
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
    public List<User> deserialize(String topic, byte[] data) {
        String json = new String(data);
        User[] usersArray = gson.fromJson(json, User[].class);
        return Arrays.asList(usersArray);
    }

}
