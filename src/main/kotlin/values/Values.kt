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

/**
 * This class is not about null safety but about knowing whether some
 * value — even null value! — has been set (is available &c.) or not.
 */
class OptionalValue<out E> {

    companion object {
        private val empty = OptionalValue<Any?>()

        @Suppress("UNCHECKED_CAST")
        fun <E> empty() = empty as OptionalValue<E>

        fun <E> of(value: E) = OptionalValue(value)
    }

    private val _value: E?
    val valueSet: Boolean

    @Suppress("UNCHECKED_CAST")
    val value: E
        get() =
            if (valueSet) _value as E
            else throw NoSuchElementException("No value is available!")

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
        "OptionalValue(${if (valueSet) _value.toString() else "###-VALUE NOT SET-###"})"
}


/**
 * Repeatedly lazily initialized value.
 */
interface LazyVar<out E> {

    val value: E
    fun reset()
    fun getAndReset(): E

    companion object {

        fun <E> by(supplier: () -> E): LazyVar<E> = LazyVarBase(supplier)

        fun <E> synchronized(supplier: () -> E) : LazyVar<E> =
                object : LazyVarBase<E>(supplier) {
                    override val value: E
                        get() = synchronized(this) { super.value }

                    override fun reset() = synchronized(this) { super.reset() }

                    override fun getAndReset() = synchronized(this) { super.getAndReset() }
                }
    }
}


private open class LazyVarBase<out E>(private val supplier: () -> E) : LazyVar<E> {

    private var _value: E? = null
    private var initialized = false

    @Suppress("UNCHECKED_CAST")
    override val value: E
        get() {
            if (!initialized) {
                _value = supplier()
                initialized = true
            }
            return _value as E
        }

    override fun reset() {
        initialized = false
    }

    override fun getAndReset(): E {
        val result = value
        initialized = false
        return result
    }
}
