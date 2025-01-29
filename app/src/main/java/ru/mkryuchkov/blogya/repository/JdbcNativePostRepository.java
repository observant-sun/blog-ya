package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.model.Post;

import java.util.Arrays;
import java.util.Collections;
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
            Arrays.stream(Optional.ofNullable(rs.getString("tags")).orElse("").split(",")).toList(),
            rs.getInt("likes"),
            rs.getTimestamp("created"),
            rs.getTimestamp("updated")
    );

    @Override
    public List<Post> findAll(Integer limit, Integer offset) {
        String sql = "select * from post order by created desc limit ? offset ?";

        return jdbcTemplate.query(sql, postRowMapper, limit, offset);
    }

    @Override
    public Optional<Post> findById(Integer id) {
        String sql = "select * from post where id = ?";
        List<Post> postList = jdbcTemplate.query(sql, postRowMapper, id);
        return postList.stream().findFirst();
    }

    @Override
    public void save(Post post) {
        String sql = "insert into post(id, title, body, tags, likes, created, updated) values (?, ?, ?, ?, ?, ?, ?)";

        String tags = String.join(",", Optional.ofNullable(post.tags()).orElse(Collections.emptyList()));
        jdbcTemplate.update(sql, post.id(), post.title(), post.body(), tags, post.likes(), post.created(), post.updated());
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "delete from post where id = ?";

        jdbcTemplate.update(sql, id);
    }
}
