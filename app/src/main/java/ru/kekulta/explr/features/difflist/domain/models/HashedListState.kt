package ru.kekulta.explr.features.difflist.domain.models

data class HashedListState(
    val newFiles: List<HashedFile> = listOf(),
    val oldFiles: List<HashedFile> = listOf(),
    val changedFiles: List<HashedFile> = listOf(),
    val deletedFiles: List<HashedFile> = listOf(),
)