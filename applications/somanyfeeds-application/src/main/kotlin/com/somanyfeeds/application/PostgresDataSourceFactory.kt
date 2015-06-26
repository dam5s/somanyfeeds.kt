package com.somanyfeeds.application

import com.somanyfeeds.jsonserialization.ObjectMapperProvider
import com.somanyfeeds.kotlinextensions.tap
import com.zaxxer.hikari.HikariDataSource
import org.postgresql.ds.PGSimpleDataSource
import java.io.StringReader
import javax.sql.DataSource

class PostgresDataSourceFactory() {
    fun create(): DataSource {
        if (usingCloudFoundry()) {
            return createElephantSQLDataSource()
        }

        return PGSimpleDataSource().tap {
            setUser("dam5s")
            setServerName("localhost")
            setPortNumber(5432)
            setDatabaseName("somanyfeeds_dev")
        }
    }

    private fun createElephantSQLDataSource(): DataSource {
        val objectMapper = ObjectMapperProvider().get()
        val vcapServicesString = getVCAPServicesString()!!
        val vcapServicesJson = objectMapper.readTree(StringReader(vcapServicesString))

        val url = vcapServicesJson.get("elephantsql").get(0).get("credentials").get("uri").asText()!!

        val fromUserToDbName = url.removePrefix("postgres://").splitBy("/")
        val fromUserToPort = fromUserToDbName.first().splitBy("@")

        val userAndPwd = fromUserToPort.first().splitBy(":")
        val serverAndPort = fromUserToPort.last().splitBy(":")

        val dbName = fromUserToDbName.last()

        return HikariDataSource().tap {
            setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource")
            setUsername(userAndPwd.first())
            setPassword(userAndPwd.last())
            setMaximumPoolSize(1) // Free Plan Turtle is limited to 5, 2 per instance then.
            setConnectionTimeout(5000)

            addDataSourceProperty("user", userAndPwd.first())
            addDataSourceProperty("password", userAndPwd.last())
            addDataSourceProperty("serverName", serverAndPort.first())
            addDataSourceProperty("portNumber", serverAndPort.last().toInt())
            addDataSourceProperty("databaseName", dbName)
        }
    }

    private fun usingCloudFoundry(): Boolean {
        return getVCAPServicesString() != null
    }

    private fun getVCAPServicesString() = System.getenv().get("VCAP_SERVICES")
}
