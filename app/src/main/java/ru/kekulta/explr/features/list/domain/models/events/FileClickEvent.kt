package ru.kekulta.explr.features.list.domain.models.events

import ru.kekulta.explr.features.list.domain.models.FileRepresentation

sealed class FileClickEvent(val file: FileRepresentation) {
    class Click(file: FileRepresentation) : FileClickEvent(file)
    class Delete(file: FileRepresentation) : FileClickEvent(file)
    class Share(file: FileRepresentation) : FileClickEvent(file)
    class Details(file: FileRepresentation) : FileClickEvent(file)
}