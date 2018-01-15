package springbook.user.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by pilhwankim on 15/12/2017.
 */
public class UserDaoTest {

    @Test
    public void main() throws SQLException {
        ApplicationContext ac = new GenericXmlApplicationContext("/applicationContext.xml");
        UserDao userDao = ac.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("pilhwankim");
        user.setName("김필환");
        user.setPassword("secret2@");
        userDao.add(user);

        User user2 = userDao.get(user.getId());
        Assert.assertThat(user2.getName(), is(user.getName()));
        Assert.assertThat(user2.getPassword(), is(user.getPassword()));
    }
}
