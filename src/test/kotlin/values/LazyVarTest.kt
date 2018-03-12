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
import org.junit.Assert.assertEquals

class LazyVarTest {

    private lateinit var intIt: Iterator<Int>

    @Before
    fun init() {
        intIt = (1..10).toList().iterator()
    }

    @Test
    fun getValue() {
        val testee: LazyVar<Int> = LazyVar.by { intIt.next() }
        assertEquals(1, testee.value)
        assertEquals(1, testee.value)
        assertEquals(1, testee.getAndReset())
        assertEquals(2, testee.value)
        assertEquals(2, testee.getAndReset())
        assertEquals(3, testee.value)
    }

    @Test
    fun getValueSynced() {
        val testeeSynced: LazyVar<Int> = LazyVar.synchronized { intIt.next() }
        assertEquals(1, testeeSynced.value)
        assertEquals(1, testeeSynced.value)
        assertEquals(1, testeeSynced.getAndReset())
        assertEquals(2, testeeSynced.value)
        assertEquals(2, testeeSynced.getAndReset())
        assertEquals(3, testeeSynced.value)
    }
}
