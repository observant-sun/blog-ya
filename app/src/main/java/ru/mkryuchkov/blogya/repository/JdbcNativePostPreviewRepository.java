package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.entity.PostPreview;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostPreviewRepository implements PostPreviewRepository {

    private static final int BODY_PREVIEW_LIMIT = 60;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PostPreview> postPreviewRowMapper = (rs, rowNum) -> new PostPreview(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("body_preview"),
            rs.getString("image_uuid"),
            rs.getString("tags"),
            rs.getInt("comments_count"),
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public List<PostPreview> findAll(Integer limit, Long offset) {
        String sql = """
                select id, title, substr(body, 0, ?) as body_preview, likes, created, updated, image_uuid,
                (select string_agg(post_tag.tag, ', ') from post_tag where post_tag.post_id = post.id group by id) as tags,
                (select count(*) from post_comment where post_comment.post_id = post.id) as comments_count
                from post
                    order by created desc
                limit ? offset ?
                """;

        return jdbcTemplate.query(sql, postPreviewRowMapper, BODY_PREVIEW_LIMIT, limit, offset);
    }

    @Override
    public List<PostPreview> findAllByTag(String tag, Integer limit, Long offset) {
        String sql = """
                select id, title, substr(body, ?), likes, created, updated, image_uuid,
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
