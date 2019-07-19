package test

/**
 * Created by Agony on 2019/7/1
 */
abstract class AbstractClass {
    companion object {
        open fun compute() {
            println("compute")
        }
    }

    open fun set() {
        print("set")
    }
}

class AbstractChildClass : AbstractClass() {
    fun get() {
        println("get")
        compute()
    }
}

fun main() {
    AbstractChildClass().get()
}