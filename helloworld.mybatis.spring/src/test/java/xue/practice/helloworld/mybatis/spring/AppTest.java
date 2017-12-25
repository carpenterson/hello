package xue.practice.helloworld.mybatis.spring;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	
	/**
	 * 事务回滚
	 */
	@Test
	public void testTransaction()
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		
		BookBussiness bookBussiness = ctx.getBean(BookBussiness.class);
		
		Book book = new Book(5, "雪山飞狐", "金庸");
		try
		{
			bookBussiness.insertBook(book);
		}
		catch (Exception e)
		{
			System.out.println("事务回滚，《雪山飞狐》不应该被插入数据库中");
		}
		
		List<Book> books = bookBussiness.getBook();
		for (Book b : books)
		{
			System.out.println(b.getName());
		}
	}
	
	/**
	 * 多数据源
	 */
	@Test
	public void testTransaction2()
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		
		BookBussiness bookBussiness = ctx.getBean(BookBussiness.class);
		
		Book book = new Book(5, "雪山飞狐", "金庸");
		try
		{
			bookBussiness.insertBook2(book);
		}
		catch (Exception e)
		{
			System.out.println("事务回滚，《雪山飞狐》不应该被插入数据库中");
		}
		
		List<Book> books = bookBussiness.getBook2();
		for (Book b : books)
		{
			System.out.println(b.getName());
		}
	}
	
	/**
	 * 在另一个数据源开事务
	 */
	@Test
	public void testTransaction3()
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		
		BookBussiness bookBussiness = ctx.getBean(BookBussiness.class);
		
		Book book = new Book(5, "雪山飞狐", "金庸");
		try
		{
			bookBussiness.insertBook21(book);
		}
		catch (Exception e)
		{
			// 事务开在了数据源2。插入操作是在数据源1
			System.out.println("事务回滚，《雪山飞狐》不应该被插入数据库中");
		}
		
		List<Book> books = bookBussiness.getBook();
		for (Book b : books)
		{
			System.out.println(b.getName());
		}
	}
}
