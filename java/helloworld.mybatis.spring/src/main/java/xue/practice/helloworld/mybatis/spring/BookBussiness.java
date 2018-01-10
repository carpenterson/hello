package xue.practice.helloworld.mybatis.spring;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import com.sun.jmx.snmp.internal.SnmpAccessControlSubSystem;

@Named
public class BookBussiness
{
	
	
	public BookBussiness() {
		super();
		System.out.println("tt");
	}

	@Inject
	private BookMapper1 bookMapper1;
	
	@Inject
	private BookMapper2 bookMapper2;
	
	@Transactional
	public void insertBook(Book book)
	{
		bookMapper1.insertBook(book);
		throw new RuntimeException("abc");
	}
	
	@Transactional("transactionManager2")
	public void insertBook2(Book book)
	{
		bookMapper2.insertBook(book);
		throw new RuntimeException("abc");
	}
	
	@Transactional("transactionManager2")
	public void insertBook21(Book book)
	{
		bookMapper1.insertBook(book);
		throw new RuntimeException("abc");
	}
	
	public List<Book> getBook()
	{
		return bookMapper1.getBook();
	}
	
	public List<Book> getBook2()
	{
		return bookMapper2.getBook();
	}
	
}
