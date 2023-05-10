package ru.kekulta.explr.features.difflist.domain.models.enums

import androidx.annotation.StringRes
import ru.kekulta.explr.R


enum class ChangeType(@StringRes val text: Int) {
    PROCESSING(R.string.processing_files_title),
    NEW(R.string.new_files_title),
    OLD(R.string.old_files_title),
    CHANGED(R.string.changed_files_title),
    DELETED(R.string.deleted_files_title),
}