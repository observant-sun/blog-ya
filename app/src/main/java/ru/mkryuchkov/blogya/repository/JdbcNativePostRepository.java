package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.entity.Post;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> new Post(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getString("image_uuid"),
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );
    private final ResultSetExtractor<Post> postResultSetExtractor = rs -> new Post(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getString("image_uuid"),
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "select id, title, body, image_uuid, likes, created, updated from post where id = ?";
        List<Post> postList = jdbcTemplate.query(sql, postRowMapper, id);
        return postList.stream().findFirst();
    }

    @Override
    public Long saveNew(Post post) {
        if (post.id() != null) {
            throw new IllegalArgumentException("Post id is already set");
        }
        Timestamp now = Timestamp.from(Instant.now());

        String title = post.title();
        String body = post.body();
        String imageUuid = post.imageUuid();
        int likes = 0;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into post(title, body, image_uuid, likes, created, updated) values (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, body);
            ps.setString(3, imageUuid);
            ps.setInt(4, likes);
            ps.setTimestamp(5, now);
            ps.setTimestamp(6, now);
            return ps;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        String sql = "select count(1) from post where id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void update(Post post) {
        if (!existsById(post.id())) {
            throw new RuntimeException("PostDto not found");
        }

        Timestamp now = Timestamp.from(Instant.now());

        String title = post.title();
        String body = post.body();
        String imageUuid = post.imageUuid();
        int likes = Optional.ofNullable(post.likes()).orElse(0);

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                            update post
                                set title = ?, body = ?, image_uuid = ?, likes = ?, updated = ?
                                where id = ?
                            """);
            ps.setString(1, title);
            ps.setString(2, body);
            ps.setString(3, imageUuid);
            ps.setInt(4, likes);
            ps.setTimestamp(5, now);
            ps.setLong(6, post.id());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from post where id = ?";

        jdbcTemplate.update(sql, id);
    }
}
