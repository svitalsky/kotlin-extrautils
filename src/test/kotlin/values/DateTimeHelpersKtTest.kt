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
import org.junit.Assert.*
import org.junit.Test
import org.junit.rules.ExpectedException
import java.time.LocalDate

class DateTimeHelpersKtTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun inc() {
        var date = LocalDate.of(2018, 3, 29)
        assertEquals(LocalDate.of(2018, 3, 30), ++date)
        date++
        assertEquals(LocalDate.of(2018, 3, 31), date)
    }


    @Test
    fun dec() {
        var date = LocalDate.of(2018, 3, 29)
        assertEquals(LocalDate.of(2018, 3, 28), --date)
        date--
        assertEquals(LocalDate.of(2018, 3, 27), date)
    }


    @Test
    fun iterator() {
        val first = LocalDate.of(2018, 3, 30)
        var last = LocalDate.of(2018, 4, 1)
        val range = first..last
        var expected = first
        for (day in range) {
            assertEquals(expected, day)
            expected++
        }
        assertEquals(++last, expected)
    }


    @Test
    fun iteratorOverflow() {
        thrown.expect(NoSuchElementException::class.java)
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 31)
        val iterator = (first..last).iterator()
        for (ignore in (1..5)) iterator.next()
    }


    @Test
    fun iteratorOnEmpty() {
        thrown.expect(NoSuchElementException::class.java)
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 29)
        val iterator = (first..last).iterator()
        iterator.next()
    }


    @Test
    fun iteratorInSingle() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 30)
        val iterator = (first..last).iterator()
        assertTrue(iterator.hasNext())
        assertEquals(first, iterator.next())
        assertFalse(iterator.hasNext())
    }
}