package com.shivachi.pacs.demo.auth.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.shivachi.pacs.demo.auth.data.role.AccessRight;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.SneakyThrows;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Converter
public class AccessRightConverter implements AttributeConverter<List<AccessRight>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(List<AccessRight> accessRights) {
        return accessRights != null && !accessRights.isEmpty() ? this.objectMapper.writeValueAsString(accessRights) : null;
    }

    @SneakyThrows
    @Override
    public List<AccessRight> convertToEntityAttribute(String s) {
//        return s != null && !s.isEmpty() ? this.objectMapper.readValue(s.trim(), new TypeReference<>() {}) : new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return null;
        }

        String[] accessRightNames = this.objectMapper.readValue(s.trim(), String[].class);
        return Arrays.stream(accessRightNames)
                .filter(val ->{
                    AccessRight right = AccessRight.fromString(val);
                    return !Objects.isNull(right);
                }).map(AccessRight::fromString).collect(Collectors.toList());
    }
}
