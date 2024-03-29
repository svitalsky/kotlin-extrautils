/*
 * Copyright © 2017–2021 Marcel Svitalsky
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
import java.time.LocalDate.now
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
 * Allows adding two closed ranges of LocalDates together, providing the second follows the first
 * without interruption.
 */
operator fun ClosedRange<LocalDate>.plus(other: ClosedRange<LocalDate>): ClosedRange<LocalDate> {
    var firstEnd = endInclusive
    if (++firstEnd != other.start) {
        throw IllegalArgumentException("Cannot add range that is not a direct continuation of the current one!")
    }
    return start..other.endInclusive
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

private const val EFFECTIVE_START = 2001
private const val GOOD_FRIDAY_ADDED = 2016

/*
 * Valid as of 2018.
 * As they create more holidays or change the existing ones, we shall modify this
 * by adding branches for new years, so that old values stay true.
 */
val LocalDate.isCzechHoliday: Boolean
    get() = if (dayOfWeek.value in (6..7)) true
    else when  {
        this.year >= GOOD_FRIDAY_ADDED ->
            when (month) {
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
        this.year >= EFFECTIVE_START ->
            when (month) {
                JANUARY -> dayOfMonth == 1
                MARCH, APRIL -> easterMonday
                MAY -> dayOfMonth == 1 || dayOfMonth == 8
                JULY -> dayOfMonth in (5..6)
                SEPTEMBER -> dayOfMonth == 28
                OCTOBER -> dayOfMonth == 28
                NOVEMBER -> dayOfMonth == 17
                DECEMBER -> dayOfMonth in (24..26)
                else -> false
            }
        else -> throw IllegalArgumentException("This date is not supported!")
    }

val LocalDate.easterFriday: Boolean
    get() = this == year.easterSunday.minusDays(2)

val LocalDate.easterSunday: Boolean
    get() = this == year.easterSunday

val LocalDate.easterMonday: Boolean
    get() = this == year.easterSunday.plusDays(1)

val Int.easterSunday: LocalDate
    get() = if (this < 1 || this > 9999) throw IllegalArgumentException("Year out of bounds!")
    else easterEggs[this] ?: computeEasterSunday().also { easterEggs[this] = it }

private fun Int.computeEasterSunday(): LocalDate {
    val a = this % 19
    val b = this / 100
    val c = this % 100
    val d = (19 * a + b - (b / 4) - ((8 * b + 13) / 25) + 15) % 30
    val e = (a + 11 * d) / 319
    val f = (2 * (b % 4) + 2 * (c / 4) - (c % 4) - d + e + 32) % 7
    val month = (d - e + f + 90) / 25
    val day = (d - e + f + month + 19) % 32
    return LocalDate.of(this, month, day)
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

fun today(): LocalDate =
    now()

val LocalDate.isToday: Boolean
    get() = this == now()

val LocalDate.isYesterday: Boolean
    get() = this == now().minusDays(1)

val LocalDate.isTomorrow: Boolean
    get() = this == now().plusDays(1)

val LocalDate?.orToday: LocalDate
    get() = this ?: today()

val LocalDateTime?.orNow: LocalDateTime
    get() = this ?: LocalDateTime.now()

val httpDatetimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

val ZonedDateTime.httpFormatted
    get() = httpDatetimeFormatter.format(withZoneSameInstant(ZoneId.of("GMT")))

val nowHttpFormatted
    get() = ZonedDateTime.now().httpFormatted

val String.parsedHttpDatetime: ZonedDateTime
    get() = httpDatetimeFormatter.parse(this, ZonedDateTime::from)

val YearMonth.lastDay
    get() = LocalDate.of(year, month, lengthOfMonth())

/**
 * Walks over period given by start and end (defaults to today) by month long steps,
 * returning first and last day of each step.
 */
class MonthlyCursor @JvmOverloads constructor(start: LocalDate, end: LocalDate? = null) : ClosedRange<LocalDate> {

    init {
        assert(!start.isAfter(end ?: now())) {
            "Start cannot be after end!"
        }
    }

    var firstDay = start
        private set

    private val realEnd = end ?: now()
    private val last = YearMonth.from(realEnd)

    private val cursor
        get() = YearMonth.from(firstDay)

    val lastDay
        get() = if (hasNext) cursor.lastDay else realEnd

    val hasNext
        get() = cursor < last

    fun shift() {
        if (hasNext) firstDay = lastDay.plusDays(1)
    }

    override val start
        get() = firstDay

    override val endInclusive
        get() = lastDay
}


data class BetweenComparatorFstHalf<T: Comparable<T>>(val testee: T, val lo: T)

infix fun <T: Comparable<T>> T.between(lo: T) = BetweenComparatorFstHalf(this, lo)

infix fun <T: Comparable<T>> BetweenComparatorFstHalf<T>.and(hi: T): Boolean =
    this.lo <= this.testee && this.testee <= hi
