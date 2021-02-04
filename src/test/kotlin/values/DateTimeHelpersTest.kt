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
package cz.mpts.libs.extrautils.kotlin.values

import cz.mpts.libs.extrautils.kotlin.*
import org.junit.Assert.*
import org.junit.Test
import java.time.*
import java.time.LocalDate.now
import java.time.Month.*

class DateTimeHelpersTest {

    @Test
    fun inc() {
        var date = LocalDate.of(2018, 3, 29)
        assertEquals(LocalDate.of(2018, 3, 30), ++date)
        date++
        assertEquals(LocalDate.of(2018, 3, 31), date)
    }


    @Test
    fun dec() {
        var date = LocalDate.of(2018, 3, 29)
        assertEquals(LocalDate.of(2018, 3, 28), --date)
        date--
        assertEquals(LocalDate.of(2018, 3, 27), date)
    }


    @Test
    fun ymInc() {
        var ym = YearMonth.of(2018, 5)
        assertEquals(YearMonth.of(2018, 6), ++ym)
        assertEquals(YearMonth.of(2018, 6), ym++)
        assertEquals(YearMonth.of(2018, 7), ym)
    }


    @Test
    fun ymDec() {
        var ym = YearMonth.of(2018, 5)
        assertEquals(YearMonth.of(2018, 4), --ym)
        assertEquals(YearMonth.of(2018, 4), ym--)
        assertEquals(YearMonth.of(2018, 3), ym)
    }


    @Test
    fun iterator() {
        val first = LocalDate.of(2018, 3, 30)
        var last = LocalDate.of(2018, 4, 10)
        val range = first..last
        var expected = first
        for (day in range) {
            assertEquals(expected, day)
            expected++
        }
        assertEquals(++last, expected)
    }


