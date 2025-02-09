package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.model.PostPreview;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostPreviewRepository implements PostPreviewRepository {

    private static final int BODY_PREVIEW_LIMIT = 60;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PostPreview> postPreviewRowMapper = (rs, rowNum) -> new PostPreview(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("body_preview"),
            List.of(), // TODO implement
//            Arrays.stream(Optional.ofNullable(rs.getString("tags")).orElse("").split(",")).toList(),
            rs.getInt("comments_count"),
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public List<PostPreview> findAll(Integer limit, Integer offset) {
        // TODO сортировка тегов в алфавитном порядке
        String sql = """
                select id, title, substr(body, 0, ?) as body_preview, likes, created, updated,
                (select string_agg(post_tag.tag, ', ') from post_tag where post_tag.post_id = post.id group by id) as tags,
                (select count(*) from post_comment where post_comment.post_id = post.id) as comments_count
                from post
                    order by created desc
                limit ? offset ?
                """;

        return jdbcTemplate.query(sql, postPreviewRowMapper, BODY_PREVIEW_LIMIT, limit, offset);
    }

    @Override
    public List<PostPreview> findAllByTag(String tag, Integer limit, Integer offset) {
        String sql = """
                select id, title, substr(body, ?), likes, created, updated,
                (select array_agg(post_tag.tag) from post_tag where post_tag.post_id = post.id) as tags
                from post
                    join post_tag on post.id = post_tag.post_id
                    where post_tag.tag = ?
                    order by created desc
                limit ? offset ?
                """;

        return jdbcTemplate.query(sql, postPreviewRowMapper, BODY_PREVIEW_LIMIT, tag, limit, offset);
    }
}
