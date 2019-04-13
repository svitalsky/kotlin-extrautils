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
@file:Suppress("DEPRECATION")

package cz.mpts.libs.extrautils.kotlin.values

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN
import kotlin.test.assertTrue

class NumberHelpersKtTest {

    @Test
    fun isZero() {
        assertTrue(ZERO.isZero)
        assertTrue(BigDecimal.valueOf(0L).isZero)
        assertTrue(BigDecimal.valueOf(0.0).isZero)
        assertTrue(BigDecimal.valueOf(0, 10).isZero)
        assertTrue(BigDecimal("0.000").isZero)
        assertFalse(ONE.isZero)
    }

    @Test
    fun isPositive() {
        assertFalse(ZERO.isPositive)
        assertFalse(BigDecimal.valueOf(0L, 10).isPositive)
        assertTrue(ONE.isPositive)
        assertFalse(BigDecimal.valueOf(-55).isPositive)
    }

    @Test
    fun isNegative() {
        assertFalse(ZERO.isNegative)
        assertFalse(BigDecimal.valueOf(0L, 10).isNegative)
        assertFalse(ONE.isNegative)
        assertTrue(ONE.negate().isNegative)
        assertTrue(BigDecimal.valueOf(-55).isNegative)
    }

    @Test
    fun positiveOrZero() {
        assertEquals(TEN, TEN.positiveOrZero)
        assertEquals(ZERO, TEN.negate().positiveOrZero)
    }

    @Test
    fun negativeOrZero() {
        assertEquals(ZERO, TEN.negativeOrZero)
        assertEquals(TEN.negate(), TEN.negate().negativeOrZero)
    }

    @Test
    fun isGreaterThan() {
        assertTrue(TEN isGreaterThan ONE)
        assertTrue(ONE isGreaterThan ZERO)
        assertTrue(ONE isGreaterThan ONE.negate())
        assertFalse(ZERO isGreaterThan ZERO.negate())
    }

    @Test
    fun isLessThan() {
        assertTrue(ONE isLessThan  TEN)
        assertTrue(ZERO isLessThan  ONE)
        assertTrue(ONE.negate() isLessThan  ONE)
        assertFalse(ZERO isLessThan  ZERO.negate())
    }

    @Test
    fun isEqualTo() {
        assertTrue(ONE isEqualTo BigDecimal.valueOf(1.00000))
        assertTrue(ZERO isEqualTo BigDecimal.valueOf(0.000))
        assertTrue(TEN isEqualTo BigDecimal.valueOf(1.00000).multiply(BigDecimal.valueOf(10.0000000000)))
        assertTrue(TEN isEqualTo ONE.multiply(TEN))
        assertFalse(ONE isEqualTo ZERO)
        assertFalse(ONE isEqualTo ONE.negate())
        assertFalse(ONE isEqualTo BigDecimal.valueOf(1.00000001))
    }

    @Test
    fun isNotEqualTo() {
        assertFalse(ONE isNotEqualTo BigDecimal.valueOf(1.00000))
        assertFalse(ZERO isNotEqualTo BigDecimal.valueOf(0.000))
        assertFalse(TEN isNotEqualTo BigDecimal.valueOf(1.00000).multiply(BigDecimal.valueOf(10.0000000000)))
        assertFalse(TEN isNotEqualTo ONE.multiply(TEN))
        assertTrue(ONE isNotEqualTo ZERO)
        assertTrue(ONE isNotEqualTo ONE.negate())
        assertTrue(ONE isNotEqualTo BigDecimal.valueOf(1.00000001))
    }
}
