package com.example.chat_boss

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class A() {
    lateinit var model: Model
    var secondModel: SecondModel? = null
}

fun main() {
    val a = A()
    a.model
    a.secondModel?.someFun()
}

class Model(id: Int) {

}

class SecondModel() {
    fun someFun() {
        println("Test string")
    }
}