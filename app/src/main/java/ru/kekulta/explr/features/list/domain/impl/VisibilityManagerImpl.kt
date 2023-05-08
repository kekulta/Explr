package ru.kekulta.explr.features.list.domain.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kekulta.explr.features.list.domain.api.VisibilityManager

class VisibilityManagerImpl(showHidden: Boolean = false, showNomedia: Boolean = false) :
    VisibilityManager {

    override var showHidden: Boolean = showHidden
        set(value) {
            field = value
            showHiddenFlow.value = value
            Log.d(LOG_TAG, "hidden: ${showHiddenFlow.value}, $value")
        }
    override var showNomedia: Boolean = showNomedia
        set(value) {
            field = value
            showNomediaFlow.value = value
            Log.d(LOG_TAG, "nomedia: ${showNomediaFlow.value}, $value")
        }

    private val showHiddenFlow = MutableStateFlow(showHidden)
    private val showNomediaFlow = MutableStateFlow(showNomedia)


    override fun getHiddenStatus(): Flow<Boolean> = showHiddenFlow

    override fun getNomediaStatus(): Flow<Boolean> = showNomediaFlow

    companion object {
        const val LOG_TAG = "VisibilityManagerImpl"
    }
}