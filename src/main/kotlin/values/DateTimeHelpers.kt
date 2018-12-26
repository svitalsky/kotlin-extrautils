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
@file:Suppress("HasPlatformType")

package cz.mpts.libs.extrautils.kotlin.values

import cz.mpts.libs.extrautils.kotlin.*
import cz.mpts.libs.extrautils.kotlin.collections.Cursor
import java.time.*
import java.time.Month.*
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Adds one day so that we can use ++ operator on LocalDate.
 */
operator fun LocalDate.inc() = this.plusDays(1)

/**
 * Removes one day so that we can use -- operator on LocalDate.
 */
operator fun LocalDate.dec() = minusDays(1)


/**
 * Adds one month so that we can use ++ operator on YearMonth.
 */
operator fun YearMonth.inc() = this.plusMonths(1)

/**
 * Removes one month so that we can use -- operator on YearMonth.
 */
operator fun YearMonth.dec() = this.minusMonths(1)


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
 * Adds an Iterator for a YearMonth ClosedRange.
 */
@JvmName("yearMonthIterator")
operator fun ClosedRange<YearMonth>.iterator() =
    object : Iterator<YearMonth> {
        private var current = start

        override fun hasNext() = ! current.isAfter(endInclusive)

        override fun next() =
            if (hasNext()) current++
            else throw NoSuchElementException(NO_MORE_MONTHS_AVAILABLE)
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

/*
 * Valid as of 2018.
 * As they create more holidays or change the existing ones, we shall modify this
 * by adding branches for new years, so that old values stay true.
 */
val LocalDate.isCzechHoliday: Boolean
    get() = if (dayOfWeek.value in (6..7)) true
    else when (month) {
        JANUARY -> dayOfMonth == 1
        MARCH, APRIL -> easterFriday || easterMonday
        MAY -> dayOfMonth == 1 || dayOfMonth == 8
        JULY -> dayOfMonth in (5..6)
        SEPTEMBER -> dayOfMonth == 28
        OCTOBER -> dayOfMonth == 28
        NOVEMBER -> dayOfMonth == 17
        DECEMBER -> dayOfMonth in (24..26)
        else -> false
    }

val LocalDate.easterFriday: Boolean
    get() = this == year.easterSunday.minusDays(2)

val LocalDate.easterSunday: Boolean
    get() = this == year.easterSunday

val LocalDate.easterMonday: Boolean
    get() = this == year.easterSunday.plusDays(1)

val Int.easterSunday: LocalDate
    get() {
        if (this < 1 || this > 9999) throw IllegalArgumentException("Year out of bounds!")
        var result = easterEggs[this]
        if (result != null) return result
        val a = this % 19
        val b = this / 100
        val c = this % 100
        val d = b / 4
        val e = b % 4
        val g = (8 * b + 13) / 25
        val h = (19 * a + b - d - g + 15) % 30
        val j = c / 4
        val k = c % 4
        val m = (a + 11 * h) / 319
        val r = (2 * e + 2 * j - k - h + m + 32) % 7
        val n = (h - m + r + 90) / 25
        val p = (h - m + r + n + 19) % 32
        result = LocalDate.of(this, n, p)
        return result.also { easterEggs[this] = result }
    }

private val easterEggs = WeakHashMap<Int, LocalDate>()

val LocalDate.nextCzechWeekDay: LocalDate
    get() = if (this.isCzechHoliday) {
        var cursor = this.plusDays(1)
        while (cursor.isCzechHoliday) cursor++
        cursor
    } else this

val LocalDate.previousCzechWeekDay: LocalDate
    get() = if (this.isCzechHoliday) {
        var cursor = this.minusDays(1)
        while (cursor.isCzechHoliday) cursor--
        cursor
    } else this

val ZonedDateTime.httpFormatted
    get() = DateTimeFormatter
        .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
        .format(withZoneSameInstant(ZoneId.of("GMT")))

val nowHttpFormatted
    get() = ZonedDateTime.now().httpFormatted
