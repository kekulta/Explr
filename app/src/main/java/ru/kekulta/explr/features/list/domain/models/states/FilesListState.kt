package ru.kekulta.explr.features.list.domain.models.states

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.kekulta.explr.features.list.domain.models.enums.Category
import ru.kekulta.explr.features.main.domain.models.LocationItem

@Parcelize
data class FilesListState(
    val path: String = Category.STORAGE.path,
    val root: Category = Category.STORAGE,
    val location: Array<LocationItem> = arrayOf()
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
        result = 31 * result + root.hashCode()
        result = 31 * result + location.contentHashCode()
        return result
    }

}