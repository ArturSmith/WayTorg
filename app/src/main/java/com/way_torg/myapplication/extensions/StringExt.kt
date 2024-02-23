package com.way_torg.myapplication.extensions

import com.way_torg.myapplication.domain.entity.Category

fun String.toNewInt(): Int {
    return if (this.isEmpty()) {
        0
    } else {
        this.toInt()
    }
}

fun String.toNewDouble(): Double {
    return if (this.isEmpty()) {
        0.0
    } else {
        this.toDouble()
    }
}

suspend fun String.getOrCreateCategory(
    categories: List<Category>,
    predicate: suspend () -> Category?
) = categories.find { it.name == this } ?: predicate()

