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
package cz.mpts.libs.extrautils.kotlin.collections

import cz.mpts.libs.extrautils.kotlin.values.*
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.ExpectedException
import java.time.LocalDate

class CursorTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun hasNext() {
        assertFalse(emptyList<Any>().cursor().hasNext())
        val cursor = listOf("aaa").cursor()
        assertTrue(cursor.hasNext())
        cursor.next()
        assertFalse(cursor.hasNext())
    }


    @Test
    fun nextOK() {
        val list = listOf("aaa", null, "bbb")
        val cursor = list.cursor()
        list.forEach {
            assertTrue(it == cursor.next())
        }
    }


    @Test
    fun nextNOK() {
        thrown.expect(NoSuchElementException::class.java)
        val cursor = listOf("aaa").cursor()
        assertEquals("aaa", cursor.next())
        cursor.next()
    }


    @Test
    fun peekNonEmpty() {
        val cursor = listOf("aaa").cursor()
        assertEquals("aaa", cursor.peek())
        assertEquals("aaa", cursor.peek())
    }


    @Test
    fun peekEmpty() {
        thrown.expect(NoSuchElementException::class.java)
        val cursor = emptyList<Any>().cursor()
        cursor.peek()
    }

    @Test
    fun hasNextSynced() {
        assertFalse(emptyList<Any>().synchronizedCursor().hasNext())
        val cursor = listOf("aaa").synchronizedCursor()
        assertTrue(cursor.hasNext())
        cursor.next()
        assertFalse(cursor.hasNext())
    }


    @Test
    fun nextOKSynced() {
        val list = listOf("aaa", null, "bbb")
        val cursor = list.synchronizedCursor()
        list.forEach {
            assertTrue(it == cursor.next())
        }
    }


    @Test
    fun nextNOKSynced() {
        thrown.expect(NoSuchElementException::class.java)
        val cursor = listOf("aaa").synchronizedCursor()
        assertEquals("aaa", cursor.next())
        cursor.next()
    }


    @Test
    fun peekNonEmptySynced() {
        val cursor = listOf("aaa").synchronizedCursor()
        assertEquals("aaa", cursor.peek())
        assertEquals("aaa", cursor.peek())
    }


    @Test
    fun peekEmptySynced() {
        thrown.expect(NoSuchElementException::class.java)
        val cursor = emptyList<Any>().synchronizedCursor()
        cursor.peek()
    }


    @Test
    fun cursorOnNonIterable() {
        val first = LocalDate.of(2018, 4, 1)
        val last = LocalDate.of(2018, 4, 7)
        val datedRange = first..last
        val cursor = datedRange.cursor(datedRange.iterator())
        assertTrue(cursor.hasNext())
        var current = first
        while (cursor.hasNext()) {
            assertEquals(current, cursor.peek())
            assertEquals(current++, cursor.next())
        }
        assertEquals(last, --current)
    }
}
