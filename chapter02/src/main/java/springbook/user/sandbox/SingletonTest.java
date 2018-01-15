package springbook.user.sandbox;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
 
public class SingletonTest {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		
		System.out.println(context.getBean("userDao", UserDao.class));
		System.out.println(context.getBean("userDao", UserDao.class));
	}
} 
