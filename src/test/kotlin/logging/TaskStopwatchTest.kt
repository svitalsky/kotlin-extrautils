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
@file:Suppress("DEPRECATION")

package cz.mpts.libs.extrautils.kotlin.logging

import cz.mpts.libs.extrautils.kotlin.*
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.ExpectedException
import org.mockito.*

class TaskStopwatchTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun start() {
        val stopwatch = TaskStopwatch.create()
        assertFalse(stopwatch.started)
        stopwatch.start()
        assertTrue(stopwatch.started)
    }

    @Test
    fun stop() {
        val duration = TaskStopwatch.createStarted().stop()
        assertTrue(duration > 0)
    }

    @Test
    fun time() {
        val duration = TaskStopwatch.createStarted().time()
        assertTrue(duration > 0)
    }

    @Test
    fun doubleStart() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage(STOPWATCH_ALREADY_STARTED)
        TaskStopwatch.createStarted().start()
    }

    @Test
    fun doubleStop() {
        val stopwatch = TaskStopwatch.createStarted()
        assertTrue(stopwatch.started)
        val duration = stopwatch.stop()
        assertTrue(duration > 0)
        val duration2 = stopwatch.stop()
        assertTrue(duration2 > duration)
    }

    @Test
    fun doubleTime() {
        val stopwatch = TaskStopwatch.createStarted()
        assertTrue(stopwatch.started)
        val duration = stopwatch.time()
        assertTrue(duration > 0)
        val duration2 = stopwatch.time()
        assertTrue(duration2 > duration)
    }

    @Test
    fun noStartStop() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage(STOPWATCH_NOT_YET_STARTED)
        TaskStopwatch.create().stop()
    }

    @Test
    fun noStartTime() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage(STOPWATCH_NOT_YET_STARTED)
        TaskStopwatch.create().time()
    }

    @Test
    fun startSync() {
        val stopwatch = TaskStopwatch.createSynchronized()
        assertFalse(stopwatch.started)
        stopwatch.start()
        assertTrue(stopwatch.started)
    }

    @Test
    fun stopSync() {
        val duration = TaskStopwatch.createSynchronizedStarted().stop()
        assertTrue(duration > 0)
    }

    @Test
    fun timeSync() {
        val duration = TaskStopwatch.createSynchronizedStarted().time()
        assertTrue(duration > 0)
    }

    @Test
    fun doubleStartSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage(STOPWATCH_ALREADY_STARTED)
        TaskStopwatch.createSynchronizedStarted().start()
    }

    @Test
    fun doubleStopSync() {
        val stopwatch = TaskStopwatch.createSynchronizedStarted()
        assertTrue(stopwatch.started)
        val duration = stopwatch.stop()
        assertTrue(duration > 0)
        val duration2 = stopwatch.stop()
        assertTrue(duration2 > duration)
    }

    @Test
    fun doubleTimeSync() {
        val stopwatch = TaskStopwatch.createSynchronizedStarted()
        assertTrue(stopwatch.started)
        val duration = stopwatch.time()
        assertTrue(duration > 0)
        val duration2 = stopwatch.time()
        assertTrue(duration2 > duration)
    }

    @Test
    fun noStartStopSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage(STOPWATCH_NOT_YET_STARTED)
        TaskStopwatch.createSynchronized().stop()
    }

    @Test
    fun noStartTimeSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage(STOPWATCH_NOT_YET_STARTED)
        TaskStopwatch.createSynchronized().time()
    }

    @Mock
    private lateinit var taskStopwatch: TaskStopwatch

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun formatted() {
        assertFormatting(duration = 1, expected = "1 ns")
        assertFormatting(duration = 12, expected = "12 ns")
        assertFormatting(duration = 123, expected = "123 ns")
        assertFormatting(duration = 1234, expected = "1.234 µs")
        assertFormatting(duration = 12345, expected = "12.35 µs")
        assertFormatting(duration = 123456, expected = "123.5 µs")
        assertFormatting(duration = 1234567, expected = "1.235 ms")
        assertFormatting(duration = 12345678, expected = "12.35 ms")
        assertFormatting(duration = 123456789, expected = "123.5 ms")
        assertFormatting(duration = 1234567890, expected = "1.235 s")
        assertFormatting(duration = 12345678901, expected = "12.346 s")
        assertFormatting(duration = 123456789012, expected = "123.457 s")
    }

    private fun assertFormatting(duration: Long, expected: String) {
        Mockito.`when`(taskStopwatch.time()).thenReturn(duration)
        assertEquals(expected, taskStopwatch.formatDuration())
    }

    @Test
    fun formattedLong() {
        assertFormattingLong(duration = 12345, expected = "12.35 µs")
        assertFormattingLong(duration = 23456789012, expected = "23.46 s")
        assertFormattingLong(duration = 123456789012, expected = "2 m 3.46 s")
        assertFormattingLong(duration = 34123456789012, expected = "9 h 28 m 43.46 s")
        assertFormattingLong(duration = (14 * (24 * 3_600) + 6 * 3_600 + 48 * 60 + 43) * 1_000_000_000L + 456789012,
                             expected = "14 d 6 h 48 m 43.46 s")
    }

    private fun assertFormattingLong(duration: Long, expected: String) {
        Mockito.`when`(taskStopwatch.time()).thenReturn(duration)
        assertEquals(expected, taskStopwatch.formatLongDuration())
    }
}
