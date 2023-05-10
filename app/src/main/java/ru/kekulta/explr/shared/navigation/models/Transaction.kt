package ru.kekulta.explr.shared.navigation.models

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

data class Transaction(
    val id: Int,
    val destination: Destination,
    val cache: WeakReference<Fragment>
)