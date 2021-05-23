package UI

import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.PROPERTY)
annotation class InjectAdd

class Injector {

    companion object{

        val map: MutableMap<String, List<KClass<*>>> = mutableMapOf()

        init{
            val scanner = Scanner(File("di.properties"))
            while(scanner.hasNext()){
                val line = scanner.nextLine()
                val parts = line.split("=")
                map[parts[0]] =  parts[1].split(",")
                    .map { Class.forName(it).kotlin }

            }

            scanner.close()
        }
        fun <T: Any> create(type: KClass<T>): T{

            val o = type.createInstance()

            type.declaredMemberProperties.forEach {
                it.isAccessible = true
                if(it.hasAnnotation<Inject>()){

                    val key = type.simpleName + "." + it.name
                    val obj = map[key]!!.first().createInstance()
                    (it as KMutableProperty<*>).setter.call(o, obj)
                }
                else if(it.hasAnnotation<InjectAdd>()){
                    val key = type.simpleName + "." + it.name

                    val obj = map[key]!!.map { it.createInstance()}
                    (it.getter.call(o) as MutableList<Any>).addAll(obj)
                }
            }


            return o
        }
    }


}