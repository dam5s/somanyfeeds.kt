import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource
import org.mybatis.spring.mapper.MapperFactoryBean
import org.mybatis.spring.SqlSessionFactoryBean
import java.util.Properties

class TestDatabaseConfig {

    val dataSource: DataSource = buildTestDataSource()
    val sqlSessionFactoryBean = buildSqlSessionFactoryBean()

    private fun buildTestDataSource(): PGSimpleDataSource {
        val propsInputStream = javaClass<Any>().getResourceAsStream("/connection.properties")
        val props = Properties().let { it.load(propsInputStream); it }

        return PGSimpleDataSource().let {
            it.setUser(props.getProperty("test.user"))
            it.setServerName(props.getProperty("test.serverName"))
            it.setPortNumber(props.getProperty("test.portNumber").toInt())
            it.setDatabaseName(props.getProperty("test.databaseName"))
            it
        }
    }

    public fun buildTestDataMapper<T>(klass: Class<T>): T {
        val sqlSessionFactory = sqlSessionFactoryBean.getObject()
        sqlSessionFactory.getConfiguration().addMapper(klass)

        MapperFactoryBean<T>().let { dataMapperFactory ->
            dataMapperFactory.setMapperInterface(klass)
            dataMapperFactory.setSqlSessionFactory(sqlSessionFactory)

            return dataMapperFactory.getObject()
        }
    }

    fun executeSql(sql: String) {
        val conn = dataSource.getConnection()
        try {
            val stmt = conn.prepareStatement(sql)
            stmt.execute()
        }
        finally {
            conn.close()
        }
    }

    fun buildSqlSessionFactoryBean(): SqlSessionFactoryBean {
        return SqlSessionFactoryBean().let { it.setDataSource(dataSource); it }
    }
}