    @Test
    fun iteratorOverflow() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 31)
        val iterator = (first..last).iterator()
        for (ignore in (1..2)) iterator.next()
        val exc = assertThrows(NoSuchElementException::class.java) { iterator.next() }
        assertEquals(NO_MORE_DAYS_AVAILABLE, exc.message)
    }


    @Test
    fun iteratorOnEmpty() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 29)
        val iterator = (first..last).iterator()
        val exc = assertThrows(NoSuchElementException::class.java) { iterator.next() }
        assertEquals(NO_MORE_DAYS_AVAILABLE, exc.message)
    }


    @Test
    fun iteratorInSingle() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 30)
        val iterator = (first..last).iterator()
        assertTrue(iterator.hasNext())
        assertEquals(first, iterator.next())
        assertFalse(iterator.hasNext())
    }


    @Test
    fun iteratorYM() {
        val first = YearMonth.of(2018, 5)
        var last = YearMonth.of(2018, 11)
        val range = first..last
        var expected = first
        for (month in range) {
            assertEquals(expected, month)
            expected++
        }
        assertEquals(++last, expected)
    }


    @Test
    fun iteratorOverflowYM() {
        val first = YearMonth.of(2018, 5)
        val last = YearMonth.of(2018, 6)
        val iterator = (first..last).iterator()
        for (ignore in (1..2)) iterator.next()
        val exc = assertThrows(NoSuchElementException::class.java) { iterator.next() }
        assertEquals(NO_MORE_MONTHS_AVAILABLE, exc.message)
    }


    @Test
    fun iteratorOnEmptyYM() {
        val first = YearMonth.of(2018, 6)
        val last = YearMonth.of(2018, 5)
        val iterator = (first..last).iterator()
        val exc = assertThrows(NoSuchElementException::class.java) { iterator.next() }
        assertEquals(NO_MORE_MONTHS_AVAILABLE, exc.message)
    }


    @Test
    fun iteratorInSingleYM() {
        val first = YearMonth.of(2018, 5)
        val last = YearMonth.of(2018, 5)
        val iterator = (first..last).iterator()
        assertTrue(iterator.hasNext())
        assertEquals(first, iterator.next())
        assertFalse(iterator.hasNext())
    }


    @Test
    fun cursor() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 4, 10)
        val range = first..last
        var expected = first
        val cursor = range.cursor()
        while (cursor.hasNext()) {
            assertEquals(expected, cursor.peek())
            assertEquals(expected++, cursor.next())
        }
    }


    @Test
    fun cursorOverflow() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 31)
        val cursor = (first..last).cursor()
        for (ignore in (1..2)) cursor.next()
        val exc = assertThrows(NoSuchElementException::class.java) { cursor.next() }
        assertEquals(NO_MORE_DAYS_AVAILABLE, exc.message)
    }


    @Test
    fun cursorOnEmpty() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 29)
        val cursor = (first..last).cursor()
        val exc = assertThrows(NoSuchElementException::class.java) { cursor.peek() }
        assertEquals(NO_MORE_DAYS_AVAILABLE, exc.message)
    }


    @Test
    fun cursorInSingle() {
        val first = LocalDate.of(2018, 3, 30)
        val last = LocalDate.of(2018, 3, 30)
        val cursor = (first..last).cursor()
        assertTrue(cursor.hasNext())
        assertEquals(first, cursor.peek())
        assertEquals(first, cursor.next())
        assertFalse(cursor.hasNext())
    }

    @Test
    fun czechHoliday() {
        var cursor = LocalDate.of(2019, 1, 1)
        while (cursor.year == 2019) {
            if ((cursor.dayOfWeek.value in (6..7))) assertTrue(cursor.isCzechHoliday)
            else with (cursor) {
                when (month!!) {
                    JANUARY -> assertTrue(dayOfMonth == 1 || !isCzechHoliday)
                    FEBRUARY, MARCH, JUNE, AUGUST -> assertFalse(isCzechHoliday)
                    APRIL -> assertTrue(dayOfMonth == 19 || dayOfMonth == 22 || !isCzechHoliday)
                    MAY -> assertTrue(dayOfMonth == 1 || dayOfMonth == 8 || !isCzechHoliday)
                    JULY -> assertTrue(dayOfMonth in (5..6) || !isCzechHoliday)
                    SEPTEMBER, OCTOBER -> assertTrue(dayOfMonth == 28 || !isCzechHoliday)
                    NOVEMBER -> assertTrue(dayOfMonth == 17 || !isCzechHoliday)
                    DECEMBER -> assertTrue(dayOfMonth in (24..26) || !isCzechHoliday)
                }
            }
            cursor++
        }
    }

    @Test
    fun easter() {
        assertTrue(LocalDate.of(2019, 4, 19).easterFriday)
        assertTrue(LocalDate.of(2019, 4, 21).easterSunday)
        assertTrue(LocalDate.of(2019, 4, 22).easterMonday)
        assertEquals(LocalDate.of(2018, 4, 1), 2018.easterSunday)
        assertEquals(LocalDate.of(2019, 4, 21), 2019.easterSunday)
    }

    @Test
    fun easterWrongYearLo() {
        val exc = assertThrows(IllegalArgumentException::class.java) { 0.easterSunday }
        assertEquals("Year out of bounds!", exc.message)
    }

    @Test
    fun easterWrongYearHi() {
        val exc = assertThrows(IllegalArgumentException::class.java) { 10_000.easterSunday }
        assertEquals("Year out of bounds!", exc.message)
    }

    @Test
    fun nextCzechWeekDay() {
        for (year in (2019..2100)) {
            val yearEaster = year.easterSunday
            assertEquals(yearEaster.plusDays(2), yearEaster.nextCzechWeekDay)
            val yearChristmasEve = LocalDate.of(year, 12, 24)
            var candidate = yearChristmasEve.plusDays(3)
            while (candidate.dayOfWeek.value in (6..7)) candidate++
            assertEquals(candidate, yearChristmasEve.nextCzechWeekDay)
        }
    }

    @Test
    fun previousCzechWeekDay() {
        for (year in (2019..2100)) {
            val yearEaster = year.easterSunday
            assertEquals(yearEaster.minusDays(3), yearEaster.previousCzechWeekDay)
            val yearChristmasEve = LocalDate.of(year, 12, 24)
            var candidate = yearChristmasEve.minusDays(1)
            while (candidate.dayOfWeek.value in (6..7)) candidate--
            assertEquals(candidate, yearChristmasEve.previousCzechWeekDay)
        }
    }

    @Test
    fun httpFormatted() {
        val winterTime = LocalDateTime.of(2018, 12, 24, 15, 0, 0)
        var zoned = ZonedDateTime.of(winterTime, ZoneId.of("Europe/Prague"))
        assertEquals("Mon, 24 Dec 2018 14:00:00 GMT", zoned.httpFormatted)
        val summerTime = LocalDateTime.of(2018, 7, 25, 15, 0, 0)
        zoned = ZonedDateTime.of(summerTime, ZoneId.of("Europe/Prague"))
        assertEquals("Wed, 25 Jul 2018 13:00:00 GMT", zoned.httpFormatted)
    }

    @Test
    fun nowHttpFormattedTest() {
        // pseudo-test: might often fail around midnights and so on...
        val before = nowHttpFormatted
        Thread.sleep(1000L)
        val after = nowHttpFormatted
        assertTrue(after > before)
    }

    @Test
    fun parsedHttpDatetimeTest() {
        val zoned = ZonedDateTime.of(2018, 12, 26, 18, 14, 23, 0, ZoneId.of("Europe/Prague"))
        assertEquals(zoned.withZoneSameInstant(ZoneId.of("GMT")), zoned.httpFormatted.parsedHttpDatetime)
    }

    @Test
    fun monthlyCursorDefaultEnd() {
        val today = now()
        var cursor = MonthlyCursor(start = today.minusDays(60))
        assertTrue(cursor.hasNext)
        while (cursor.hasNext) cursor.shift()
        assertEquals(today.withDayOfMonth(1), cursor.firstDay)
        assertEquals(today, cursor.lastDay)
        cursor = MonthlyCursor(start = today)
        assertFalse(cursor.hasNext)
        assertEquals(today, cursor.lastDay)
    }

    @Test
    fun monthlyCursorGivenEnd() {
        val cursor = MonthlyCursor(start = LocalDate.of(2020, 1, 15), end = LocalDate.of(2020, 3, 25))
        assertEquals(15, cursor.firstDay.dayOfMonth)
        assertTrue(cursor.hasNext)
        cursor.shift()
        assertEquals(1, cursor.firstDay.dayOfMonth)
        assertEquals(29, cursor.lastDay.dayOfMonth)
        assertTrue(cursor.hasNext)
        cursor.shift()
        assertFalse(cursor.hasNext)
        assertEquals(25, cursor.lastDay.dayOfMonth)
    }

    @Test
    fun yearMonthLastDay() {
        assertEquals(31, YearMonth.of(2020, 1).lastDay.dayOfMonth)
        assertEquals(29, YearMonth.of(2020, 2).lastDay.dayOfMonth)
    }

    @Test
    fun wrongMonthlyCursor() {
        val exc = assertThrows(AssertionError::class.java) { MonthlyCursor(start = now().plusDays(33)) }
        assertEquals("Start cannot be after end!", exc.message)
    }

    @Test
    fun isYesterday() {
        assertFalse(now().isYesterday)
        assertTrue(now().minusDays(1).isYesterday)
    }

    @Test
    fun isTomorrow() {
        assertFalse(now().isTomorrow)
        assertTrue(now().plusDays(1).isTomorrow)
    }
}
