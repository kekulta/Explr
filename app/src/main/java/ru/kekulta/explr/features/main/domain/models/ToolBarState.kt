package ru.kekulta.explr.features.main.domain.models

import androidx.annotation.StringRes
import ru.kekulta.explr.R

data class ToolBarState(
    @StringRes val root: Int = R.string.internal_storage,
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
        var result = root
        result = 31 * result + location.contentHashCode()
        return result
    }
}