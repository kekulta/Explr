package ru.kekulta.explr.features.list.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilesListState(
    val path: String = Category.STORAGE.path,
    val root: Int = Category.STORAGE.root,
    val location: Array<String> = arrayOf()
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilesListState

        if (path != other.path) return false
        if (root != other.root) return false
        if (!location.contentEquals(other.location)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + root
        result = 31 * result + location.contentHashCode()
        return result
    }
}