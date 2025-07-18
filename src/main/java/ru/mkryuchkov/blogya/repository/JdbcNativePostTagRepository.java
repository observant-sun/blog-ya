package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostTagRepository implements PostTagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void rewriteAllTagsForPost(Long postId, List<String> tags) {
        jdbcTemplate.update(
                "delete from post_tag where post_id = ?",
                postId);
        List<Object[]> batchUpdateArgs = Optional.ofNullable(tags).orElse(Collections.emptyList()).stream()
                .map(tag -> new Object[]{postId, tag})
                .toList();
        if (batchUpdateArgs.isEmpty()) {
            return;
        }
        jdbcTemplate.batchUpdate(
                "insert into post_tag (post_id, tag) values (?, ?)",
                batchUpdateArgs
        );
    }

    @Override
    public List<String> getTagsForPost(Long postId) {
        return jdbcTemplate.query(
                "select tag from post_tag where post_id = ? order by tag",
                new Object[]{postId},
                new int[]{Types.BIGINT},
                (rs, rowNum) -> rs.getString("tag")
        );
    }

    @Override
    public void deleteAllTagsForPost(Long postId) {
        jdbcTemplate.update(
                "delete from post_tag where post_id = ?",
                postId);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from post_tag");
    }
}
