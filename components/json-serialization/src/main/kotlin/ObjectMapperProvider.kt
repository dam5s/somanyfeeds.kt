package com.somanyfeeds.jsonserialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.databind.module.SimpleModule
import java.time.ZonedDateTime
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

class ObjectMapperProvider {
    fun get(): ObjectMapper {
        val module = SimpleModule()
        module.addSerializer(javaClass<ZonedDateTime>(), ZonedDateTimeSerializer())

        return ObjectMapper().let {
            it.registerKotlinModule()
            it.registerModule(module)
            return it
        }
    }
}

class ZonedDateTimeSerializer : JsonSerializer<ZonedDateTime>() {
    override fun serialize(value: ZonedDateTime, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString(value.toString())
    }
}
