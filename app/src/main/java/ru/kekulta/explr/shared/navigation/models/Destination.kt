package ru.kekulta.explr.shared.navigation.models

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.kekulta.explr.shared.utils.contentEquals

data class Destination(val destinationKey: String, val args: Bundle? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Destination

        if (destinationKey != other.destinationKey) return false
        if (args == null || other.args == null) {
            if (args != other.args) return false
        } else {
            if (!args.contentEquals(other.args)) return false

        }

        return true
    }

    override fun hashCode(): Int {
        var result = destinationKey.hashCode()
        result = 31 * result + (args?.hashCode() ?: 0)
        return result
    }
}