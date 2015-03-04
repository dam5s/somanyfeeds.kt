package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.aggregator.DefaultArticlesController
import javax.sql.DataSource
import org.postgresql.ds.PGSimpleDataSource
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.mapper.MapperFactoryBean
import com.somanyfeeds.aggregator.PostgresArticleDataGateway
import com.somanyfeeds.aggregator.ArticleDataMapper
import com.somanyfeeds.kotlinextensions.tap

object ServiceLocator {
    val dataSource: DataSource = PGSimpleDataSource().tap {
        it.setUser("dam5s")
        it.setServerName("localhost")
        it.setPortNumber(5432)
        it.setDatabaseName("somanyfeeds_dev")
    }

    val sqlSessionFactoryBean = SqlSessionFactoryBean().tap {
        it.setDataSource(dataSource)
    }

    val staticAssetsController = DefaultStaticAssetsController()
    val articleDataMapper = buildDataMapper(javaClass<ArticleDataMapper>())
    val articleDataGateway = PostgresArticleDataGateway(articleDataMapper = articleDataMapper);
    val articlesController = DefaultArticlesController(articleDataGateway = articleDataGateway)

    fun staticAssetsController(): StaticAssetsController = staticAssetsController

    fun articlesController(): ArticlesController = articlesController


    private fun buildDataMapper<T>(klass: Class<T>): T {
        val sqlSessionFactory = sqlSessionFactoryBean.getObject().tap {
            it.getConfiguration().addMapper(klass)
        }

        return MapperFactoryBean<T>().let { dataMapperFactory ->
            dataMapperFactory.setMapperInterface(klass)
            dataMapperFactory.setSqlSessionFactory(sqlSessionFactory)
            dataMapperFactory.getObject()
        }
    }
}
