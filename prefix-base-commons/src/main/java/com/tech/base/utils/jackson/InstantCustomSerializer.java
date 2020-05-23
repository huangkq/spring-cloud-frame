package com.tech.base.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InstantCustomSerializer extends JsonSerializer<Instant> {

    private DateTimeFormatter format;

    public InstantCustomSerializer(DateTimeFormatter formatter) {
        this.format = formatter;
    }

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (instant == null) {
            return;
        }
        String jsonValue = format.format(instant.atZone(ZoneId.systemDefault()));
        jsonGenerator.writeString(jsonValue);
    }

}
