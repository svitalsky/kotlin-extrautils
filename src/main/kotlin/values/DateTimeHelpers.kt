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

import cz.mpts.libs.extrautils.kotlin.NO_MORE_DAYS_AVAILABLE
import cz.mpts.libs.extrautils.kotlin.collections.Cursor
import java.time.LocalDate

/**
 * Adds one day so that we can use ++ operator on LocalDate.
 */
@Suppress("HasPlatformType")
operator fun LocalDate.inc() = this.plusDays(1)

/**
 * Removes one day so that we can use -- operator on LocalDate.
 */
@Suppress("HasPlatformType")
operator fun LocalDate.dec() = minusDays(1)

/**
 * Adds an Iterator for a LocalDate ClosedRange.
 */
operator fun ClosedRange<LocalDate>.iterator() =
    object : Iterator<LocalDate> {
        private var current = start

        override fun hasNext() = ! current.isAfter(endInclusive)

        override fun next() =
            if (hasNext()) current++
            else throw NoSuchElementException(NO_MORE_DAYS_AVAILABLE)
    }


/**
 * Adds a Cursor for a LocalDate ClosedRange.
 * (Probably not very useful as we know the next value anyway so the
 * `peek()` functionality is not really needed here.)
 */
fun ClosedRange<LocalDate>.cursor() =
        object : Cursor<LocalDate> {
            private var current = start

            override fun hasNext() = ! current.isAfter(endInclusive)

            override fun next() =
                if (hasNext()) current++
                else throw NoSuchElementException(NO_MORE_DAYS_AVAILABLE)

            override fun peek() =
                if (hasNext()) current
                else throw NoSuchElementException(NO_MORE_DAYS_AVAILABLE)
        }
