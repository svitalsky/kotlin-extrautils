/*
 * Copyright © 2017–2018 Marcel Svitalsky
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

import org.junit.*
import org.junit.rules.ExpectedException
import java.math.BigDecimal


class OptionalValueTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun getValueSet() {
        Assert.assertTrue(OptionalValue.of(value = "").valueSet)
        Assert.assertTrue(OptionalValue.of(value = null).valueSet)
        Assert.assertFalse(OptionalValue.empty<Any>().valueSet)
    }


    @Test
    fun getValue() {
        Assert.assertEquals(BigDecimal.ZERO, OptionalValue.of(value = BigDecimal.ZERO).value)
        Assert.assertNull(OptionalValue.of(value = null).value)
    }


    @Test
    fun getValueUnset() {
        thrown.expect(NoSuchElementException::class.java)
        OptionalValue.empty<Any>().value
    }


    @Suppress("ReplaceCallWithComparison")
    @Test
    fun equals() {
        Assert.assertTrue(OptionalValue.of(value = "a") == OptionalValue.of(value = "a"))
        Assert.assertFalse(OptionalValue.of(value = "a") == OptionalValue.of(value = "b"))
        Assert.assertFalse(OptionalValue.of(value = "1").equals(OptionalValue.of(value = 1)))
        Assert.assertTrue(OptionalValue.of(value = null) == OptionalValue.of(value = null))
        // this one works thanks to type erasure
        Assert.assertTrue(OptionalValue.of<Int?>(value = null).equals(OptionalValue.of<String?>(value = null)))
        Assert.assertTrue(OptionalValue.empty<Any>() == OptionalValue.empty<Any>())
    }

    @Test
    fun toStringTest() {
        Assert.assertEquals("OptionalValue(3)", OptionalValue.of(3).toString())
        Assert.assertEquals("OptionalValue(null)", OptionalValue.of(null).toString())
        Assert.assertEquals("OptionalValue(###-VALUE NOT SET-###)", OptionalValue.empty<Int>().toString())
    }
}
