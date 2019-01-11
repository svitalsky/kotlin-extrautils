/*
 * Copyright © 2017–2019 Marcel Svitalsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.mpts.libs.extrautils.kotlin.values

import cz.mpts.libs.extrautils.kotlin.NO_VALUE_AVAILABLE
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.ExpectedException
import java.math.BigDecimal


class OptionalValueTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun getValueSet() {
        assertTrue(OptionalValue.of(value = "").valueSet)
        assertTrue(OptionalValue.of(value = null).valueSet)
        assertFalse(OptionalValue.empty<Any>().valueSet)
    }


    @Test
    fun isEmpty() {
        assertTrue(OptionalValue.empty<Int>().isEmpty)
        assertFalse(OptionalValue.of(value = 123).isEmpty)
    }


    @Test
    fun getValue() {
        assertEquals(BigDecimal.ZERO, OptionalValue.of(value = BigDecimal.ZERO).value)
        assertNull(OptionalValue.of(value = null).value)
    }


    @Test
    fun getValueUnset() {
        thrown.expect(NoSuchElementException::class.java)
        thrown.expectMessage(NO_VALUE_AVAILABLE)
        OptionalValue.empty<Any>().value
    }


    @Suppress("ReplaceCallWithComparison")
    @Test
    fun equals() {
        assertTrue(OptionalValue.of(value = "a") == OptionalValue.of(value = "a"))
        assertFalse(OptionalValue.of(value = "a") == OptionalValue.of(value = "b"))
        assertFalse(OptionalValue.of(value = "1").equals(OptionalValue.of(value = 1)))
        assertTrue(OptionalValue.of(value = null) == OptionalValue.of(value = null))
        // this one works thanks to type erasure
        assertTrue(OptionalValue.of<Int?>(value = null).equals(OptionalValue.of<String?>(value = null)))
        assertTrue(OptionalValue.empty<Any>() == OptionalValue.empty<Any>())
    }

    @Test
    fun toStringTest() {
        assertEquals("OptionalValue(3)", OptionalValue.of(3).toString())
        assertEquals("OptionalValue(null)", OptionalValue.of(null).toString())
        assertEquals("OptionalValue<<<#-VALUE NOT SET-#>>>", OptionalValue.empty<Int>().toString())
    }
}
