package ru.kekulta.explr.shared.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import ru.kekulta.explr.features.list.domain.models.enums.FileType
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


val File.extension: String
    get() = name.substringAfterLast('.', "")

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024

val File.type: FileType
    get() = if (isDirectory) {
        FileType.DIRECTORY
    } else {
        when (extension.lowercase()) {
            in imageExtensions -> FileType.IMAGE
            in audioExtensions -> FileType.AUDIO
            in videosExtensions -> FileType.VIDEO
            in documentsExtensions -> FileType.DOCUMENT
            else -> FileType.FILE
        }
    }

fun File.shareFile(context: Context) {

    val uri = FileProvider.getUriForFile(context, "ru.kekulta.fileprovider", this)
    val mime: String? = when (type) {
        FileType.IMAGE -> "image/*"
        FileType.VIDEO -> "video/*"
        FileType.AUDIO -> "audio/*"
        else -> context.contentResolver.getType(uri)
    }

    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.setDataAndType(uri, mime)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}

fun File.openFile(context: Context) {

    val uri = FileProvider.getUriForFile(context, "ru.kekulta.fileprovider", this)
    val mime: String? = when (type) {
        FileType.IMAGE -> "image/*"
        FileType.VIDEO -> "video/*"
        FileType.AUDIO -> "audio/*"
        else -> context.contentResolver.getType(uri)
    }

    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setDataAndType(uri, mime)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(intent)
}



val FileRepresentation.extension: String
    get() = name.substringAfterLast('.', "")

val FileRepresentation.sizeInKb get() = size / 1024
val FileRepresentation.sizeInMb get() = sizeInKb / 1024

val FileRepresentation.file: File
    get() = File(path)

fun FileRepresentation.mime(context: Context): String? {
    val uri = FileProvider.getUriForFile(context, "ru.kekulta.fileprovider", this.file)
    return context.contentResolver.getType(uri)
}

fun FileRepresentation.shareFile(context: Context) = this.file.shareFile(context)

fun FileRepresentation.openFile(context: Context) = file.openFile(context)

fun FileRepresentation.requireParent(): String =
    parent ?: Environment.getExternalStorageDirectory().path

fun FileRepresentation.deleteRecursively() = file.deleteRecursively()