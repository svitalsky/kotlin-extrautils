/*
 * Copyright © 2017–2021 Marcel Svitalsky
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

import cz.mpts.libs.extrautils.kotlin.NO_VALUE_AVAILABLE
import cz.mpts.libs.extrautils.kotlin.values.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CursorTest {

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
        val cursor = listOf("aaa").cursor()
        assertEquals("aaa", cursor.next())
        val exc = assertThrows(NoSuchElementException::class.java) { cursor.next() }
        assertEquals(NO_VALUE_AVAILABLE, exc.message)
    }


    @Test
    fun peekNonEmpty() {
        val cursor = listOf("aaa").cursor()
        assertEquals("aaa", cursor.peek())
        assertEquals("aaa", cursor.peek())
    }


    @Test
    fun peekEmpty() {
        val cursor = emptyList<Any>().cursor()
        val exc = assertThrows(NoSuchElementException::class.java) { cursor.peek() }
        assertEquals(NO_VALUE_AVAILABLE, exc.message)
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
        val cursor = listOf("aaa").synchronizedCursor()
        assertEquals("aaa", cursor.next())
        val exc = assertThrows(NoSuchElementException::class.java) { cursor.next() }
        assertEquals(NO_VALUE_AVAILABLE, exc.message)
    }


    @Test
    fun peekNonEmptySynced() {
        val cursor = listOf("aaa").synchronizedCursor()
        assertEquals("aaa", cursor.peek())
        assertEquals("aaa", cursor.peek())
    }


    @Test
    fun peekEmptySynced() {
        val cursor = emptyList<Any>().synchronizedCursor()
        val exc = assertThrows(NoSuchElementException::class.java) { cursor.peek() }
        assertEquals(NO_VALUE_AVAILABLE, exc.message)
    }


    @Test
    fun cursorOnNonIterable() {
        val first = LocalDate.of(2018, 4, 1)
        val last = LocalDate.of(2018, 4, 7)
        val dateRange = first..last
        val cursor = dateRange.cursor(dateRange.iterator())
        assertTrue(cursor.hasNext())
        var current = first
        while (cursor.hasNext()) {
            assertEquals(current, cursor.peek())
            assertEquals(current++, cursor.next())
        }
        assertEquals(last, --current)
    }
}
