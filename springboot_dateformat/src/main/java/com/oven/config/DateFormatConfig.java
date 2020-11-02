package com.oven.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@JsonComponent
public class DateFormatConfig {

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {
        return builder -> {
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            DateFormat format = new SimpleDateFormat(PATTERN);
            format.setTimeZone(timeZone);
            builder.failOnEmptyBeans(false)
                    .failOnUnknownProperties(false)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .dateFormat(format);
        };
    }

    @Bean
    public LocalDateTimeSerializer localDateTimeDeserializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(PATTERN));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(@Qualifier("localDateTimeDeserializer") LocalDateTimeSerializer localDateTimeDeserializer) {
        return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer);
    }

}
