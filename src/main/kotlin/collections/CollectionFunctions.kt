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

/**
 * Returns `true` if this Iterable, grouped by the `keySelector`, has at least
 * one element occurring more than once.
 */
inline fun <T, K> Iterable<T>.hasMultiple(crossinline keySelector: (T) -> K) =
    groupingBy(keySelector)
        .eachCount()
        .any { it.value > 1 }


/**
 * Returns `true` if this Iterable has at least one element occurring more than once.
 */
fun <T> Iterable<T>.hasMultiple() =
    groupingBy { it }
        .eachCount()
        .any { it.value > 1 }


/**
 * Returns the set of those elements of this Iterable that, grouped by the
 * `keySelector`, occur more than once.
 */
inline fun <T, K> Iterable<T>.multipleOnly(crossinline keySelector: (T) -> K) =
    groupBy(keySelector)
        .filterValues { it.size > 1 }
        .values
        .flatten()
        .toSet()


/**
 * Returns the set of those elements of this Iterable that occur more than once.
 */
fun <T> Iterable<T>.multipleOnly() =
    groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys


/**
 * Returns the set of those elements of this Iterable that, grouped by the
 * `keySelector`, occur exactly `n` times.
 */
inline fun <T, K> Iterable<T>.occurExactlyNTimes(n: Int, crossinline keySelector: (T) -> K) =
    groupBy(keySelector)
        .filterValues { it.size == n }
        .values
        .flatten()
        .toSet()


/**
 * Returns the set of those elements of this Iterable that occur exactly `n` times.
 */
fun <T> Iterable<T>.occurExactlyNTimes(n: Int) =
    groupingBy { it }
        .eachCount()
        .filterValues { it == n }
        .keys


/**
 * Returns the set of those elements of this Iterable that, grouped by the
 * `keySelector`, occur just once.
 */
inline fun <T, K> Iterable<T>.singleOnly(crossinline keySelector: (T) -> K) =
        occurExactlyNTimes(n = 1, keySelector = keySelector)


/**
 * Returns the set of those elements of this Iterable that occur just once.
 */
fun <T> Iterable<T>.singleOnly() = occurExactlyNTimes(n = 1)
