package ru.mkryuchkov.blogya.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PostTagMapper {

    default List<String> toList(String tags) {
        return Optional.ofNullable(tags)
                .map(str -> str.split(","))
                .map(Arrays::asList)
                .orElse(List.of()).stream()
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .collect(Collectors.toList());
    }

    default String toCommaDelimitedString(List<String> tags) {
        return Optional.ofNullable(tags)
                .map(str -> String.join(", ", str))
                .orElse("");
    }
}
