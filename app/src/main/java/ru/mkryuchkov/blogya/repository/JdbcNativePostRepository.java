package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.dto.PostDto;
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
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "select id, title, body, tags, likes, created, updated from post where id = ?";
        List<Post> postList = jdbcTemplate.query(sql, postRowMapper, id);
        return postList.stream().findFirst();
    }

    @Override
    public void saveNew(Post post) {
        if (post.id() != null) {
            throw new IllegalArgumentException("Post id is already set");
        }
        Timestamp now = Timestamp.from(Instant.now());

        String title = post.title();
        String body = post.body();
        int likes = 0;

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into post(title, body, likes, created, updated) values (?, ?, ?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, body);
            ps.setInt(3, likes);
            ps.setTimestamp(4, now);
            ps.setTimestamp(5, now);
            return ps;
        });
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        String sql = "select count(*) from post where id = ?";
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

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                    update post
                        set title = ?, body = ?, updated = ?
                        where id = ?
                    """);
            ps.setString(1, title);
            ps.setString(2, body);
            ps.setTimestamp(3, now);
            ps.setLong(4, post.id());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from post where id = ?";

        jdbcTemplate.update(sql, id);
    }
}
