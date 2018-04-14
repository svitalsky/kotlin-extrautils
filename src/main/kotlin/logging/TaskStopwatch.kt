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

import cz.mpts.libs.extrautils.kotlin.sync

interface TaskStopwatch {

    companion object {
        fun create(): TaskStopwatch = TaskStopwatchBasic()
        fun createStarted(): TaskStopwatch = TaskStopwatchBasic(started = true)
        fun createSynchronized(): TaskStopwatch = SynchronizedTaskStopwatch()
        fun createSynchronizedStarted(): TaskStopwatch = SynchronizedTaskStopwatch(started = true)
    }

    val startTime: Long

    /**
     * Starts the stopwatch and returns the start time in nanoseconds.
     *
     * @return start time in nanoseconds
     * @throws IllegalStateException in case of calling on already started stopwatch
     */
    fun start(): Long

    /**
     * Returns the time this stopwatch has been running in nanoseconds.
     * May be called repeatedly.
     *
     * @return running time in nanoseconds
     * @throws IllegalStateException in case of calling on not yet started stopwatch.
     */
    fun stop(): Long

    /**
     * Returns the time this stopwatch has been running nicely formatted.
     *
     * @return running time nicely formatted
     * @throws IllegalStateException in case of calling on not yet started stopwatch.
     */
    fun formatted(): String
}


private open class TaskStopwatchBasic(started: Boolean = false) : TaskStopwatch {

    final override var startTime = if (started) System.nanoTime() else 0L
        private set

    override fun start() =
        if (startTime == 0L) { startTime = System.nanoTime(); startTime }
        else throw IllegalStateException("This stopwatch has already been started!")

    override fun stop() =
        if (startTime > 0) System.nanoTime() - startTime
        else throw IllegalStateException("This stopwatch has not yet been started!")

    override fun formatted() = formatDuration()
}


internal fun TaskStopwatch.formatDuration(): String {
    val result = stop()
    return when {
        result < 1_000 -> "$result ns"
        result < 10_000 -> "${"%.3f".format(result.toDouble() / 1_000.0)} µs"
        result < 100_000 -> "${"%.2f".format(result.toDouble() / 1_000.0)} µs"
        result < 1_000_000 -> "${"%.1f".format(result.toDouble() / 1_000.0)} µs"
        result < 10_000_000 -> "${"%.3f".format(result.toDouble() / 1_000_000.0)} ms"
        result < 100_000_000 -> "${"%.2f".format(result.toDouble() / 1_000_000.0)} ms"
        result < 1_000_000_000 -> "${"%.1f".format(result.toDouble() / 1_000_000.0)} ms"
        else -> "${"%.3f".format(result.toDouble() / 1_000_000_000.0)} s"
    }
}


private class SynchronizedTaskStopwatch(started: Boolean = false) : TaskStopwatchBasic(started) {

    override fun start() = sync { super.start() }

    override fun stop() = sync { super.stop() }

    override fun formatted() = sync { super.formatted() }
}
