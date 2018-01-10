package xue.practice.helloworld.mybatis;

import java.util.List;

/**
 * 接口的名称和BookMapper.xml的namespace完全一致
 * 
 * @author Administrator
 *
 */
public interface BookMapper
{
	
	List<Book> getBook();
	
	List<Book> getBookByID(List<Integer> ids);
}
