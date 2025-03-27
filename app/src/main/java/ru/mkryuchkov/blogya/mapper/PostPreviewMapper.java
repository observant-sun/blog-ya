package ru.mkryuchkov.blogya.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.PostPreview;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PostPreviewMapper {

    PostPreviewDto toDto(PostPreview postPreview);

}
