package ru.kekulta.explr.features.main.domain.api

import androidx.annotation.StringRes

interface ResourcesManager {
    fun getString(@StringRes id: Int): String
}