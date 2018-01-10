package xuezhenhua.log2dao;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App {
	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) throws PropertyVetoException, SQLException {
		logger.info("Hello World");

//		DataSource ds = DataSourceUtil.getDataSource();
//
//		try (Connection conn = ds.getConnection();
//				PreparedStatement pst = conn.prepareStatement("select sysdate() from dual");
//				ResultSet rs = pst.executeQuery();) {
//			if (rs.next()) {
//				System.out.println(rs.getTime(1));
//			}
//		}
	}
}
