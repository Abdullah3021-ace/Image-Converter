package com.example.imageconvertpro.dataclasses

import android.net.Uri

data class ImageFolder(
    val name: String,
    var thumbnailUri: Uri,
    var count: Int = 0
)
