package com.somanyfeeds.jsonserialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.ZonedDateTime
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.somanyfeeds.kotlinextensions.tap
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class ObjectMapperProvider {
    fun get(): ObjectMapper {
        val module = SimpleModule()
        module.addSerializer(javaClass<ZonedDateTime>(), object : JsonSerializer<ZonedDateTime>() {
            override fun serialize(value: ZonedDateTime, jgen: JsonGenerator, provider: SerializerProvider) {
                jgen.writeString(value.toString())
            }
        })

        return ObjectMapper().tap {
            registerKotlinModule()
            registerModule(module)
        }
    }
}
