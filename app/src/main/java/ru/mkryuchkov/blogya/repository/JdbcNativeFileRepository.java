//package ru.mkryuchkov.blogya.repository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import ru.mkryuchkov.blogya.entity.FileEntity;
//
//import java.sql.PreparedStatement;
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//@RequiredArgsConstructor
//public class JdbcNativeFileRepository implements FileRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Override
//    public FileEntity saveNewFile(byte[] content) {
//        String uuid = UUID.randomUUID().toString();
//        jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(
//                    "insert into file (id, content) values (?, ?)");
//            ps.setString(1, uuid);
//            ps.setBytes(2, content);
//            return ps;
//        });
//        return new FileEntity(uuid, content);
//    }
//
//    @Override
//    public Optional<FileEntity> getById(String id) {
//        FileEntity fileEntity = jdbcTemplate.queryForObject(
//                "select content from file where id = ?",
//                (rs, rowNum) -> new FileEntity(
//                        rs.getString("id"),
//                        rs.getBytes("content")
//                ),
//                id
//        );
//        return Optional.ofNullable(fileEntity);
//    }
//}
