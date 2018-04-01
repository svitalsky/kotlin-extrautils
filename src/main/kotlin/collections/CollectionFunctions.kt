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


inline fun <T, K> Iterable<T>.hasMultiple(crossinline keySelector: (T) -> K) =
    groupingBy(keySelector)
        .eachCount()
        .any { it.value > 1 }


fun <T> Iterable<T>.hasMultiple() =
    groupingBy { it }
        .eachCount()
        .any { it.value > 1 }


inline fun <T, K> Iterable<T>.multipleOnly(crossinline keySelector: (T) -> K) =
    groupingBy(keySelector)
        .eachCount()
        .filterValues { it > 1 }
        .keys


fun <T> Iterable<T>.multipleOnly() =
    groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys
