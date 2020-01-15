package com.tech.base.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.tech.base.utils.JacksonUtil;
import com.tech.base.utils.date.DateFormatUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class JacksonConfig {

    private static final Logger logger = LoggerFactory.getLogger(JacksonConfig.class);

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setTimeZone(DateFormatUtil.DEFAULT_TIME_ZONE);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setDateFormat(new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_FULL));

        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Instant.class,
                new InstantCustomSerializer(DateTimeFormatter.ofPattern(DateFormatUtil.DATE_FORMAT_FULL, Locale.SIMPLIFIED_CHINESE)));
        javaTimeModule.addDeserializer(Instant.class, new InstantCustomDeserializer());

        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateFormatUtil.DATE_FORMAT_FULL, Locale.SIMPLIFIED_CHINESE)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatUtil.DATE_FORMAT_FULL, Locale.SIMPLIFIED_CHINESE)));

        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DateFormatUtil.DATE_FORMAT_SHORT, Locale.SIMPLIFIED_CHINESE)));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateFormatUtil.DATE_FORMAT_SHORT, Locale.SIMPLIFIED_CHINESE)));

        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_FULL)));
        javaTimeModule.addDeserializer(Date.class, new DateCustomDeserializer());

        objectMapper.registerModule(javaTimeModule);

        logger.info("==========jackson objectMapper init==========");
        JacksonUtil.setMapper(objectMapper);
        return objectMapper;
    }

    static class InstantCustomSerializer extends JsonSerializer<Instant> {
        private DateTimeFormatter format;

        private InstantCustomSerializer(DateTimeFormatter formatter) {
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

    static class InstantCustomDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateString = p.getText().trim();
            if (StringUtils.isNotBlank(dateString)) {
                try {
                    Date pareDate = (new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_FULL)).parse(dateString);
                    if (null != pareDate) {
                        return pareDate.toInstant();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    static class DateCustomDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateString = p.getText().trim();
            if (StringUtils.isNotBlank(dateString)) {
                try {
                    return (new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_FULL)).parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
