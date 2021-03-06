import org.junit.Test
import visitors.PrintJSONVisitor
import kotlin.test.assertEquals


class Phase2Tests {

    enum class Form{
        Circle, Rectangle, Square
    }

    data class Point(val x: Double, val y: Double,
                     @Exclude val name: String?,
                     @ChangeName("geometry") val form: Form
                        )

    data class Person(
        val name: String, val age: Int, val position: List<String>,
        val skills: List<String>?, val salary: Map<Int, Number?>,
        @Exclude val nabo: Boolean
    )

    @Test
    fun string() {
        val str = "stringtest"

        val jg = JSONGenerator()
        val v = PrintJSONVisitor()

        val istr = jg.instantiate(str)
        istr.accept(v)
        assertEquals("\"${str}\"", v.output)


    }

    @Test
    fun integer() {

        val i = 2
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ii = jg.instantiate(i)
        ii.accept(v)
        assertEquals("$i", v.output)
    }

    @Test
    fun doubles() {

        val d = 2.2
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val dd = jg.instantiate(d)
        dd.accept(v)
        assertEquals("$d", v.output)
    }


    @Test
    fun numbers() {

        val e = 2.2E6
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(e)
        ee.accept(v)
        assertEquals("$e", v.output)
    }

    @Test
    fun bools() {

        val b = false
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(b)
        ee.accept(v)
        assertEquals("$b", v.output)
    }

    @Test
    fun nulls() {

        val b = null
        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(b)
        ee.accept(v)
        assertEquals("$b", v.output)
    }

    @Test
    fun SimpleDataClass() {
        val p1 = Point(2.0, 2.0, null, Form.Circle)

        val jg = JSONGenerator()
        val v = PrintJSONVisitor()
        val ee = jg.instantiate(p1)
        ee.accept(v)
        assertEquals("{ \"y\": 2.0, \"x\": 2.0, \"form\": \"Circle\" }", v.output)

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
            "{ \"name\": \"Francisco\", \"age\": 21, \"position\": [ \"Student\", \"Trainee\" ], \"salary\": { \"2019\": null, \"2020\": 19000.88 }, \"skills\": null }"

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
        assertEquals("[ \"three\", \"two\", \"one\" ]", v.output)
    }
}