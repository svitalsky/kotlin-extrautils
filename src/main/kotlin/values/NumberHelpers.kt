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
@file:Suppress("HasPlatformType", "DeprecatedCallableAddReplaceWith")

package cz.mpts.libs.extrautils.kotlin.values

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

/**
 * A shortcut for so often used a comparison
 */
val BigDecimal.isZero
    get() = compareTo(ZERO) == 0

val BigDecimal.isPositive
    get() = signum() == 1

val BigDecimal.isNegative
    get() = signum() == -1

val BigDecimal.positiveOrZero
    get() = if (isPositive) this else ZERO

val BigDecimal.negativeOrZero
    get() = if (isNegative) this else ZERO

@Deprecated("There is actually an overloaded operator in the stdlib.")
infix fun BigDecimal.isGreaterThan(other: BigDecimal) = compareTo(other) > 0

@Deprecated("There is actually an overloaded operator in the stdlib.")
infix fun BigDecimal.isLessThan(other: BigDecimal) = compareTo(other) < 0

infix fun BigDecimal.isEqualTo(other: BigDecimal) = compareTo(other) == 0

infix fun BigDecimal.isNotEqualTo(other: BigDecimal) = compareTo(other) != 0
