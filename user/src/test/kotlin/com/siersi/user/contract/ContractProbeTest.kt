package com.siersi.user.contract

import com.siersi.common.utils.Result
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import tools.jackson.databind.json.JsonMapper

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ContractProbeTest {

    @Autowired
    lateinit var mapper: JsonMapper

    enum class Level { BASIC, VIP }

    data class Dto(
        val id: Long,
        val name: String,
        val nick: String?,
        val level: Level = Level.BASIC
    )

    private fun probe(tag: String, block: () -> Any?) {
        val r = try {
            "OK   -> " + block()
        } catch (e: Exception) {
            "FAIL -> " + e.javaClass.simpleName + ": " + (e.message ?: "").lineSequence().first()
        }
        println("[PROBE] $tag :: $r")
    }

    @Test
    fun runProbes() {
        println("[PROBE] mapper       = " + mapper.javaClass.name)

        probe("1 未知字段(对方加字段)") {
            mapper.readValue("""{"id":1,"name":"a","nick":null,"extra":"x"}""", Dto::class.java)
        }

        probe("2 非空字段缺失") {
            mapper.readValue("""{"id":1,"nick":null}""", Dto::class.java)
        }

        probe("3 可空字段缺失") {
            mapper.readValue("""{"id":1,"name":"a"}""", Dto::class.java)
        }

        probe("4 枚举未知值") {
            mapper.readValue("""{"id":1,"name":"a","nick":null,"level":"GOLD"}""", Dto::class.java)
        }

        probe("5 数字字段给字符串") {
            mapper.readValue("""{"id":"1","name":"a","nick":null}""", Dto::class.java)
        }

        probe("6 null 字段是否输出") {
            mapper.writeValueAsString(Result.success(data = Dto(1, "a", null)))
        }

        probe("7 snake_case -> camelCase") {
            mapper.readValue("""{"id":1,"name":"a","nick":null,"user_name":"x"}""", Dto::class.java)
        }

        probe("8 Long 超 JS 安全整数") {
            mapper.writeValueAsString(mapOf("id" to 9007199254740993L))
        }

        probe("9 Kotlin 默认值生效") {
            mapper.readValue("""{"id":1,"name":"a","nick":null}""", Dto::class.java).level
        }

        probe("10 输出字段顺序") {
            mapper.writeValueAsString(Dto(1, "a", "n"))
        }
    }
}
