import org.junit.Test
import visitors.PrintJSONVisitor
import kotlin.test.assertEquals

class Phase2Tests {

    data class Point(val x: Double, val y: Double, val name: String?)

    data class Person(
        val name: String, val age: Int, val position: List<String>,
        val skills: List<String>?, val salary: Map<Int, Number?>,
        val nabo: Boolean
    )

    @Test
    fun string() {
        val str = "stringtest"

        val jg = JSONGenerator()
        val v = PrintJSONVisitor()

        val istr = jg.instantiate(str)
        istr.accept(v)
        assertEquals("\"${str}\", ", v.output)


    }

    @Test
    fun integer() {

        val i = 2
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ii = jg.instantiate(i)
        ii.accept(v)
        assertEquals("$i, ", v.output)
    }

    @Test
    fun doubles() {

        val d = 2.2
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val dd = jg.instantiate(d)
        dd.accept(v)
        assertEquals("$d, ", v.output)
    }


    @Test
    fun numbers() {

        val e = 2.2E6
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(e)
        ee.accept(v)
        assertEquals("$e, ", v.output)
    }

    @Test
    fun bools() {

        val b = false
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(b)
        ee.accept(v)
        assertEquals("$b, ", v.output)
    }

    @Test
    fun nulls() {

        val b = null
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(b)
        ee.accept(v)
        assertEquals("$b, ", v.output)
    }

    @Test
    fun SimpleDataClass() {
        val p1 = Point(2.0, 2.0, null)

        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(p1)
        ee.accept(v)
        assertEquals("{ \"name\": null, \"x\": 2.0, \"y\": 2.0 }", v.output)

    }

    @Test
    fun ComplexDataClass() {
        val myPositions: List<String> = listOf("Student", "Trainee")
        val mySkills: List<String>? = null
        val mySalary: Map<Int, Double?> = mapOf(2019 to null, 2020 to 19000.88)


        val me = Person("Francisco", 21, myPositions, mySkills, mySalary, true)

        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(me)
        ee.accept(v)
        val eo =
            "{ \"skills\": null, \"nabo\": true, \"name\": \"Francisco\", \"position\": [ \"Student\", \"Trainee\" ], \"age\": 21, \"salary\": { \"2019\": null, \"2020\": 19000.88 }}"

        assertEquals(eo, v.output)

    }

    @Test
    fun hashmaps() {

        val hashmap = hashMapOf<String, Any>("test" to 3)
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(hashmap)
        ee.accept(v)
        assertEquals("{ \"test\": 3 }", v.output)
    }

    @Test
    fun arr() {

        val arr = listOf<String>("three", "two", "one")
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(arr)
        ee.accept(v)
        assertEquals("[ \"three\", \"two\", \"one\" ], ", v.output)
    }
}