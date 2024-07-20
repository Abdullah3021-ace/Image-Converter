package com.example.imageconvertpro.mvvm

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.imageconvertpro.dataclasses.Image
import com.example.imageconvertpro.dataclasses.ImageFolder
import java.io.File

class ImageRepository(private val context: Context) {
    fun getImageFolders(): List<ImageFolder> {
        val imageFolders = mutableListOf<ImageFolder>()
        val folderMap = HashMap<String, ImageFolder>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val folderName = it.getString(folderNameColumn)
                val data = it.getString(dataColumn)
                val fileUri = Uri.fromFile(File(data))

                if (!folderMap.containsKey(folderName)) {
                    val imageFolder = ImageFolder(folderName, fileUri, 1)
                    folderMap[folderName] = imageFolder
                    imageFolders.add(imageFolder)
                } else {
                    val existingFolder = folderMap[folderName]
                    existingFolder!!.count += 1
                    existingFolder!!.thumbnailUri = fileUri
                }
            }
        }

        return imageFolders
    }
    fun getImagesByFolder(folderName: String): List<Image> {
        val images = mutableListOf<Image>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE
        )
        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(folderName)
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)

        cursor?.use {
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val name = it.getString(nameColumn)
                val data = it.getString(dataColumn)
                val fileUri = Uri.fromFile(File(data))
                images.add(Image(name, fileUri))
            }
        }

        return images
    }
}