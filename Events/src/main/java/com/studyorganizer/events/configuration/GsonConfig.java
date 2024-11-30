package com.studyorganizer.events.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.studyorganizer.events.serializers.*;


import java.time.Instant;

public class GsonConfig {

    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Instant.class, new InstantAdapter());
        return builder.create();
    }
}


