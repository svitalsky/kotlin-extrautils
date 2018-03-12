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
package cz.mpts.libs.extrautils.kotlin.collections

import cz.mpts.libs.extrautils.kotlin.values.*

/**
 * Iterator that allows to test — even repeatedly — what next element will
 * be like before actually retrieving it.
 */
interface Cursor<out E> : Iterator<E> {
    fun peek() : E
}


private open class CursorBasic<out E>(protected val iterator: Iterator<E>) : Cursor<E> {

    private var current: LazyVar<OptionalValue<E>> = LazyVar.by {
        if (iterator.hasNext()) OptionalValue.of(iterator.next())
        else OptionalValue.empty()
    }

    override fun hasNext() = current.value.valueSet

    override fun next() = current.getAndReset().value

    override fun peek() = current.value.value
}


/**
 * Returns cursor for this iterable.
 */
fun <E> Iterable<E>.cursor() : Cursor<E> = CursorBasic(iterator())


/**
 * Returns synchronized cursor for this iterable. It should be thread safe
 * as long as the underlying iterator is.
 */
fun <E> Iterable<E>.synchronizedCursor() : Cursor<E> =
    object: CursorBasic<E>(iterator()) {
        override fun hasNext() = synchronized(iterator) { super.hasNext() }
        override fun next() = synchronized(iterator) { super.next() }
        override fun peek() = synchronized(iterator) { super.peek() }
    }
