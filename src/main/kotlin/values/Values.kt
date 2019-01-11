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
package cz.mpts.libs.extrautils.kotlin.values

import cz.mpts.libs.extrautils.kotlin.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This class is not about null safety but about knowing whether some
 * value — even null value! — has been set (is available &c.) or not.
 */
class OptionalValue<out E> {

    companion object {
        private val empty = OptionalValue<Any?>()

        /**
         * Returns an empty instance (the only existing empty instance, under normal
         * circumstances) of OptionalValue cast to proper type.
         */
        @Suppress("UNCHECKED_CAST")
        fun <E> empty() = empty as OptionalValue<E>

        /**
         * Returns OptionalValue with its `value` set to the provided value.
         */
        fun <E> of(value: E) = OptionalValue(value)
    }

    private val _value: E?

    /**
     * `False` for an empty OptionalValue, otherwise `true`.
     * @see isEmpty
     */
    val valueSet: Boolean

    /**
     * Opposite of `valueSet`.
     * @see valueSet
     */
    val isEmpty
        get() = ! valueSet

    /**
     * For an empty OptionalValue throws `NoSuchElementException`, otherwise contains the value
     * of this OptionalValue.
     *
     * @throws NoSuchElementException when empty
     */
    @Suppress("UNCHECKED_CAST")
    val value: E
        get() =
            if (valueSet) _value as E
            else throw NoSuchElementException(NO_VALUE_AVAILABLE)

    private constructor() {
        _value = null
        valueSet = false
    }

    private constructor(e: E) {
        _value = e
        valueSet = true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OptionalValue<*>
        return if (valueSet) other.valueSet && (_value == other._value) else !other.valueSet
    }

    override fun hashCode() = valueSet.hashCode() + if (valueSet) 31 * (_value?.hashCode() ?: 0) else 0

    override fun toString() =
        "OptionalValue${if (valueSet) "(" + _value.toString() + ")" else "<<<#-VALUE NOT SET-#>>>"}"
}


/**
 * Repeatedly lazily initialized value.
 * Usually (always for current implementations within this module) needs some
 * initializer (supplier) that provides real values on demand.
 *
 * For usage example see [cz.mpts.libs.extrautils.kotlin.collections.Cursor] source code.
 */
interface LazyVar<out E> {

    /**
     * The current value. It is lazily initialized on the first access.
     */
    val value: E

    /**
     * Switches this to an uninitialized state, so that the next access
     * to the `value` initializes it again.
     */
    fun reset()

    /**
     * Resets this and returns the `value` it had before resetting.
     * @see reset
     */
    fun getAndReset(): E

    companion object {

        /**
         * Creates a LazyVar that can be repeatedly initialized by the supplier.
         */
        fun <E> by(supplier: () -> E): LazyVar<E> = LazyVarBase(supplier)

        /**
         * Creates a synchronized version of a LazyVar.
         * @see by
         */
        fun <E> synchronized(supplier: () -> E) : LazyVar<E> =
                object : LazyVarBase<E>(supplier) {
                    override val value: E
                        get() = sync { super.value }

                    override fun reset() = sync { super.reset() }

                    override fun getAndReset() = sync { super.getAndReset() }
                }
    }
}


private open class LazyVarBase<out E>(private val supplier: () -> E) : LazyVar<E> {

    private var _value: E? = null
    private val initialized = AtomicBoolean(false)

    @Suppress("UNCHECKED_CAST")
    override val value: E
        get() {
            if (!initialized.getAndSet(true)) _value = supplier()
            return _value as E
        }

    override fun reset() = initialized.set(false)

    override fun getAndReset() = value.also {
        initialized.set(false)
    }
}
