package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.entity.PostComment;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostCommentRepository implements PostCommentRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PostComment> postCommentRowMapper = (rs, rowNum) -> new PostComment(
            rs.getLong("id"),
            rs.getLong("post_id"),
            rs.getString("text"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public List<PostComment> findAllByPostId(Long postId) {
        String sql = """
                select id, post_id, "text", created, updated
                from post_comment
                where post_id = ?
                    order by created desc
                """;

        return jdbcTemplate.query(sql, postCommentRowMapper, postId);
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        String sql = "select exists ( select * from post_comment where id = ? )";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Boolean.class, id))
                .orElse(false);
    }

    @Override
    public void saveNew(PostComment postComment) {
        if (postComment.id() != null) {
            throw new IllegalArgumentException("id is already set");
        }
        Timestamp now = Timestamp.from(Instant.now());

        Long postId = postComment.postId();
        String text = postComment.text();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into post_comment(post_id, \"text\", created, updated) values (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setLong(1, postId);
            ps.setString(2, text);
            ps.setTimestamp(3, now);
            ps.setTimestamp(4, now);
            return ps;
        });
    }

    @Override
    public void update(PostComment postComment) {
        if (!existsById(postComment.id())) {
            throw new RuntimeException("comment not found by id " + postComment.id());
        }
        Timestamp now = Timestamp.from(Instant.now());

        String text = postComment.text();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                            update post_comment
                                set "text" = ?, updated = ?
                            where id = ?
                            """);
            ps.setString(1, text);
            ps.setTimestamp(2, now);
            ps.setLong(3, postComment.id());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from post_comment where id = ?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from post_comment");
    }
}
