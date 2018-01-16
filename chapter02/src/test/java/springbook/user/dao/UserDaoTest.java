package springbook.user.dao;

import org.junit.Assert;
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

    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext ac = new GenericXmlApplicationContext("/applicationContext.xml");
        UserDao userDao = ac.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));


        User user1 = new User("pilhwankim", "김필환", "secret2@");
        User user2 = new User("leegm700", "이길원", "springno2");
        userDao.add(user1);
        userDao.add(user2);

        assertThat(userDao.getCount(), is(2));


        User userget1 = userDao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = userDao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext ac = new GenericXmlApplicationContext("/applicationContext.xml");
        UserDao dao = ac.getBean("userDao", UserDao.class);

        User user1 = new User("pilhwankim", "김필환", "secret2@");
        User user2 = new User("leegm700", "이길원", "springno1");
        User user3 = new User("bumjin", "박범진", "springno2");

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
        ApplicationContext ac = new GenericXmlApplicationContext("/applicationContext.xml");
        UserDao dao = ac.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }
}
