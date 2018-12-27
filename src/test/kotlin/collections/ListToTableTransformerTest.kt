package cz.mpts.libs.extrautils.kotlin.collections

import cz.mpts.libs.extrautils.kotlin.collections.TableFillingType.*
import org.junit.*
import org.junit.Assert.assertEquals

class ListToTableTransformerTest {

    private lateinit var transformer: ListToTableTransformer
    private var list = listOf("1", "2", "3")
    private var listLong = listOf("1", "2", "3", "4", "5", "6", "7")
    private var letters = listOf("a", "b", "c")

    @Before
    fun init() {
        transformer = ListToTableTransformer()
            .listSize(5)
            .tableHeight(2)
            .tableWidth(3)
            .fillingType(TOP_LEFT_TO_RIGHT)
    }

    @Test
    fun transformTopRight() {
        val expected = listOf(listOf(0, 1, 2), listOf(3, 4, -1))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun transformTopRightFull() {
        val transformer = transformer.listSize(6)
        val expected = listOf(listOf(0, 1, 2), listOf(3, 4, 5))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun transformTopBottom() {
        val transformer = transformer.fillingType(TOP_LEFT_TO_BOTTOM)
        val expected = listOf(listOf(0, 2, 4), listOf(1, 3, -1))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun transformBottomRight() {
        val transformer = transformer.fillingType(BOTTOM_LEFT_TO_RIGHT)
        val expected = listOf(listOf(3, 4, -1), listOf(0, 1, 2))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun transformBottomTop() {
        val transformer = transformer.fillingType(BOTTOM_LEFT_TO_TOP)
        val expected = listOf(listOf(1, 3, -1), listOf(0, 2, 4))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun transformTopRightPadding() {
        val transformer = transformer.startPadding(2).listSize(3)
        val expected = listOf(listOf(-1, -1, 0), listOf(1, 2, -1))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun transformBottomTopPadding() {
        val transformer = transformer.fillingType(BOTTOM_LEFT_TO_TOP).startPadding(2).listSize(4)
        val expected = listOf(listOf(-1, 1, 3), listOf(-1, 0, 2))
        assertEquals(expected, transformer.mkIndexPattern())
    }

    @Test
    fun mkTableSourceNullTest() {
        val transformer = transformer.listSize(4).startPadding(2)
        val expected = listOf(listOf(null, null, "1"), listOf("2", "3", null))
        assertEquals(expected, transformer.mkTableSource(list))
    }

    @Test
    fun mkTableSourceEmptyTest() {
        val transformer = transformer.listSize(4).startPadding(2)
        val expected = listOf(listOf("", "", "1"), listOf("2", "3", ""))
        assertEquals(expected, transformer.mkTableSource(list, ""))
    }

    @Test
    fun mkTableSourceEmptyLonglistTest() {
        val transformer = transformer.listSize(4).startPadding(2)
        val expected = listOf(listOf("empty", "empty", "1"), listOf("2", "3", "4"))
        assertEquals(expected, transformer.mkTableSource(listLong, "empty"))
    }

    @Test
    fun mkTableSourceEmptyLongShortTest() {
        val transformer = transformer.listSize(3).startPadding(2)
        val expected = listOf(listOf("empty", "empty", "1"), listOf("2", "3", "empty"))
        assertEquals(expected, transformer.mkTableSource(listLong, "empty"))
    }

    @Test
    fun mkTableSourceItemTransformerNull() {
        val transformer = transformer.listSize(3).startPadding(2)
        val expected = listOf(listOf(null, null, 1), listOf(2, 3, null))
        assertEquals(expected,
                     transformer.mkTableSource(list = list,
                                               itemTransformer = { it.toInt() }))
    }

    @Test
    fun mkTableSourceItemTransformerEmpty() {
        val transformer = transformer.listSize(3).startPadding(2)
        val expected = listOf(listOf(0, 0, 1), listOf(2, 3, 0))
        assertEquals(expected,
                     transformer.mkTableSource(list = list,
                                               empty = 0,
                                               itemTransformer = { it.toInt() }))
    }

    @Test
    fun mkTableSourceRowTransformerNull() {
        val transformer = transformer.listSize(3).startPadding(2)
        val expected = listOf("  1", "23 ")
        assertEquals(expected,
                     transformer.mkTableSource(list = list,
                                               itemTransformer = { it }) { list ->
                         list.joinToString(separator = "") { it ?: " " }
                     })
    }

    @Test
    fun mkTableSourceRowTransformerEmpty() {
        val transformer = transformer.listSize(3).startPadding(2)
        val expected = listOf("-, -, A", "B, C, -")
        assertEquals(expected,
                     transformer.mkTableSource(list = letters,
                                               empty = "-",
                                               itemTransformer = { it.toUpperCase() }) { list ->
                         list.joinToString()
                     })
    }
}
