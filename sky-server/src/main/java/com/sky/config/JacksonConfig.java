package com.sky.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 全局配置序列化返回 JSON 处理
        SimpleModule simpleModule = new SimpleModule();
        //JSON Long ==> String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);

        // 注册自定义日期序列化器
        simpleModule.addSerializer(Date.class, new DateSerializer());

        objectMapper.registerModule(simpleModule);

        // 设置全局日期格式和时区
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return objectMapper;
    }
    
    // 自定义日期序列化器
    public static class DateSerializer extends JsonSerializer<Date> {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        static {
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        }

        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(dateFormat.format(date));
        }
    }
}