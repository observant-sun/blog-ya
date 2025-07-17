package ru.mkryuchkov.blogya;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.mkryuchkov.blogya.mapper.PostCommentMapper;
import ru.mkryuchkov.blogya.mapper.PostMapper;
import ru.mkryuchkov.blogya.mapper.PostPreviewMapper;
import ru.mkryuchkov.blogya.mapper.PostTagMapper;
import ru.mkryuchkov.blogya.repository.*;

@Configuration
@ComponentScan({"ru.mkryuchkov.blogya.service"})
public class ServiceTestConfig {

    @Bean
    @Primary
    public PostRepository postRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    @Primary
    public FileRepository fileRepository() {
        return Mockito.mock(FileRepository.class);
    }

    @Bean
    @Primary
    public PostCommentRepository postCommentRepository() {
        return Mockito.mock(PostCommentRepository.class);
    }

    @Bean
    @Primary
    public PostPreviewRepository postPreviewRepository() {
        return Mockito.mock(PostPreviewRepository.class);
    }

    @Bean
    @Primary
    public PostTagRepository postTagRepository() {
        return Mockito.mock(PostTagRepository.class);
    }

    @Bean
    @Primary
    public PostCommentMapper postCommentMapper() {
        return Mockito.mock(PostCommentMapper.class);
    }

    @Bean
    @Primary
    public PostMapper postMapper() {
        return Mockito.mock(PostMapper.class);
    }

    @Bean
    @Primary
    public PostPreviewMapper postPreviewMapper() {
        return Mockito.mock(PostPreviewMapper.class);
    }

    @Bean
    @Primary
    public PostTagMapper postTagMapper() {
        return Mockito.mock(PostTagMapper.class);
    }

}
