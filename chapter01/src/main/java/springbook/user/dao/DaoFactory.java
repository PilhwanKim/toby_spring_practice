package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

    // 생성자 DI 방식
    @Bean
    public UserDao userDaoByConstructor() {
        return new UserDao(connectionMaker());
    }

    // 수정자 DI 방식
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    // 1.7.4 의존관계 주입의 응용 - 기능 추가(커넥션 횟수 카운트 기능)
    @Bean
    public ConnectionMaker countingConnectionMaker() {
        return new CountingConnectionMaker(connectionMaker());
    }

    @Bean
    public UserDao userDaoWithCounting() {
        return new UserDao(countingConnectionMaker());
    }
}
