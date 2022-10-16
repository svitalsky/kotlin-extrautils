package cz.mpts.libs.extrautils.kotlin.values

import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.test.*

class NotNullTest {

    @Test
    fun correctTest() {
        val n: Int? = getNum(77, false)
        assertEquals(n, n.notNull)
    }

    @Test
    fun incorrectTest() {
        val exception = assertThrows(IllegalStateException::class.java) {
            val n: Int? = getNum(37, true)
            val m: Int = n.notNull
            if (m != n) throw UnsupportedOperationException("Really? How did we get here?")
        }
        assertEquals("Value is null even though it shouldn't be.", exception.message)
    }

    @Test
    fun correctTestFun() {
        val n: Int? = getNum(77, false)
        assertEquals(n, n.notNull("Shouldn't be"))
    }

    @Test
    fun incorrectTestFun() {
        val exception = assertThrows(IllegalStateException::class.java) {
            val n: Int? = getNum(37, true)
            val m: Int = n.notNull("Should be broken.")
            if (m != n) throw UnsupportedOperationException("Really? How did we get here?")
        }
        assertEquals("Should be broken.", exception.message)
    }

    @Test
    fun correctTestFunLazy() {
        val n: Int? = getNum(77, false)
        assertEquals(n, n.notNull { "Shouldn't be" })
    }

    @Test
    fun incorrectTestFunLazy() {
        val exception = assertThrows(IllegalStateException::class.java) {
            val n: Int? = getNum(37, true)
            val m: Int = n.notNull { "Should be broken." }
            if (m != n) throw UnsupportedOperationException("Really? How did we get here?")
        }
        assertEquals("Should be broken.", exception.message)
    }

    private fun getNum(num: Int, drop: Boolean) : Int? =
        if (drop) null else num
}
