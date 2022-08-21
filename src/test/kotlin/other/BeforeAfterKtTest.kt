package other

import cz.mpts.libs.extrautils.kotlin.other.*
import org.junit.Test

import org.junit.Assert.*

class BeforeAfterKtTest {

    @Test
    fun beforeCommandIfTest() {
        var x = 0
        var cond = false
        val resI: Int = beforeCommandIf({ 3 }, cond) { x = 1 }
        assertEquals(0, x)
        assertEquals(3, resI)
        cond = true
        val resB = beforeCommandIf({ true }, cond) { x = 1 }
        assertEquals(1, x)
        assertTrue(resB)
    }

    @Test
    fun runIfAndThenTest() {
        var x = 0
        var cond = false
        var res: Double = runIf(cond) {
            x = 1
        } andThen { 3.3 }
        assertEquals(0, x)
        assertEquals(3.3, res, 0.0001)
        cond = true
        res = runIf(cond) {
            x = 1
        } andThen { 555.233 }
        assertEquals(1, x)
        assertEquals(555.233, res, 0.0001)
    }
}
