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

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class NumberHelpersKtTest {

    @Test
    fun isZero() {
        assertTrue(BigDecimal.ZERO.isZero)
        assertTrue(BigDecimal.valueOf(0L).isZero)
        assertTrue(BigDecimal.valueOf(0.0).isZero)
        assertTrue(BigDecimal.valueOf(0, 10).isZero)
        assertTrue(BigDecimal("0.000").isZero)
        assertFalse(BigDecimal.ONE.isZero)
    }
}
