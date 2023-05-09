package ru.kekulta.explr.features.list.domain.impl

import android.content.Context
import androidx.core.content.FileProvider
import ru.kekulta.explr.features.list.domain.api.FileUtil
import ru.kekulta.explr.shared.utils.FileType
import ru.kekulta.explr.shared.utils.type
import java.io.File


// Application context here, so no memory leaks
class FileUtilImpl(private val context: Context) : FileUtil {
    override fun getType(file: File): FileType =
        if (file.isDirectory) {
            FileType.DIRECTORY
        } else {
            val uri = FileProvider.getUriForFile(context, "ru.kekulta.fileprovider", file)
            val mime: String? = context.contentResolver.getType(uri)

            when {
                mime?.startsWith("image") ?: false -> FileType.IMAGE
                mime?.startsWith("audio") ?: false -> FileType.AUDIO
                mime?.startsWith("video") ?: false -> FileType.VIDEO
                else -> file.type
            }
        }

    companion object {
        const val LOG_TAG = "TypeCheckerImpl"
    }
}