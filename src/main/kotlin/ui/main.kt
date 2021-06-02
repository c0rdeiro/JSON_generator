package ui

import JSONGenerator

fun main() {
    data class Point(val x: Int, val y: Int )
    data class Person(
        val name: String,
        val age: Int,
        val location: Point,
        val pepe: List<String>,
        val hist: List<Point>
    )

    val arr = listOf<String>("potato", "poteto", "radon")
    val arrP = listOf<Point>(Point(1, 1), Point(5, 5))
    val test = Person("Francisco", 21, Point(-2, -2), arr, arrP)

    val test2 = Person("Inácio", 31, Point(-2, -2), arr, arrP)
    val classroom = listOf<Person>(test, test2)

    val o = JSONGenerator().instantiate(test)
    Visualizer().instantiate(o)

}