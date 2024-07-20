package com.example.imageconvertpro.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.developers.imagezipper.ImageZipper
import com.example.imageconvertpro.databinding.FragmentImageCompressorBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageCompressor : Fragment() {
    private lateinit var binding: FragmentImageCompressorBinding
    private val PICK_IMAGE_REQUEST = 1
    private var compressRatio = 0
    private var selectedImageUri: Uri? = null
    private var compressedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageCompressorBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            openGallery()
        }

        binding.compress.setOnClickListener {
            if (selectedImageUri != null) {
                compressImg()
            } else {
                Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT)
                    .show()
            }
        }

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
            saveCompressedImage()
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            binding.imageView2.setImageURI(selectedImageUri)
            val imagePath = getRealPathFromURI(selectedImageUri!!)
            if (imagePath != null) {
                val originalFile = File(imagePath)
                val originalSize = originalFile.length()
                binding.originalSize.text = "Image Size " + formatFileSize(originalSize)
                binding.originalSize.visibility = View.VISIBLE
            }
        }
    }

    private fun compressImg() {
        val imagePath = getRealPathFromURI(selectedImageUri!!)
        if (imagePath != null) {
            val imageZipperFile = ImageZipper(requireContext())
                .setQuality(compressRatio)
                .setMaxWidth(200)
                .setMaxHeight(200)
                .compressToFile(File(imagePath))

            val compressedSize = imageZipperFile.length()
            binding.originalSize.text = "Image Size " + formatFileSize(compressedSize)

            val base64 = ImageZipper.getBase64forImage(imageZipperFile)
            compressedBitmap = ImageZipper.decodeBase64(base64)

            loadCompressedImage(compressedBitmap!!)
        } else {
            Toast.makeText(requireContext(), "Error getting image path", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCompressedImage(bitmap: Bitmap) {
        binding.imageView2.setImageBitmap(bitmap)
        binding.compress.visibility = View.GONE
        binding.linearLayout2.visibility = View.VISIBLE
    }

    private fun saveCompressedImage() {
        if (compressedBitmap == null) {
            Toast.makeText(requireContext(), "No compressed image to save", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val targetDir = File(picturesDir, "Compressed Images")
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val fileName = "compressed_image_${System.currentTimeMillis()}.jpg"
        val targetFile = File(targetDir, fileName)

        try {
            val outputStream = FileOutputStream(targetFile)
            compressedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val finalFileSize = targetFile.length()
            Toast.makeText(
                requireContext(),
                "Image saved to ${targetFile.absolutePath}. Size: ${formatFileSize(finalFileSize)}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatFileSize(size: Long): String {
        val kb = size / 1024
        val mb = kb / 1024
        return when {
            mb > 0 -> "$mb MB"
            kb > 0 -> "$kb KB"
            else -> "$size bytes"
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var result: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx != -1) {
                    val fileName = cursor.getString(idx)
                    val file = File(requireContext().filesDir, fileName)
                    try {
                        val inputStream = requireContext().contentResolver.openInputStream(uri)
                        val outputStream = FileOutputStream(file)
                        val buffer = ByteArray(4 * 1024)
                        var read: Int
                        while (inputStream!!.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                        result = file.absolutePath
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            cursor.close()
        }
        return result
    }
}
