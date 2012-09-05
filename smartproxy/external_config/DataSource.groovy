
/**
 * DataSource.groovy
 * Purpose: 
 * @author mkapoor
 * @version Aug 30, 2012 4:04:36 PM
 */

dataSource{
	driverClassName ="org.postgresql.Driver"
	dialect = "org.hibernate.dialect.PostgreSQLDialect"
}

environments {
	development {
		dataSource {
			url = "jdbc:postgresql://localhost:5432/smart"
			username = "smart"
			password = "smart"
			loggingSql = true
			formatSql = true
			dbCreate = "update"
		}
	}
	test {
		dataSource {
			url = "jdbc:postgresql://localhost:5432/smart"
			username = "smart"
			password = "smart"
		}
	}
	production {
		dataSource {
			url = "jdbc:postgresql://localhost:5432/smart"
			username = "smart"
			password = "smart"
		}
	}
}
