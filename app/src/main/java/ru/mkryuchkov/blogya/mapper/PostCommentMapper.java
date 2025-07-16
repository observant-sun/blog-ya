package ru.mkryuchkov.blogya.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.entity.PostComment;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PostCommentMapper {

    PostCommentDto toDto(PostComment postComment);
    PostComment toEntity(PostCommentDto postCommentDto, Long postId);
    @Mapping(target = "id", source = "commentId")
    PostComment toEntity(PostCommentDto postCommentDto, Long postId, Long commentId);

}
