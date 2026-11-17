package com.example.dockercompose.repository;

import com.example.dockercompose.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name FROM users ORDER BY id",
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("name")
                )
        );
    }
}
