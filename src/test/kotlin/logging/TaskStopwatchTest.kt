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
import org.junit.rules.ExpectedException

class TaskStopwatchTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun start() {
        val startTime = TaskStopwatch.create().start()
        println("Start time $startTime ns")
        Assert.assertTrue(startTime > 0)
    }

    @Test
    fun stop() {
        val duration = TaskStopwatch.createStarted().stop()
        println("Duration time $duration ns")
        Assert.assertTrue(duration > 0)
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
        Assert.assertTrue(duration2 > duration)
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
        Assert.assertTrue(startTime > 0)
    }

    @Test
    fun stopSync() {
        val duration = TaskStopwatch.createStartedSynchronized().stop()
        println("Duration time $duration ns")
        Assert.assertTrue(duration > 0)
    }

    @Test
    fun doubleStartSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("This stopwatch has already been started!")
        TaskStopwatch.createStartedSynchronized().start()
    }

    @Test
    fun doubleStopSync() {
        val stopwatch = TaskStopwatch.createStartedSynchronized()
        println("The stopwatch has been started at ${stopwatch.startTime}")
        val duration = stopwatch.stop()
        println("Duration time $duration ns")
        val duration2 = stopwatch.stop()
        println("Duration 2 time $duration2 ns")
        Assert.assertTrue(duration2 > duration)
    }

    @Test
    fun noStartStopSync() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("This stopwatch has not yet been started!")
        TaskStopwatch.createSynchronized().stop()
    }
}
