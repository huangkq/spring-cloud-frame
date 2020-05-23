package com.tech.base.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.tech.base.utils.date.DateFormatUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Instant;

public class InstantCustomDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText().trim();
        if (StringUtils.isBlank(dateString)) return null;

        if (dateString.length() > DateFormatUtil.DATE_FORMAT_SHORT.length()) {
            return DateFormatUtil.stringToDate(dateString, DateFormatUtil.DATE_FORMAT_FULL).toInstant();
        } else {
            return DateFormatUtil.stringToDate(dateString, DateFormatUtil.DATE_FORMAT_SHORT).toInstant();
        }
    }
}
