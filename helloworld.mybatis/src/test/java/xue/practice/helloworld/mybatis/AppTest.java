package xue.practice.helloworld.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
	/**
	 * sqlite链接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	@Test
	public void testSqliteConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("org.sqlite.JDBC");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try
		{
			conn = DriverManager.getConnection("jdbc:sqlite:mydb.db");
			pst = conn.prepareStatement("select * from book");
			rs = pst.executeQuery();
			while (rs.next())
			{
				System.out.println(rs.getString(2) + "----" + rs.getString(3));
			}
		}
		finally
		{
			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (Exception e)
				{
				}
			}
			if (pst != null)
			{
				try
				{
					pst.close();
				}
				catch (Exception e)
				{
				}
			}
			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (Exception e)
				{
				}
			}
		}
	}
	
	/**
	 * 通过传入字面量从XML中选取sql
	 * @throws IOException
	 */
	@Test
	public void test1()
		throws IOException
	{
		String resource = "xue/practice/helloworld/mybatis/mybatis_config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		// 创建sessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
				.build(inputStream);
		// 创建session
		SqlSession session = sqlSessionFactory.openSession();
		try
		{
			List<Book> books = session
					.selectList("xue.practice.helloworld.mybatis.BookMapper.getBook");
			for (Book book : books)
			{
				System.out.println(book.getName() + "---" + book.getAuthor());
			}
		}
		finally
		{
			session.close();
		}
	}
	
	/**
	 * 定义一个接口，避免使用字面量
	 * @throws IOException
	 */
	@Test
	public void test2()
		throws IOException
	{
		String resource = "xue/practice/helloworld/mybatis/mybatis_config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
				.build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		try
		{
			// 多定义一个接口BookMapper。并且这个接口的名称和BookMapper.xml的namespace一致。
			BookMapper bookMapper = session.getMapper(BookMapper.class);
			List<Book> books = bookMapper.getBook();
			for (Book book : books)
			{
				System.out.println(book.getName() + "---" + book.getAuthor());
			}
		}
		finally
		{
			session.close();
		}
	}
	
	/**
	 * 动态sql，in
	 * @throws IOException
	 */
	@Test
	public void test3()
		throws IOException
	{
		String resource = "xue/practice/helloworld/mybatis/mybatis_config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
				.build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		try
		{
			BookMapper bookMapper = session.getMapper(BookMapper.class);
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(1);
			ids.add(4);
			List<Book> books = bookMapper.getBookByID(ids);
			for (Book book : books)
			{
				System.out.println(book.getName() + "---" + book.getAuthor());
			}
		}
		finally
		{
			session.close();
		}
	}
	
}
