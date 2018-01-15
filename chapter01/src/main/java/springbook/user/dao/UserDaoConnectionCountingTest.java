package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

/**
 * 1.7.4 의존관계 주입의 응용 - 기능 추가(커넥션 횟수 카운트 기능)
 * 커넥션 카운트 기능의 TestCase
 */
@Deprecated
public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDaoWithCounting", UserDao.class);

        User user = new User();
        user.setId("leon0517");
        user.setName("김필환");
        user.setPassword("secret1!");
        userDao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");

        CountingConnectionMaker ccm = context.getBean("countingConnectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter : " + ccm.getCounter());
    }
}
