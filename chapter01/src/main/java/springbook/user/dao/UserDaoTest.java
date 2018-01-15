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
        // ver1. 관계 설정 코드가 클라이언트 class(UserDaoTest) 에서 하도록 변경되었다. 이제야 서로가 완전히 분리되었다.
        // ver2. 관계 설정 역할을 DaoFactory 로 분리시켰다. UserDaoTest 역할이 한가지로 줄어들었다.
        // ver3. 본격적으로 Spring 사용한다.

        // ver4. JavaConfig 대신 XML 설정 방식을 사용한다.
//        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);

        // ver4-1. UserDaoTest 와 동일 패키지 위치를 기점을 찾고 싶을 경우.
//        ApplicationContext ac = new GenericXmlApplicationContext("/applicationContext.xml");

        // ver4-2. 루트 패키지를 기점을 찾고 싶을 경우.
        ApplicationContext ac = new GenericXmlApplicationContext("applicationContext.xml");
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
