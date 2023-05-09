package ru.kekulta.explr.features.list.domain.api.usecases

import ru.kekulta.explr.features.list.domain.models.enums.FileType
import java.io.File

interface FileUtil {
    fun getType(file: File): FileType
}