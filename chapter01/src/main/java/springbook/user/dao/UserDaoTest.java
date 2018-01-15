package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

/**
 * Created by pilhwankim on 15/12/2017.
 */
public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // method 01. JavaConfig 방식을 사용한다.
//        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);

        // method 02. UserDaoTest 와 동일 패키지 위치를 기점을 찾고 싶을 경우.
        ApplicationContext ac = new GenericXmlApplicationContext("/applicationContext.xml");

        // method 03. 루트 패키지를 기점을 찾고 싶을 경우.
//        ApplicationContext ac = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = ac.getBean("userDao", UserDao.class);

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
    }
}
