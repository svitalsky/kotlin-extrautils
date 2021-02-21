package cz.mpts.libs.extrautils.kotlin.stats

import cz.mpts.libs.extrautils.kotlin.values.isEqualTo
import java.math.*
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP


data class StatsResult(val count: Int,
                       val min: BigDecimal,
                       val max: BigDecimal,
                       val mean: BigDecimal,
                       val median: BigDecimal)


val Collection<BigDecimal>.stats : StatsResult
    get() = sorted().run {
        require(isNotEmpty()) { "Cannot compute stats for empty data collection!" }
        StatsResult(count = size,
                    min = first(),
                    max = last(),
                    mean = mean,
                    median = median)
    }

private val List<BigDecimal>.mean
    get() = fold(ZERO) { sum, item -> sum.add(item) }.divide(size.toBigDecimal(), HALF_UP)

private val List<BigDecimal>.median
    get() =
        if ((size % 2) != 0) get((size - 1) / 2)
        else {
            val v1 = get((size / 2) - 1)
            val v2 = get(size / 2)
            if (v1 isEqualTo v2) v1
            else v1.add(v2).divide(2.toBigDecimal(), HALF_UP)
        }
