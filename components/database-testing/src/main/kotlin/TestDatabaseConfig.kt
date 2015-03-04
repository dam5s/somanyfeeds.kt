import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource
import org.mybatis.spring.mapper.MapperFactoryBean
import org.mybatis.spring.SqlSessionFactoryBean
import java.util.Properties
import com.somanyfeeds.kotlinextensions.tap
import com.somanyfeeds.kotlinextensions.getResourceAsStream

class TestDatabaseConfig {
    val dataSource: DataSource = buildTestDataSource()
    val sqlSessionFactoryBean = SqlSessionFactoryBean().tap { it.setDataSource(dataSource) }

    private fun buildTestDataSource(): PGSimpleDataSource {
        val props = Properties().tap { it.load(getResourceAsStream("/connection.properties")) }

        return PGSimpleDataSource().tap {
            it.setUser(props.getProperty("test.user"))
            it.setServerName(props.getProperty("test.serverName"))
            it.setPortNumber(props.getProperty("test.portNumber").toInt())
            it.setDatabaseName(props.getProperty("test.databaseName"))
        }
    }

    public fun buildTestDataMapper<T>(klass: Class<T>): T {
        val sqlSessionFactory = sqlSessionFactoryBean.getObject().tap {
            it.getConfiguration().addMapper(klass)
        }

        return MapperFactoryBean<T>().let { dataMapperFactory ->
            dataMapperFactory.setMapperInterface(klass)
            dataMapperFactory.setSqlSessionFactory(sqlSessionFactory)
            dataMapperFactory.getObject()
        }
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
