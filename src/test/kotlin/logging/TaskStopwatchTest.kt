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
package cz.mpts.libs.extrautils.kotlin.logging

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
        val startTime = TaskStopwatch.create().start()
        println("Start time $startTime ns")
        assertTrue(startTime > 0)
    }

    @Test
    fun stop() {
        val duration = TaskStopwatch.createStarted().stop()
        println("Duration time $duration ns")
        assertTrue(duration > 0)
    }

    @Test
    fun doubleStart() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("This stopwatch has already been started!")
        TaskStopwatch.createStarted().start()
    }

    @Test
    fun doubleStop() {
        val stopwatch = TaskStopwatch.createStarted()
        println("The stopwatch has been started at ${stopwatch.startTime}")
        val duration = stopwatch.stop()
        println("Duration time $duration ns")
        val duration2 = stopwatch.stop()
        println("Duration 2 time $duration2 ns")
        assertTrue(duration2 > duration)
    }

    @Test
    fun noStartStop() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("This stopwatch has not yet been started!")
        TaskStopwatch.create().stop()
    }

    @Test
    fun startSync() {
        val startTime = TaskStopwatch.createSynchronized().start()
        println("Start time $startTime ns")
        assertTrue(startTime > 0)
    }

    @Test
    fun stopSync() {
        val duration = TaskStopwatch.createSynchronizedStarted().stop()
        println("Duration time $duration ns")
        assertTrue(duration > 0)
    }

    @Test
    fun doubleStartSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("This stopwatch has already been started!")
        TaskStopwatch.createSynchronizedStarted().start()
    }

    @Test
    fun doubleStopSync() {
        val stopwatch = TaskStopwatch.createSynchronizedStarted()
        println("The stopwatch has been started at ${stopwatch.startTime}")
        val duration = stopwatch.stop()
        println("Duration time $duration ns")
        assertTrue(duration > 0)
        val duration2 = stopwatch.stop()
        println("Duration 2 time $duration2 ns")
        assertTrue(duration2 > duration)
    }

    @Test
    fun noStartStopSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("This stopwatch has not yet been started!")
        TaskStopwatch.createSynchronized().stop()
    }

    @Mock
    private val taskStopwatch = Mockito.mock(TaskStopwatch::class.java)

    @Test
    fun formatted() {
        Mockito.`when`(taskStopwatch.stop()).thenReturn(1)
        assertEquals("1 ns", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(11)
        assertEquals("11 ns", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(123)
        assertEquals("123 ns", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(1234)
        assertEquals("1.234 µs", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(12345)
        assertEquals("12.35 µs", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(123456)
        assertEquals("123.5 µs", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(1234567)
        assertEquals("1.235 ms", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(12345678)
        assertEquals("12.35 ms", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(123456789)
        assertEquals("123.5 ms", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(1234567890)
        assertEquals("1.235 s", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(12345678901)
        assertEquals("12.346 s", taskStopwatch.formatDuration())
        Mockito.`when`(taskStopwatch.stop()).thenReturn(123456789012)
        assertEquals("123.457 s", taskStopwatch.formatDuration())
    }
}
