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
        mutableListOf<List<E?>>().fillResult(list = list) { null }

    fun <E> mkTableSource(list: List<E>, empty: E) =
        mutableListOf<List<E>>().fillResult(list = list) { empty }

    fun <E, T> mkTableSource(list: List<E>, itemTransformer: (e: E) -> T) =
        mutableListOf<List<E?>>().fillResult(list = list,
                                            itemTransformer = itemTransformer) { null }

    fun <E, T> mkTableSource(list: List<E>, empty: T, itemTransformer: (e: E) -> T) =
        mutableListOf<List<E>>().fillResult(list = list,
                                            itemTransformer = itemTransformer) { empty }

    fun <E, T, R> mkTableSource(list: List<E>,
                                itemTransformer: (e: E) -> T,
                                rowTransformer: (l: List<T?>) -> R) =
        mutableListOf<R>().fillResult(list = list,
                                      itemTransformer = itemTransformer,
                                      rowTransformer = rowTransformer) { null }

    fun <E, T, R> mkTableSource(list: List<E>,
                                empty: T,
                                itemTransformer: (e: E) -> T,
                                rowTransformer: (l: List<T>) -> R) =
        mutableListOf<R>().fillResult(list = list,
                                      itemTransformer = itemTransformer,
                                      rowTransformer = rowTransformer) { empty }

    private fun <E, T, R> MutableList<R>.fillResult(
        list: List<E>,
        itemTransformer: (e: E) -> T = ::identityTransformation,
        rowTransformer: (l: List<T>) -> R = ::identityTransformation,
        emptyProducer: () -> T) =
        apply {
            mkIndexPattern().forEach { rowIndexes ->
                mutableListOf<T>().apply {
                    rowIndexes.forEach { index ->
                        if (index in 0 until list.size) add(itemTransformer(list[index]))
                        else add(emptyProducer())
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
        if (tableHeight == 0 && tableWidth == 0) {
            throw IllegalArgumentException("Either table width or table height (or both) must be given!")
        }
        if (tableWidth < 0) {
            throw IllegalArgumentException("Table width must be a positive number!")
        }
        if (tableHeight < 0) {
            throw IllegalArgumentException("Table height must be a positive number!")
        }
        if (listSize <= 0) {
            throw IllegalArgumentException("List size must be given and positive!")
        }
        if (_fillingType == null) {
            throw IllegalArgumentException("Filling type must be given!")
        }
        if (startPadding < 0) {
            throw IllegalArgumentException("Start padding must be zero or positive!")
        }
        if (tableHeight > 0 && tableWidth > 0 &&
            (listSize + startPadding) > (tableHeight * tableWidth))
        {
            throw IllegalArgumentException("Given table height and width are too small for given list size!")
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
