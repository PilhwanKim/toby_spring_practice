package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.*;

/**
 * UserDao - 1장은 DB connection 관심사를 어떻게 분리할 것인가에 대해 다루면서 조금씩 코드가 개선 중이다.
 */
public class UserDao {

    private DataSource dataSource;

    public void add(User user) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch(SQLException e) {
            throw e;
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e){ }
                /*
                ps.close() 메소드에도 SQLException이 발생할 수 있기 때문에 이를 잡아줘야 한다.
                그렇지 않으면 Connection close() 하지 못하고 메소드를 빠져나가게 된다.
                */
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e){ }
            }
        }
    }

    public User get(String id) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select * from users where id = ?");
            ps.setString(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if(user == null) throw new EmptyResultDataAccessException(1);

            return user;
        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e){ }
                /*
                rs.close() 메소드에도 SQLException이 발생할 수 있기 때문에 이를 잡아줘야 한다.
                그렇지 않으면 Connection close() 하지 못하고 메소드를 빠져나가게 된다.
                */
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e){ }
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e){ }
            }
        }

    }

    public void deleteAll() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("delete from users");
            ps.executeUpdate();
        } catch (SQLException e){
            throw e;
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e){ }
                /*
                ps.close() 메소드에도 SQLException이 발생할 수 있기 때문에 이를 잡아줘야 한다.
                그렇지 않으면 Connection close() 하지 못하고 메소드를 빠져나가게 된다.
                */
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e){ }
            }
        }

    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e){ }
                /*
                rs.close() 메소드에도 SQLException이 발생할 수 있기 때문에 이를 잡아줘야 한다.
                그렇지 않으면 Connection close() 하지 못하고 메소드를 빠져나가게 된다.
                */
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e){ }
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e){ }
            }
        }
    }

    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }
}
