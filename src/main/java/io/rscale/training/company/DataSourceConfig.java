package io.rscale.training.company;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig extends AbstractCloudConfig {

	private static final Logger logger = Logger.getLogger(DataSourceConfig.class);

	public DataSourceConfig() {
		logger.info(this.getClass() + " loaded");
	}

	@Bean
	public DataSource dataSource() throws Exception {

		DataSource dataSource = cloud().getSingletonServiceConnector(DataSource.class, null);
		if (!isMySQL(dataSource)) {
			logger.error("MySQL required when running cloud profile.");
			throw new UnsatisfiedDependencyException("javax.sql.DataSource", "javax.sql.DataSource",
					"javax.sql.DataSource", "MySQL required when running cloud profile.");
		}
		return dataSource;

	}

	private boolean isMySQL(DataSource dataSource) {
		
		String driverName = null;
		boolean flag = false;
		try {
			driverName = dataSource.getConnection().getMetaData().getDriverName();
			if (driverName.startsWith("MySql")){
				flag = true;
			}
			System.out.println(">>>>>>>>>>>" + driverName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(">>>>>>>>>>>isMySQL: " + flag);
		return flag;
		
	}

}
