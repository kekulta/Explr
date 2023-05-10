package ru.kekulta.explr.shared.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import ru.kekulta.explr.features.list.domain.models.states.FilesListState
import ru.kekulta.explr.features.list.ui.FilesListFragment
import java.io.Serializable

fun <T : Parcelable> Bundle.getParcelableSafe(key: String, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        getParcelable(key)
    }

fun <T : Enum<T>> Bundle.putEnum(key: String, enum: T) {
    putSerializable(key, enum)
}

@Suppress("UNCHECKED_CAST")
fun <T : Serializable> Bundle.getSerializableSafe(key: String, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        try {
            getSerializable(key)?.let { it as T }
        } catch (e: Exception) {
            null
        }
    }