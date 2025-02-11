package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.model.Post;

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
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("body"),
            rs.getString("tags"),
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public Optional<Post> findById(Integer id) {
        String sql = "select * from post where id = ?";
        List<Post> postList = jdbcTemplate.query(sql, postRowMapper, id);
        return postList.stream().findFirst();
    }

    @Override
    public void saveNew(Post post) {
        Timestamp now = Timestamp.from(Instant.now());

        String title = post.title();
        String body = post.body();
        String tags = post.tags();
        int likes = 0;

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into post(title, body, likes, tags, created, updated) values (?, ?, ?, ?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, body);
            ps.setInt(3, likes);
            ps.setString(4, tags);
            ps.setTimestamp(5, now);
            ps.setTimestamp(6, now);
            return ps;
        });
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "select count(*) from post where id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void update(Post post) {
        Optional<Post> byId = findById(post.id());
        if (byId.isEmpty()) {
            throw new RuntimeException("Post not found");
        }

        Timestamp now = Timestamp.from(Instant.now());

        String title = post.title();
        String body = post.body();
        String tags = post.tags();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                    update post
                        set title = ?, body = ?, tags = ?, updated = ?
                        where id = ?
                    """);
            ps.setString(1, title);
            ps.setString(2, body);
            ps.setString(3, tags);
            ps.setTimestamp(4, now);
            ps.setInt(5, post.id());
            return ps;
        });
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "delete from post where id = ?";

        jdbcTemplate.update(sql, id);
    }
}
