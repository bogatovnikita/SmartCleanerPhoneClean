package com.smart.cleaner.phoneclean.domain.extentions

fun <T> List<T>.findDuplicates(compare: (T, T) -> Boolean = {a, b -> a == b}) : List<List<T>>{
    val source = this.toMutableList()

    if (source.size <= 1) return emptyList()

    val result = mutableListOf<MutableList<T>>()

    while (source.isNotEmpty()){
        val first = source.removeFirst()
        val comparator: (T) -> Boolean = { compare(it, first) }

        var shouldCheckSource = true

        result.forEach { group ->
            if (group.containsBy(comparator)){
                group.add(first)
                shouldCheckSource = false
                return@forEach
            }
        }

        if (shouldCheckSource && source.containsBy(comparator)){
            result.add(mutableListOf(first))
        }


    }

    return result
}

private fun <T> List<T>.containsBy(compare: (T) -> Boolean) : Boolean{
    return find(compare) != null
}