package ru.kekulta.explr.shared.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import ru.kekulta.explr.features.main.ui.MainActivity

const val FILE_PERMISSION_REQUEST_CODE = 111



fun Context.checkFilesPermissions(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Log.d(
            MainActivity.LOG_TAG, """
                Version: ${Build.VERSION.SDK_INT}, MANAGE_ALL_FILES
                isManager: ${Environment.isExternalStorageManager()}
            """.trimIndent()
        )
        Environment.isExternalStorageManager()
    } else {
        Log.d(
            MainActivity.LOG_TAG, """
                Version: ${Build.VERSION.SDK_INT}, LEGACY_FILES
            """.trimIndent()
        )
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


fun Activity.requestFilesPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
    } else {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            FILE_PERMISSION_REQUEST_CODE
        )
    }
}