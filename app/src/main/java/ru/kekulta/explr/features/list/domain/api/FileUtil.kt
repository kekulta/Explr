package ru.kekulta.explr.features.list.domain.api

import ru.kekulta.explr.shared.utils.FileType
import java.io.File

interface FileUtil {
    fun getType(file: File): FileType
}