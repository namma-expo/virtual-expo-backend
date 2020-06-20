package com.nammaexpo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("singleton")
public class SerDe {

    private static ObjectMapper mapper;

    private SerDe() {

    }

    public static void init() {
        mapper = new ObjectMapper();
    }

    public static ObjectMapper mapper() {
        Preconditions.checkNotNull(mapper, "Mapper is null");
        return mapper;
    }

    public static <T> String writeValueAsString(T t) {
        try {
            return mapper().writeValueAsString(t);
        } catch (Exception ex) {
            log.error("Serialization fail - {}", ex);
            throw ExpoException.error(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}
