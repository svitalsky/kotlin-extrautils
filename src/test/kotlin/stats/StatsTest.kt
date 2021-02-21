package stats

import cz.mpts.libs.extrautils.kotlin.stats.*
import org.junit.Test

import org.junit.Assert.*
import java.math.*
import java.math.RoundingMode.HALF_UP

class StatsTest {

    @Test
    fun stats() {
        assertEquals(resEqualsOdd, listOf(3, 3, 3).bd.stats)
    }

    @Test
    fun stats2() {
        assertEquals(resEqualsEven, listOf(3, 3, 3, 3).bd.stats)
    }

    @Test
    fun stats3() {
        assertEquals(resSqOdd, listOf(1, 2, 3).bdd.stats)
    }

    @Test
    fun stats4() {
        assertEquals(resSqEven, listOf(1, 2, 3, 4).bdd.stats)
    }

    @Test
    fun stats5() {
        assertEquals(resRnd, listOf(1, 1, 2, 2, 2, 10).bdd.stats)
    }
}

private val resEqualsOdd = StatsResult(count = 3, min = 3.bd, max = 3.bd, mean = 3.bd, median = 3.bd)

private val resEqualsEven = StatsResult(count = 4, min = 3.bd, max = 3.bd, mean = 3.bd, median = 3.bd)

private val resSqOdd = StatsResult(count = 3, min = 1.bdd, max = 3.bdd, mean = 2.bdd, median = 2.bdd)

private val resSqEven = StatsResult(count = 4, min = 1.bdd, max = 4.bdd, mean = 2.5.bdd, median = 2.5.bdd)

private val resRnd = StatsResult(count = 6, min = 1.bdd, max = 10.bdd, mean = 3.bdd, median = 2.bdd)

private val Double.bd
    get() = toBigDecimal(MathContext(2, HALF_UP))

private val Double.bdd
    get() = bd.divide(1.bd, 2, HALF_UP)

private val Int.bd
    get() = toBigDecimal(MathContext(2, HALF_UP))

private val Int.bdd
    get() = bd.divide(1.bd, 2, HALF_UP)

private val List<Int>.bd
    get() = map { it.bd }

private val List<Int>.bdd
    get() = map { it.bdd }
