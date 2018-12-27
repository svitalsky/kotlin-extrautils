package cz.mpts.libs.extrautils.kotlin.collections

import cz.mpts.libs.extrautils.kotlin.collections.TableFillingType.*
import java.lang.IllegalArgumentException

class ListToTableTransformer private constructor(builder: TransformerBuilder) {
    private val tableWidth: Int
    private val tableHeight: Int
    private val fillingType: TableFillingType
    private val listSize: Int
    private val startPadding: Int

    init {
        tableWidth = builder.tableWidth
        tableHeight = builder.tableHeight
        fillingType = builder.fillingType!!
        listSize = builder.listSize
        startPadding = builder.startPadding
    }

    fun transform() =
        (0 until tableHeight).map { rowIndex ->
            (0 until tableWidth).map { columnIndex -> Pair(rowIndex, columnIndex).listIndex }.toList()
        }.toList()

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

    companion object {
        class TransformerBuilder {
            internal var tableWidth = 0
            internal var tableHeight = 0
            internal var listSize = 0
            internal var fillingType: TableFillingType? = null
            internal var startPadding = 0

            fun tableWidth(value: Int) = apply { tableWidth = value }
            fun tableHeight(value: Int) = apply { tableHeight = value }
            fun listSize(value: Int) = apply { listSize = value }
            fun fillingType(value: TableFillingType) = apply { fillingType = value }
            fun startPadding(value: Int) = apply { startPadding = value }

            fun build() : ListToTableTransformer {
                validateInputs()
                countTableDimensions()
                return ListToTableTransformer(this)
            }

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
                if (fillingType == null) {
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
    }
}


enum class TableFillingType {
    TOP_LEFT_TO_RIGHT,
    TOP_LEFT_TO_BOTTOM,
    BOTTOM_LEFT_TO_RIGHT,
    BOTTOM_LEFT_TO_TOP;
}
