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
package cz.mpts.libs.extrautils.kotlin.logging

import cz.mpts.libs.extrautils.kotlin.*
import java.math.*
import java.math.RoundingMode.HALF_UP
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.truncate

/**
 * Stopwatch useful for measuring tasks durations and such.
 *
 * Typical usage:
 *
 *     TaskStopwatch.createStarted then {
 *         doSomeWork()
 *     } then {
 *         doMoreWork()
 *     } eventually { stopwatch ->
 *         logger.info("All work done in ${stopwatch.formatted}.")
 *     }
 */
interface TaskStopwatch {

    companion object {
        fun create(): TaskStopwatch = TaskStopwatchBasic()
        fun createStarted(): TaskStopwatch = TaskStopwatchBasic(started = true)
        fun createSynchronized(): TaskStopwatch = SynchronizedTaskStopwatch()
        fun createSynchronizedStarted(): TaskStopwatch = SynchronizedTaskStopwatch(started = true)
    }

    /**
     * Tells whether this instance has been started yet.
     */
    val started: Boolean

    /**
     * Starts the stopwatch.
     *
     * @throws IllegalStateException in case of calling on already started stopwatch
     */
    fun start()

    /**
     * Returns the time this stopwatch has been running in nanoseconds.
     * May be called repeatedly.
     *
     * @return running time in nanoseconds
     * @throws IllegalStateException in case of calling on not yet started stopwatch.
     */
    fun time(): Long


    /**
     * Returns the time this stopwatch has been running in nanoseconds.
     * May be called repeatedly.
     *
     * @return running time in nanoseconds
     * @throws IllegalStateException in case of calling on not yet started stopwatch.
     * @deprecated
     */
    @Deprecated("This method has been deprecated, use time instead.",
                replaceWith = ReplaceWith("time()"))
    fun stop(): Long = time()

    /**
     * Returns the time this stopwatch has been running nicely formatted.
     *
     * @return running time nicely formatted
     * @throws IllegalStateException in case of calling on not yet started stopwatch.
     */
    val formatted: String


    /**
     * Returns the time this stopwatch has been running nicely formatted, including
     * minutes and hours.
     *
     * @return running time nicely formatted
     * @throws IllegalStateException in case of calling on not yet started stopwatch.
     */
    val formattedLong: String
}

/**
 * @see TaskStopwatch
 */
inline infix fun TaskStopwatch.then(f: () -> Any): TaskStopwatch = run {
    f()
    this
}

/**
 * @see TaskStopwatch
 */
inline infix fun <T> TaskStopwatch.eventually(crossinline f: (stopwatch: TaskStopwatch) -> T) = f(this)


private open class TaskStopwatchBasic(started: Boolean = false) : TaskStopwatch {

    private val _started = AtomicBoolean(started)

    final override val started
        get() = _started.get()

    private var startTime = System.nanoTime()

    override fun start() =
        if (_started.getAndSet(true)) throw IllegalStateException(STOPWATCH_ALREADY_STARTED)
        else startTime = System.nanoTime()

    override fun time() =
        if (_started.get()) System.nanoTime() - startTime
        else throw IllegalStateException(STOPWATCH_NOT_YET_STARTED)

    override val formatted
        get() = formatDuration()

    override val formattedLong
        get() = formatLongDuration()
}

internal fun TaskStopwatch.formatDuration() = time().let {
    when {
        it < 1_000 -> "$it ns"
        it < 10_000 -> "${"%.3f".format(it.toDouble() / 1_000.0)} µs"
        it < 100_000 -> "${"%.2f".format(it.toDouble() / 1_000.0)} µs"
        it < 1_000_000 -> "${"%.1f".format(it.toDouble() / 1_000.0)} µs"
        it < 10_000_000 -> "${"%.3f".format(it.toDouble() / 1_000_000.0)} ms"
        it < 100_000_000 -> "${"%.2f".format(it.toDouble() / 1_000_000.0)} ms"
        it < 1_000_000_000 -> "${"%.1f".format(it.toDouble() / 1_000_000.0)} ms"
        else -> "${"%.3f".format(it.toDouble() / 1_000_000_000.0)} s"
    }
}

internal fun TaskStopwatch.formatLongDuration() = time().let {
    if (it < 1_000_000_000) formatDuration()
    else {
        val msec = (it.toDouble() / 1_000_000_000.0).round(2)
        when {
            msec < 60.0 -> "$msec s"
            msec < 3_600.0 -> msec.minSec
            msec < 3_600.0 * 24 -> msec.hourMinSec
            else -> msec.dayHourMinSec
        }
    }
}

private val Double.minSec: String
    get() {
        val intPart = truncate(this).toInt()
        val mins = intPart / 60
        return "$mins m ${(this - (mins * 60)).round(2)} s"
    }

private val Double.hourMinSec: String
    get() {
        val intPart = truncate(this).toInt()
        val hours = intPart / 3_600
        val mins = (intPart - (hours * 3_600)) / 60
        return "$hours h $mins m ${(this - (hours * 3_600) - (mins * 60)).round(2)} s"
    }

private val Double.dayHourMinSec: String
    get() {
        val intPart = truncate(this).toInt()
        val days = intPart / (24 * 3_600)
        val hours = (intPart - (days * 24 * 3_600)) / 3_600
        val mins = (intPart - (days * 24 * 3_600) - (hours * 3_600)) / 60
        return "$days d $hours h $mins m" +
                " ${(this - (days * 24 * 3_600) - (hours * 3_600) - (mins * 60)).round(2)} s"
    }

private fun Double.round(scale: Int) = BigDecimal(this).setScale(scale, HALF_UP).toDouble()

private class SynchronizedTaskStopwatch(started: Boolean = false) : TaskStopwatchBasic(started) {
    override fun start()     = sync { super.start() }
    override fun time()      = sync { super.time() }
    override val formatted
        get() = sync { super.formatted }
    override val formattedLong
        get() = sync { super.formattedLong }
}
