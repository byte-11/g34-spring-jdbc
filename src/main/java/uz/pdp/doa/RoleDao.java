package uz.pdp.doa;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.pdp.domain.UserRole;

@Repository
public class RoleDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RoleDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<UserRole> getRolesByUserId(final Long userId) {
        return namedParameterJdbcTemplate.query(
                """
                        SELECT r.id, r.name FROM roles r
                        INNER JOIN user_roles ur ON r.id = ur.role_id
                        WHERE ur.user_id = :userId
                     """,
                new MapSqlParameterSource()
                .addValue("userId", userId),
                BeanPropertyRowMapper.newInstance(UserRole.class)
        );
    }
}
