package cz.mpts.libs.extrautils.kotlin.collections

import cz.mpts.libs.extrautils.kotlin.collections.ListToTableTransformer.Companion.TransformerBuilder
import cz.mpts.libs.extrautils.kotlin.collections.TableFillingType.*
import org.junit.*

import org.junit.Assert.*

class ListToTableTransformerTest {

    private lateinit var transformerBuilder: TransformerBuilder

    @Before
    fun init() {
        transformerBuilder = TransformerBuilder().listSize(5).tableHeight(2).tableWidth(3)
    }

    @Test
    fun transformTopRight() {
        val transformer = transformerBuilder.fillingType(TOP_LEFT_TO_RIGHT).build()
        val expected = listOf(listOf(0, 1, 2), listOf(3, 4, -1))
        assertEquals(expected, transformer.transform())
    }

    @Test
    fun transformTopRightFull() {
        val transformer = transformerBuilder.fillingType(TOP_LEFT_TO_RIGHT).listSize(6).build()
        val expected = listOf(listOf(0, 1, 2), listOf(3, 4, 5))
        assertEquals(expected, transformer.transform())
    }

    @Test
    fun transformTopBottom() {
        val transformer = transformerBuilder.fillingType(TOP_LEFT_TO_BOTTOM).build()
        val expected = listOf(listOf(0, 2, 4), listOf(1, 3, -1))
        assertEquals(expected, transformer.transform())
    }

    @Test
    fun transformBottomRight() {
        val transformer = transformerBuilder.fillingType(BOTTOM_LEFT_TO_RIGHT).build()
        val expected = listOf(listOf(3, 4, -1), listOf(0, 1, 2))
        assertEquals(expected, transformer.transform())
    }

    @Test
    fun transformBottomTop() {
        val transformer = transformerBuilder.fillingType(BOTTOM_LEFT_TO_TOP).build()
        val expected = listOf(listOf(1, 3, -1), listOf(0, 2, 4))
        assertEquals(expected, transformer.transform())
    }

    @Test
    fun transformTopRightPadding() {
        val transformer = transformerBuilder.fillingType(TOP_LEFT_TO_RIGHT).startPadding(2).listSize(3).build()
        val expected = listOf(listOf(-1, -1, 0), listOf(1, 2, -1))
        assertEquals(expected, transformer.transform())
    }

    @Test
    fun transformBottomTopPadding() {
        val transformer = transformerBuilder.fillingType(BOTTOM_LEFT_TO_TOP).startPadding(2).listSize(4).build()
        val expected = listOf(listOf(-1, 1, 3), listOf(-1, 0, 2))
        assertEquals(expected, transformer.transform())
    }
}
