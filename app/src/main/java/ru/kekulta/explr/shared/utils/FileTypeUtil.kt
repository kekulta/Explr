package ru.kekulta.explr.shared.utils

import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import java.io.File

enum class FileType {
    DIRECTORY, FILE, VIDEO, IMAGE, AUDIO, DOCUMENT,
}

val File.extension: String
    get() = name.substringAfterLast('.', "")

val FileRepresentation.extension: String
    get() = name.substringAfterLast('.', "")

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024

val FileRepresentation.sizeInKb get() = size / 1024
val FileRepresentation.sizeInMb get() = sizeInKb / 1024

val imageExtensions = setOf(
    "jpg",
    "jpeg",
    "jpe",
    "jfif",
    "jfi",
    "png",
    "gif",
    "webp",
    "tiff",
    "tif",
    "heif",
    "heic",
    "raw",
    "arw",
    "cr",
    "rw2",
    "nrw",
    "k25",
    "svg",
    "svgz",
    "eps",
    "ai"
)

val videosExtensions = setOf(
    "webm",
    "mkv",
    "flv",
    "flv",
    "vob",
    "vob",
    "ogg",
    "drc",
    "gif",
    "gifv",
    "webm",
    "gifv",
    "mng",
    "avi",
    "wmv",
    "yuv",
    "rmvb",
    "viv",
    "asf",
    "amv",
    "m4p",
    "m4v",
    "mpv",
    "m2v",
    "m4v",
    "svi",
    "3gp",
    "3g2",
    "mxf",
    "roq",
    "nsv",
    "flv",
    "f4p"
)

val documentsExtensions =
    setOf("doc", "docx", "html", "htm", "odt", "pdf", "xls", "xlsx", "ods", "ppt", "pptx", "txt")

val audioExtensions = setOf(
    "3gp",
    "aa",
    "aac",
    "aax",
    "act",
    "aiff",
    "alac",
    "amr",
    "ape",
    "au",
    "awb",
    "dss",
    "dvf",
    "flac",
    "gsm",
    "ivs",
    "m4a",
    "m4b",
    "m4p",
    "mmf",
    "mmf",
    "mp3",
    "mpc",
    "msv",
    "nmf",
    "ogg",
    "oga",
    "mogg",
    "opus",
    "ra",
    "rm",
    "ra",
    "raw",
    "rf64",
    "sln",
    "tta",
    "voc",
    "vox",
    "wav",
    "wma",
    "wv",
    "webm",
    "8svx",
    "cda"
)

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