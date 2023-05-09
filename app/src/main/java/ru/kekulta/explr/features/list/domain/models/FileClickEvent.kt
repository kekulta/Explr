package ru.kekulta.explr.features.list.domain.models

sealed class FileClickEvent(val file: FileRepresentation) {
    class Click(file: FileRepresentation) : FileClickEvent(file)
    class Delete(file: FileRepresentation) : FileClickEvent(file)
    class Share(file: FileRepresentation) : FileClickEvent(file)
}