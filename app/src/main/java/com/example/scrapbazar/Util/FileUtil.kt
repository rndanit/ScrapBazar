package com.example.scrapbazar.Util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File

object FileUtil {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver: ContentResolver = context.contentResolver
        val file = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // For versions before Android 10
            File(getRealPathFromUri(context, uri))
        } else {
            // For Android 10 and later
            createFileFromUri(context, uri, contentResolver)
        }

        return file
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String {
        var realPath = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            realPath = it.getString(columnIndex)
        }

        return realPath
    }

    private fun createFileFromUri(
        context: Context,
        uri: Uri,
        contentResolver: ContentResolver
    ): File? {
        val fileExtension = getFileExtension(context, uri,contentResolver)
        val fileName = "${System.currentTimeMillis()}.$fileExtension"

        val destinationFile = File(context.cacheDir, fileName)

        contentResolver.openInputStream(uri)?.use { inputStream ->
            destinationFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return destinationFile
    }

    private fun getFileExtension(context: Context, uri: Uri,
                                 contentResolver: ContentResolver): String {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return if (uri.scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        } ?: "jpg" //Default to jpg if extension not found
    }
}

