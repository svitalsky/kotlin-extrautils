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

import org.junit.Assert.*
import org.junit.Test

class CollectionFunctionsKtTest {

    private val testees = listOf(Testee("a"),
                                 Testee("b"),
                                 Testee("a"),
                                 Testee("c"),
                                 Testee("a"),
                                 Testee("b"),
                                 Testee("a"),
                                 Testee("d"))

    @Test
    fun hasMultiple() {
        val doesNot = listOf(1, 2, 3, 4, 5)
        assertFalse(doesNot.hasMultiple { it })
        assertTrue(testees.hasMultiple { it.value })
    }


    @Test
    fun multipleOnly() {
        assertTrue(testees.multipleOnly { it.value }.contains("a"))
        assertTrue(testees.multipleOnly { it.value }.contains("b"))
        assertFalse(testees.multipleOnly { it.value }.contains("c"))
        assertFalse(testees.multipleOnly { it.value }.contains("d"))
    }
}


private class Testee(val value: String)
