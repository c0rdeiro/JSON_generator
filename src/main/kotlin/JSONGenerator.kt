import models.*
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class JSONGenerator() {

    //TODO: Insert Annotations

    fun instantiate(v: Any?): JSONValue =
        when (v) {
            is String -> JSONString(v)
            is Number -> JSONNumber(v)
            is Boolean -> JSONBoolean(v)
            null -> JSONNull()
            is Collection<*> -> instantiateCollection(v)
            is Map<*, *> -> instantiateMap(v)
            //is Enum<*> -> instantiateEnum(v)
            else -> instantiateClass(v)
        }


    private fun instantiateClass(c: Any): JSONValue {
        val clazz: KClass<Any> = c::class as KClass<Any>
        val properties = clazz.declaredMemberProperties
        val values = HashMap<JSONKey, JSONValue>()

        properties.forEach {
            it.call(c).let { v -> values[JSONKey(it.name)] = instantiate(v) }
        }

        return JSONObject(values)
    }


    private fun instantiateCollection(arr: Collection<*>): JSONArray {

        val output = mutableListOf<JSONValue>()

        arr.forEach { output.add(instantiate(it)) }

        return JSONArray(output)
    }

    private fun instantiateMap(map: Map<*, *>): JSONObject {

        val output = mutableMapOf<JSONKey, JSONValue>()

        map.forEach { output[JSONKey(it.key.toString())] = instantiate(it.value) }

        return JSONObject(output)
    }

    //TODO Enum instantiation

}


