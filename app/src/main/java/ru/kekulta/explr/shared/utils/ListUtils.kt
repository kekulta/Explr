package ru.kekulta.explr.shared.utils

fun <T> isEqual(first: List<T>, second: List<T>): Boolean {

    if (first.size != second.size) {
        return false
    }

    first.forEachIndexed { index, value ->
        if (second[index] != value) {
            return false
        }
    }
    return true
}

fun <T> List<T>.contentEquals(list: List<T>) = isEqual(this, list)