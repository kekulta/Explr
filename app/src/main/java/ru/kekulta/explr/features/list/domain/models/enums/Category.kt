package ru.kekulta.explr.features.list.domain.models.enums

import ru.kekulta.explr.R

enum class Category(val textId: Int, val root: Int, val path: String) {
    VIDEOS(R.id.videos_item, R.string.videos_root, "VIDEOS::"),
    AUDIO(R.id.audio_item, R.string.audio_root, "AUDIO::"),
    DOCUMENTS(R.id.documents_item, R.string.documents_root, "DOCUMENTS::"),
    IMAGES(R.id.image_item, R.string.images_root, "IMAGES::"),
    STORAGE(R.id.internal_storage, R.string.internal_storage_root, "STORAGE::"),
    DOWNLOADS(R.id.downloads_item, R.string.downloads_root, "DOWNLOADS::"),
    RECENT(R.id.recent_item, R.string.recent_root, "RECENT::"),
    DIFFS(R.id.diffs_storage, R.string.diffs_root, "DIFFS::")
}