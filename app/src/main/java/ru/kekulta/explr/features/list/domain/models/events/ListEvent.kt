package ru.kekulta.explr.features.list.domain.models.events

import ru.kekulta.explr.features.list.domain.models.FileRepresentation
import java.io.File

sealed class ListEvent {
    class OpenFile(val file: File) : ListEvent()
    class ShareFile(val file: File) : ListEvent()
    class DetailsFile(val file: FileRepresentation) : ListEvent()
}