package com.studyorganizer.userstokens.serializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write ( JsonWriter jsonWriter , Instant instant ) throws IOException
    {
        jsonWriter.value( instant.toString() );  // Writes in standard ISO 8601 format.
    }

    @Override
    public Instant read ( JsonReader jsonReader ) throws IOException
    {
        return Instant.parse( jsonReader.nextString() );   // Parses standard ISO 8601 format.
    }
}
