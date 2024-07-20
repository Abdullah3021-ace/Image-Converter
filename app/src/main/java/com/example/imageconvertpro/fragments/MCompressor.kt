package com.example.imageconvertpro.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.developers.imagezipper.ImageZipper
import com.example.imageconvertpro.R
import com.example.imageconvertpro.adapters.ImageAdapter
import com.example.imageconvertpro.databinding.FragmentMCompressorBinding
import com.example.imageconvertpro.vars.imageList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MCompressor : Fragment() {

    private lateinit var binding: FragmentMCompressorBinding
    private var compressRatio = 50 // Adjust as needed
    private val compressedImages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMCompressorBinding.inflate(inflater, container, false)

        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.recyclerview.adapter = ImageAdapter(imageList)



        val initialSize = getTotalSize(imageList)
        binding.imagesSize.text = "Images Size : ${formatSize(initialSize)}"
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_MCompressor_to_compressorType)
                }
            })


        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                compressRatio = progress
                binding.quality.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.again.setOnClickListener {
            binding.compress.visibility = View.VISIBLE
            binding.linearLayout2.visibility = View.GONE
        }

        binding.done.setOnClickListener {
            saveCompressedImages()
        }

        binding.compress.setOnClickListener {
            compressAllImages(imageList)
        }

        return binding.root
    }

    private fun compressAllImages(imageList: List<String>) {
        compressedImages.clear()

        for (imageUri in imageList) {
            val imagePath = getImagePathFromUri(imageUri.toUri())
            try {
                val compressedImageFile = compressImg(imagePath)
                compressedImages.add(compressedImageFile.absolutePath)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error compressing image: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val compressedSize = getTotalSize(compressedImages)
        binding.imagesSize.text = "Compressed Images Size : ${formatSize(compressedSize)}"
        binding.compress.visibility = View.GONE
        binding.linearLayout2.visibility = View.VISIBLE
    }

    private fun getImagePathFromUri(uri: Uri): String {
        // Example method to get file path from URI
        return uri.toFile().absolutePath
    }

    private fun compressImg(imagePath: String): File {
        try {
            val file = File(imagePath)


            val compressedImageFile = ImageZipper(requireContext())
                .setQuality(compressRatio)
                .setMaxWidth(200)
                .setMaxHeight(200)
                .compressToFile(file)

            return compressedImageFile
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error compressing image: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            throw e
        }
    }

    private fun saveCompressedImages() {
        try {
            val picturesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val compressedDir = File(picturesDir, "Compressed Images")
            if (!compressedDir.exists()) {
                compressedDir.mkdirs()
            }

            for (compressedImagePath in compressedImages) {
                val compressedImageFile = File(compressedImagePath)
                val compressedFileName = compressedImageFile.name
                val destFile = File(compressedDir, compressedFileName)

                compressedImageFile.copyTo(destFile, overwrite = true)
            }

            Toast.makeText(
                requireContext(),
                "Compressed images saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error saving compressed images: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun getTotalSize(imagePaths: List<String>): Long {
        var totalSize = 0L
        for (path in imagePaths) {
            val file = File(path)
            totalSize += if (file.exists()) file.length() else 0L
        }
        return totalSize
    }

    private fun formatSize(sizeInBytes: Long): String {
        val sizeInMb = sizeInBytes / (1024.0 * 1024.0)
        return String.format("%.2f Mb", sizeInMb)
    }

}
