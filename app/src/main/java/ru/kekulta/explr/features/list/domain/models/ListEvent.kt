package ru.kekulta.explr.features.list.domain.models

import java.io.File

sealed class ListEvent {
    class OpenFile(val file: File): ListEvent()
    class ShareFile(val file: File): ListEvent()
}