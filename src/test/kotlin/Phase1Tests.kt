import models.*
import org.junit.Test
import visitors.FindAllStringsVisitor
import visitors.PrintJSONVisitor
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Phase1Tests {


    val arr1 = JSONArray(arrayListOf(JSONNumber(2), JSONNumber(4)))
    val str1 = JSONKey("name")
    val str2 = JSONBoolean(false)
    val obj1 = JSONObject(hashMapOf(JSONKey("second") to arr1, JSONKey("tier2") to JSONString("Kappa"), JSONKey("thirdin") to JSONNull()))

    val test1 = JSONObject(hashMapOf(str1 to str2, JSONKey("pepe") to JSONString("guardioka") , JSONKey("another level") to obj1))


    @Test
    fun printObject(){

        val v1 = PrintJSONVisitor()
        val expected_out1 = "{ \"name\": false, \"pepe\": \"guardioka\", \"another level\": { \"tier2\": \"Kappa\", \"thirdin\": null, \"second\": [ 2, 4 ] }}"
        test1.accept(v1)

        assertEquals(expected_out1, v1.output)
    }

    @Test
    fun printArray(){
        val v2 = PrintJSONVisitor()
        val expected_output2 = "[ 2, 4 ], "
        arr1.accept(v2)

        assertEquals(expected_output2, v2.output)
    }

    @Test
    fun visitorsTest(){

        val v3 = FindAllStringsVisitor()
        val expected_output2 = mutableListOf<String>("Kappa", "guardioka")
        test1.accept(v3)

        assertTrue(expected_output2.containsAll(v3.output))
        assertEquals(2, v3.count)
    }


}