package ru.kekulta.explr.features.list.domain.models.enums

import androidx.annotation.StringRes
import ru.kekulta.explr.R

enum class FileType(@StringRes val text: Int) {
    DIRECTORY(R.string.directory_type),
    FILE(R.string.file_type),
    VIDEO(R.string.video_type),
    IMAGE(R.string.image_type),
    AUDIO(R.string.audio_type),
    DOCUMENT(R.string.document_type),
}