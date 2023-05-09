package ru.kekulta.explr.features.list.domain.models.enums

import ru.kekulta.explr.R

enum class SortType(val id: Int) {
    NO_SORT(R.string.no_sort_title),
    NAME(R.string.sort_by_name_title),
    DATE_MODIFIED(R.string.sort_by_modified_title),
    SIZE(R.string.sort_by_size_title),
    TYPE((R.string.sort_by_type_title))
}