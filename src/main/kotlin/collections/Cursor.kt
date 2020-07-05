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
package cz.mpts.libs.extrautils.kotlin.collections

import cz.mpts.libs.extrautils.kotlin.values.*

/**
 * Iterator that allows to test — even repeatedly — what next element will
 * be like before actually retrieving it.
 */
interface Cursor<out E> : Iterator<E> {

    /**
     * Returns the value that the next `next()` call will return, without moving
     * iteration forward. Called repeatedly will return always the same value unless
     * `next()` is called in the meantime.
     *
     * @throws NoSuchElementException in case the underlying iterator has no next value
     */
    fun peek() : E
}


private open class CursorBasic<out E>(protected val iterator: Iterator<E>) : Cursor<E> {

    private val current: LazyVar<OptionalValue<E>> = LazyVar by {
        if (iterator.hasNext()) OptionalValue.of(iterator.next())
        else OptionalValue.none()
    }

    override fun hasNext() = current.value.valueSet

    override fun next() = current.getAndReset().value

    override fun peek() = current.value.value
}


/**
 * Returns cursor for this Iterable.
 */
fun <E> Iterable<E>.cursor() : Cursor<E> = CursorBasic(iterator = iterator())


/**
 * Returns synchronized cursor for this Iterable. It should be thread safe
 * as long as the underlying iterator is.
 */
fun <E> Iterable<E>.synchronizedCursor() : Cursor<E> =
    object: CursorBasic<E>(iterator = iterator()) {
        override fun hasNext() = synchronized(iterator) { super.hasNext() }
        override fun next() = synchronized(iterator) { super.next() }
        override fun peek() = synchronized(iterator) { super.peek() }
    }


/**
 * Returns cursor for any object having an iterator, be it native implementation,
 * an extension function or a common outer function
 */
@Suppress("unused")
fun <E> E.cursor(iterator: Iterator<E>) : Cursor<E> = CursorBasic(iterator = iterator)
