package cz.mpts.libs.extrautils.kotlin.collections

interface Counted {
    val count: Int
}

val Counted?.countOrZero: Int
    get() = this?.count ?: 0

val Iterable<Counted?>.sum: Int
    get() = sumOf { it.countOrZero }


interface Summed {
    val sum: Int
}

val Summed?.sumOrZero: Int
    get() = this?.sum ?: 0

val Iterable<Summed?>.sumTotal: Int
    get() = sumOf { it.sumOrZero }
