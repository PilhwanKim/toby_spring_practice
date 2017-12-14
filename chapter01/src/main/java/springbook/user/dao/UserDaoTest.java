package springbook.user.dao;

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

        // 관계 설정 코드가 클라이언트 class(UserDaoTest) 에서 하도록 변경되었다.
        // 이제야 서로가 완전히 분리되었다.
        NConnectionMaker connectionMaker = new NConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        userDao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");
    }
}
