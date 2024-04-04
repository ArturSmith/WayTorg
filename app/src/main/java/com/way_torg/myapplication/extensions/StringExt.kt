package com.way_torg.myapplication.extensions

import com.way_torg.myapplication.domain.entity.Category

fun String.zeroIntIfEmpty(): Int {
    return if (this.isEmpty()) 0 else { this.toInt() }
}

fun String.zeroDoubleIfEmpty(): Double {
    return if (this.isEmpty()) {
        0.0
    } else {
        this.toDouble()
    }
}

inline fun String.getOrCreateCategory(
    categories: List<Category>,
    predicate: () -> Category?
) = categories.find { it.name == this } ?: predicate()

