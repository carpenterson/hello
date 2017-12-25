package xue.practice.helloworld.mybatis.spring;

import java.util.List;

/**
 * 接口的名称和BookMapper.xml的namespace完全一致
 * 
 * @author Administrator
 *
 */
public interface BookMapper2
{
	
	List<Book> getBook();
	
	void insertBook(Book book);
}
