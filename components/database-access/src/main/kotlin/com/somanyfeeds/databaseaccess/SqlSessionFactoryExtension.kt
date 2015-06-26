package com.somanyfeeds.databaseaccess

import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory

fun <T, U> SqlSessionFactory.withMapper(mapperType: Class<T>, useMapper: T.(session: SqlSession) -> U): U {
    val session = openSession()
    try {
        return session
            .getMapper(mapperType)
            .useMapper(session)
    } finally {
        session.close()
    }
}
