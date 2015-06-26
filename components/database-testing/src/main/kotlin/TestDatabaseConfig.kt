import com.somanyfeeds.kotlinextensions.getResourceAsStream
import com.somanyfeeds.kotlinextensions.tap
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import org.postgresql.ds.PGSimpleDataSource
import java.util.Properties
import javax.sql.DataSource

class TestDatabaseConfig(mapperType: Class<*>) {
    val dataSource: DataSource = buildTestDataSource()
    val sqlSessionFactory = buildSqlSessionFactory(dataSource, mapperType)

    private fun buildTestDataSource(): PGSimpleDataSource {
        val props = Properties().tap { load(getResourceAsStream("connection.properties")) }

        return PGSimpleDataSource().tap {
            setUser(props.getProperty("test.user"))
            setServerName(props.getProperty("test.serverName"))
            setPortNumber(props.getProperty("test.portNumber").toInt())
            setDatabaseName(props.getProperty("test.databaseName"))
        }
    }

    private fun buildSqlSessionFactory(dataSource: DataSource, mapperType: Class<*>): SqlSessionFactory {
        val transactionFactory: TransactionFactory = JdbcTransactionFactory()
        val environment = Environment("development", transactionFactory, dataSource)
        val configuration = Configuration(environment)

        configuration.addMapper(mapperType)

        return SqlSessionFactoryBuilder().build(configuration)
    }

    fun executeSql(sql: String) {
        val conn = dataSource.getConnection()
        try {
            conn.prepareStatement(sql).let { it.execute() }
        } finally {
            conn.close()
        }
    }
}
