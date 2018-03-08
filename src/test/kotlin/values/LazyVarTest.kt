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

import org.junit.Test

import org.junit.Assert.*

class LazyVarTest {

    private val intIt = (1..10).toList().iterator()
    private val testee: LazyVar<Int> = LazyVar { intIt.next() }

    @Test
    fun getValue() {
        assertEquals(1, testee.value)
        assertEquals(1, testee.value)
        assertEquals(1, testee.getAndReset())
        assertEquals(2, testee.value)
        assertEquals(2, testee.getAndReset())
        assertEquals(3, testee.value)
    }
}
