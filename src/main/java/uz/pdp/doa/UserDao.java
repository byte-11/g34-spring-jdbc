package uz.pdp.doa;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import uz.pdp.domain.User;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public UserDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    public void saveUser(final User user) {
        /*KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(
                            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)",
                            new String[] {"id", "username", "password"}
                    );
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.getGeneratedKeys();
            return preparedStatement;
        };
        jdbcTemplate.update(psc, keyHolder);

        Map<String, Object> keys = keyHolder.getKeys();
        System.out.println("generated id:" + keys.get("id"));
        System.out.println("inserted username: " + keys.get("username"));
        System.out.println("inserted password: " + keys.get("password"));*/
        /*jdbcTemplate.update(
                "INSERT INTO users(username, email, password) VALUES (?, ?, ?)",
                user.getUsername(), user.getEmail(), user.getPassword()
        );*/
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", user.getUsername())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword());
        namedParameterJdbcTemplate.update(
                "INSERT INTO users(username, email, password) VALUES (:username, :email, :password)",
                params
        );


    }

    public void updateUser(final User user) {
        /*jdbcTemplate.update(
                "UPDATE users SET username = ? , email = ? , password = ? WHERE id = ?",
                user.getUsername(), user.getEmail(), user.getPassword(), user.getId()
        );*/
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "UPDATE users SET username = :username , email = :email , password = :password WHERE id = :id",
                new BeanPropertySqlParameterSource(user),
                keyHolder,
                new String[]{"id", "username", "email", "password"}
        );
        Map<String, Object> keys = keyHolder.getKeys();
        System.out.println("generated id:" + keys.get("id"));
        System.out.println("inserted username: " + keys.get("username"));
        System.out.println("inserted password: " + keys.get("password"));

    }

    public void deleteUser(final Long id) {
        jdbcTemplate.update(
                "DELETE FROM users WHERE id = ?",
                id
        );
    }

    public User findUserById(final Long id) {
        /*RowMapper<User> rowMapper = (rs, rowNum) -> {
            return User.builder()
                    .id(rs.getLong("id"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .build();
        };*/
        return jdbcTemplate.queryForObject(
                "SELECT u.id, u.username, u.email, u.password FROM users u WHERE u.id = ?",
                BeanPropertyRowMapper.newInstance(User.class),
                id
        );
    }

    public List<User> findAllUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                BeanPropertyRowMapper.newInstance(User.class)
        );
    }

    public void saveWithSimpleJdbc(final User user) {
        SimpleJdbcInsert insert = simpleJdbcInsert.withTableName("users")
                .usingColumns("username", "email");
        insert.setGeneratedKeyNames("id", "username", "password");

        KeyHolder keyHolder = insert.executeAndReturnKeyHolder(new BeanPropertySqlParameterSource(user));

        Map<String, Object> keys = keyHolder.getKeys();
        System.out.println("generated id:" + keys.get("id"));
        System.out.println("inserted username: " + keys.get("username"));
        System.out.println("inserted password: " + keys.get("password"));
    }


}
