package com.studyorganizer.userstokens.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.studmodel.User;
import com.studyorganizer.userstokens.serializers.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GsonConfig {

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }
}
