package ru.kekulta.explr.features.main.domain.models

import androidx.annotation.StringRes
import ru.kekulta.explr.R
import ru.kekulta.explr.features.list.domain.models.Category

data class ToolBarState(
    val root: Category = Category.STORAGE,
    val location: Array<String> = arrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToolBarState

        if (root != other.root) return false
        if (!location.contentEquals(other.location)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = root.hashCode()
        result = 31 * result + location.contentHashCode()
        return result
    }

}