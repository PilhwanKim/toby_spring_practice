package springbook.user.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by pilhwankim on 15/12/2017.
 */
public class UserDaoTest {
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        ApplicationContext ac = new GenericXmlApplicationContext("applicationContext.xml");
        this.dao = ac.getBean("userDao", UserDao.class);

        this.user1 = new User("pilhwankim", "김필환", "secret2@");
        this.user2 = new User("leegm700", "이길원", "springno1");
        this.user3 = new User("bumjin", "박범진", "springno2");
    }

    @Test
    public void addAndGet() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));


        dao.add(user1);
        dao.add(user2);

        assertThat(dao.getCount(), is(2));


        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }
}
