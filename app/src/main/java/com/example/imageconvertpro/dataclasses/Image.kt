package com.example.imageconvertpro.dataclasses


import android.net.Uri

data class Image(
    val name: String,
    val uri: Uri,
    var isSelected :Boolean= false
)
