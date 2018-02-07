package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * UserDao - 1장은 DB connection 관심사를 어떻게 분리할 것인가에 대해 다루면서 조금씩 코드가 개선 중이다.
 */
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate();
        this.jdbcTemplate.setDataSource(dataSource);
    }

    private final RowMapper<User> userMapper = new RowMapper<User>() {
        @Nullable
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            return user;
        }
    };

    public void add(final User user) {
        this.jdbcTemplate.update(
                "insert into users(id, name, password, level, login, recommend) " +
                        "values(?, ?, ?, ?, ?, ?)", user.getId(), user.getName(),
                user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        });
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                "update users set name = ?, password = ?, level = ?, login = ?, " +
                        "recommend = ? where id = ? ", user.getName(), user.getPassword(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

}
