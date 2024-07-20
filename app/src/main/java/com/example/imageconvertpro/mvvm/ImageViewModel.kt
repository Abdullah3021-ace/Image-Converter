package com.example.imageconvertpro.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imageconvertpro.dataclasses.Image
import com.example.imageconvertpro.dataclasses.ImageFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ImageRepository(application)

    private val _imageFolders = MutableLiveData<List<ImageFolder>>()
    val imageFolders: LiveData<List<ImageFolder>> get() = _imageFolders

    private val _imagesInFolder = MutableLiveData<List<Image>>()
    val imagesInFolder: LiveData<List<Image>> get() = _imagesInFolder

    init {
        fetchImageFolders()
    }

    private fun fetchImageFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            val folders = repository.getImageFolders()
            _imageFolders.postValue(folders)
        }
    }

    fun fetchImagesByFolder(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val images = repository.getImagesByFolder(folderName)
            _imagesInFolder.postValue(images)
        }
    }
}
