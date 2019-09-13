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

import cz.mpts.libs.extrautils.kotlin.collections.TableFillingType.*

class ListToTableTransformer {
    private var tableWidth: Int = 0
    private var tableHeight: Int = 0
    private var _fillingType: TableFillingType? = null
    private var listSize: Int = 0
    private var startPadding: Int = 0
    private val fillingType: TableFillingType
        get() = _fillingType!!

    fun <E> mkTableSource(list: List<E>) =
        mutableListOf<List<E?>>().fillResult(list = list, empty = null)

    fun <E> mkTableSource(list: List<E>, empty: E) =
        mutableListOf<List<E>>().fillResult(list = list, empty = empty)

    fun <E, T> mkTableSource(list: List<E>, itemTransformer: (e: E) -> T) =
        mutableListOf<List<E?>>().fillResult(list = list,
                                             itemTransformer = itemTransformer,
                                             empty = null)

    fun <E, T> mkTableSource(list: List<E>, empty: T, itemTransformer: (e: E) -> T) =
        mutableListOf<List<E>>().fillResult(list = list,
                                            itemTransformer = itemTransformer,
                                            empty = empty)

    fun <E, T, R> mkTableSource(list: List<E>,
                                itemTransformer: (e: E) -> T,
                                rowTransformer: (l: List<T?>) -> R) =
        mutableListOf<R>().fillResult(list = list,
                                      itemTransformer = itemTransformer,
                                      rowTransformer = rowTransformer,
                                      empty = null)

    fun <E, T, R> mkTableSource(list: List<E>,
                                empty: T,
                                itemTransformer: (e: E) -> T,
                                rowTransformer: (l: List<T>) -> R) =
        mutableListOf<R>().fillResult(list = list,
                                      itemTransformer = itemTransformer,
                                      rowTransformer = rowTransformer,
                                      empty = empty)

    private fun <E, T, R> MutableList<R>.fillResult(
        list: List<E>,
        empty: T,
        itemTransformer: (e: E) -> T = ::identityTransformation,
        rowTransformer: (l: List<T>) -> R = ::identityTransformation) =
        apply {
            mkIndexPattern().forEach { rowIndexes ->
                mutableListOf<T>().apply {
                    rowIndexes.forEach { index ->
                        if (index in list.indices) add(itemTransformer(list[index]))
                        else add(empty)
                    }
                }.also { add(rowTransformer(it.toList())) }
            }
        }.toList()

    @Suppress("UNCHECKED_CAST")
    private fun <E, R> identityTransformation(e: E) = e as R

    fun mkIndexPattern() = run {
        validateInputs()
        countTableDimensions()
        (0 until tableHeight).map { rowIndex ->
            (0 until tableWidth).map { columnIndex -> Pair(rowIndex, columnIndex).listIndex }.toList()
        }.toList()
    }

    private val Pair<Int, Int>.listIndex
        get() = when (fillingType) {
            TOP_LEFT_TO_RIGHT -> ((first * tableWidth) + second).effectiveIndex
            TOP_LEFT_TO_BOTTOM -> (first + (second * tableHeight)).effectiveIndex
            BOTTOM_LEFT_TO_RIGHT -> (((tableHeight - first - 1) * tableWidth) + second).effectiveIndex
            BOTTOM_LEFT_TO_TOP -> ((tableHeight - first - 1) + (second * tableHeight)).effectiveIndex
        }

    private val Int.effectiveIndex
        get() = when {
            this < startPadding -> -1
            this < listSize + startPadding -> this - startPadding
            else -> -1
        }

    fun tableWidth(value: Int) = apply { tableWidth = value }
    fun tableHeight(value: Int) = apply { tableHeight = value }
    fun listSize(value: Int) = apply { listSize = value }
    fun fillingType(value: TableFillingType) = apply { _fillingType = value }
    fun startPadding(value: Int) = apply { startPadding = value }

    private fun validateInputs() {
        require(!(tableHeight == 0 && tableWidth == 0)) {
            "Either table width or table height (or both) must be given!"
        }
        require(tableWidth >= 0) { "Table width must be a positive number!" }
        require(tableHeight >= 0) { "Table height must be a positive number!" }
        require(listSize > 0) { "List size must be given and positive!" }
        requireNotNull(_fillingType) { "Filling type must be given!" }
        require(startPadding >= 0) { "Start padding must be zero or positive!" }
        require(!(tableHeight > 0 && tableWidth > 0 &&
                (listSize + startPadding) > (tableHeight * tableWidth))) {
            "Given table height and width are too small for given list size!"
        }
    }

    private fun countTableDimensions() {
        if (tableWidth == 0 || tableHeight == 0) {
            val given = if (tableHeight == 0) tableWidth else tableHeight
            var other = listSize / given
            if (listSize % given != 0) other++
            if (tableHeight == 0) tableHeight = other else tableWidth = other
        }
    }
}


enum class TableFillingType {
    TOP_LEFT_TO_RIGHT,
    TOP_LEFT_TO_BOTTOM,
    BOTTOM_LEFT_TO_RIGHT,
    BOTTOM_LEFT_TO_TOP;
}
