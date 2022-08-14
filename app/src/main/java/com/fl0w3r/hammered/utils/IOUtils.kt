package com.fl0w3r.hammered.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.File

object IOUtils {

    /**
     * Writes the [uri] to the [outputFile] using [contentResolver].
     * */
    fun writeToFilesDir(uri: Uri, outputFile: File, contentResolver: ContentResolver) {
        contentResolver.openInputStream(uri)?.use { iStream ->
            val imageArr = iStream.readBytes()

            outputFile.outputStream().use { oStream ->
                oStream.write(imageArr)
            }
        }
    }
}