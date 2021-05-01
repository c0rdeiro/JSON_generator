import models.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.PROPERTY)
annotation class Exclude()

@Target(AnnotationTarget.PROPERTY)
annotation class ChangeName(val newName: String)

class JSONGenerator() {

    fun instantiate(v: Any?): JSONValue =
        when {
            v is String -> JSONString(v)
            v is Number -> JSONNumber(v)
            v is Boolean -> JSONBoolean(v)
            v == null -> JSONNull()
            v is Collection<*> -> instantiateCollection(v)
            v is Map<*, *> -> instantiateMap(v)
            v is Enum<*> -> JSONString(v.name)
            v::class.isData -> instantiateClass(v)
            else -> throw IllegalArgumentException("Unsupported data type.")
        }


    private fun instantiateClass(c: Any): JSONValue {
        val clazz: KClass<Any> = c::class as KClass<Any>
        val properties = clazz.declaredMemberProperties
        val values = HashMap<JSONKey, JSONValue>()

        properties.forEach {
            if(!it.hasAnnotation<Exclude>())
                it.call(c).let { v -> values[mapKey(it)] = instantiate(v) }
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

    private fun mapKey(key: KProperty1<*, *>): JSONKey{

        return if(key.hasAnnotation<ChangeName>())
            JSONKey(key.findAnnotation<ChangeName>()!!.newName)
        else
            JSONKey(key.name)
    }

}


