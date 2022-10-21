package cz.mpts.libs.extrautils.kotlin.collections

import org.junit.Test
import kotlin.test.assertEquals

class CommonInterfacesTests {

    @Test
    fun testCountedSum() {
        assertEquals(21, items.sumOrZero)
    }

    @Test
    fun testCountedSumTotal() {
        assertEquals(42, listOf(items, null, items).sumTotal)
    }

    companion object {
        val items = Items(items = itemsList)
    }
}


data class Item(val id: Int, val name: String, override val count: Int) : Counted

data class Items(val items: List<Item?>) : Summed
{
    override val sum: Int
        get() = items.sum
}

val itemsList = listOf(
    Item(id = 1, name = "first", count = 7),
    null,
    Item(id = 2, name = "second", count = 11),
    Item(id = 3, name = "third", count = 3))
