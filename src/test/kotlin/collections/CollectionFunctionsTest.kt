/*
 * Copyright © 2017–2020 Marcel Svitalsky
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

import org.junit.Assert.*
import org.junit.Test

class CollectionFunctionsKtTest {

    private val doesNot = listOf(1, 2, 3, 4, 5)

    private val doesToo = listOf(1, 2, 3, 4, 5, 1)

    private val testees = listOf(Testee(value = "a"),
                                 Testee(value = "b"),
                                 Testee(value = "a"),
                                 Testee(value = "c"),
                                 Testee(value = "a"),
                                 Testee(value = "b"),
                                 Testee(value = "a"),
                                 Testee(value = "d"),
                                 Testee(value = "e"),
                                 Testee(value = "f"))

    private val others = listOf(Other("a", 1),
                                Other("b", 1),
                                Other("c", 2))

    @Test
    fun hasMultiple() {
        assertFalse(doesNot.hasMultiple())
        assertTrue(doesToo.hasMultiple())
        assertTrue(testees.hasMultiple { it.value })
        assertFalse(others.hasMultiple())
        assertTrue(others.hasMultiple { it.key })
    }

    @Test
    fun multipleOnly() {
        assertTrue(doesNot.multipleOnly().isEmpty())
        assertTrue(doesToo.multipleOnly().contains(1))
        with (testees.multipleOnly { it.value }) {
            assertTrue(contains(Testee("a")))
            assertTrue(contains(Testee("b")))
            assertFalse(contains(Testee("c")))
            assertFalse(contains(Testee("d")))
            assertEquals(setOf(Testee("a"), Testee("b")), this)
        }
        with (others.multipleOnly { it.key }) {
            assertTrue(contains(Other("a", 1)))
            assertEquals(setOf(Other("a", 1), Other("b", 1)), this)
        }
    }

    @Test
    fun singleOnly() {
        assertEquals(doesNot.toSet(), doesNot.singleOnly())
        assertTrue(testees.singleOnly().contains(Testee(value = "e")))
        assertEquals(setOf(2, 3, 4, 5), doesToo.singleOnly())
        assertTrue(testees.singleOnly { it.value }.contains(Testee(value = "f")))
        assertEquals(others.toSet(), others.singleOnly())
        assertEquals(setOf(Other("c", 2)), others.singleOnly { it.key })
    }

    @Test
    fun occurExactlyNTimes() {
        assertTrue(testees.occurExactlyNTimes(n = 2).contains(Testee(value = "b")))
        assertTrue(testees.occurExactlyNTimes(n = 4) { it.value }.contains(Testee("a")))
        assertFalse(testees.occurExactlyNTimes(n = 3).contains(Testee(value = "f")))
        assertTrue(doesNot.occurExactlyNTimes(n = 2).isEmpty())
        assertEquals(setOf(Other("a", 1), Other("b", 1)), others.occurExactlyNTimes(2) { it.key })
    }
}


private data class Testee(val value: String)

private class Other(val value: String, val key: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Other
        return value == other.value
    }

    override fun hashCode() = value.hashCode()
}
