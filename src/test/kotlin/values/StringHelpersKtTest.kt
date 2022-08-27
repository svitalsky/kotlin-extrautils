package values

import cz.mpts.libs.extrautils.kotlin.values.asWordIn
import org.junit.Assert.*
import org.junit.Test

class StringHelpersKtTest {

    @Test
    fun asWordInTest() {
        val w = "Ahoj"
        assertFalse(" " asWordIn "Ahoj a nazdar")
        assertFalse(w asWordIn "Hmm" )
        assertTrue("$w " asWordIn " $w   ")
        assertTrue(w asWordIn "$w a nazdar")
        assertFalse(w asWordIn "${w}a nazdar")
        assertTrue(w asWordIn "A on na to: $w")
        assertFalse(w asWordIn "A on na to:$w")
        assertTrue(w asWordIn "Hmm, $w and this")
        assertFalse(w asWordIn "Hmm$w and this")
        assertFalse(w asWordIn "Hmm, ${w}and this")
        assertFalse(w asWordIn "Hmm${w}and this")
    }
}
