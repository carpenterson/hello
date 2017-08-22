package xuezhenhua.log2dao;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceUtil {

	static Properties p;

	static {
		try (InputStream is = DataSourceUtil.class.getClassLoader()
				.getResourceAsStream("datasource-config.properties");) {
			p = new Properties();
			p.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DataSource getDataSource() throws PropertyVetoException {

		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = new ComboPooledDataSource();

		dataSource.setDriverClass(p.getProperty("driverClass"));
		dataSource.setJdbcUrl(p.getProperty("jdbcUrl"));
		dataSource.setUser(p.getProperty("user"));
		dataSource.setPassword(p.getProperty("password"));

		return dataSource;
	}

}
