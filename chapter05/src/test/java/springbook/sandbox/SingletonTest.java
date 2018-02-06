package springbook.sandbox;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;

import static org.hamcrest.CoreMatchers.sameInstance;
 
public class SingletonTest {
	@Test
	public void testSingletonInSpringContext() {
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		Assert.assertThat(context.getBean("userDao", UserDao.class), sameInstance(context.getBean("userDao", UserDao.class)));
	}
} 
