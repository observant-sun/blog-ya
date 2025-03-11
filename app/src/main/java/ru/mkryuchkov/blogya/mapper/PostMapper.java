package ru.mkryuchkov.blogya.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.Post;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PostMapper {

    @Mapping(target = "imageUuid", source = "imageUuid")
    Post toEntity(PostDto dto, String imageUuid);
    PostDto toDto(Post entity, String tags);

}
