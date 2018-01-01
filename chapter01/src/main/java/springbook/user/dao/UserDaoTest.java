package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

/**
 * Created by pilhwankim on 15/12/2017.
 */
public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        User user = new User();
        user.setId("leon0517");
        user.setName("김필환");
        user.setPassword("secret1!");

        // ver1. 관계 설정 코드가 클라이언트 class(UserDaoTest) 에서 하도록 변경되었다. 이제야 서로가 완전히 분리되었다.
        // ver2. 관계 설정 역할을 DaoFactory 로 분리시켰다. UserDaoTest 역할이 한가지로 줄어들었다.
        // ver3. 본격적으로 Spring 사용한다.
        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = ac.getBean("userDao", UserDao.class);

        System.out.println(user.getId() + "등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");
    }
}
