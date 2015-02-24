package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.aggregator.DefaultArticlesController
import javax.sql.DataSource
import org.postgresql.ds.PGSimpleDataSource
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.mapper.MapperFactoryBean
import com.somanyfeeds.aggregator.PostgresArticleDataGateway
import com.somanyfeeds.aggregator.ArticleDataMapper

object ServiceLocator {
    val dataSource: DataSource = buildDataSource()
    val sqlSessionFactoryBean = buildSqlSessionFactoryBean()
    val staticAssetsController = DefaultStaticAssetsController()
    val articleDataMapper = buildDataMapper(javaClass<ArticleDataMapper>())
    val articleDataGateway = PostgresArticleDataGateway(articleDataMapper = articleDataMapper);
    val articlesController = DefaultArticlesController(articleDataGateway = articleDataGateway)

    fun staticAssetsController(): StaticAssetsController = staticAssetsController

    fun articlesController(): ArticlesController = articlesController

    private fun buildDataSource(): PGSimpleDataSource {
        PGSimpleDataSource().let {
            it.setUser("dam5s")
            it.setServerName("localhost")
            it.setPortNumber(5432)
            it.setDatabaseName("somanyfeeds_dev")
            return it
        }
    }

    private fun buildDataMapper<T>(klass: Class<T>): T {
        val sqlSessionFactory = sqlSessionFactoryBean.getObject()
        sqlSessionFactory.getConfiguration().addMapper(klass)

        val dataMapperFactory = MapperFactoryBean<T>()
        dataMapperFactory.setMapperInterface(klass)
        dataMapperFactory.setSqlSessionFactory(sqlSessionFactory)

        return dataMapperFactory.getObject()
    }

    fun buildSqlSessionFactoryBean(): SqlSessionFactoryBean {
        SqlSessionFactoryBean().let {
            it.setDataSource(dataSource)
            return it
        }
    }
}
