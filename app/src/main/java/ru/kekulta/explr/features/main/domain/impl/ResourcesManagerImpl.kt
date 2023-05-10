package ru.kekulta.explr.features.main.domain.impl

import android.content.Context
import android.content.res.Resources
import ru.kekulta.explr.features.main.domain.api.ResourcesManager

class ResourcesManagerImpl(private val resources: Resources): ResourcesManager {
    override fun getString(id: Int): String = resources.getString(id)
}