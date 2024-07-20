package com.example.imageconvertpro.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.imageconvertpro.databinding.FragmentImageConverterBinding

class ImageConverter : Fragment() {

    private lateinit var binding: FragmentImageConverterBinding
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageConverterBinding.inflate(layoutInflater, container, false)

        binding.button.setOnClickListener {
            openGallery()
        }

        binding.ch1.setOnClickListener {
            setQualityUnselected()
            setQualitySelected(binding.ch1, binding.lowTexxt)
        }
        binding.ch2.setOnClickListener {
            setQualityUnselected()
            setQualitySelected(binding.ch2, binding.medText)
        }
        binding.ch3.setOnClickListener {
            setQualityUnselected()
            setQualitySelected(binding.ch3, binding.highText)
        }

        binding.png.setOnClickListener {
            setFileTypeUnselected()
            setQualitySelected(binding.png, binding.textPNG)
        }
        binding.jpg.setOnClickListener {
            setFileTypeUnselected()
            setQualitySelected(binding.jpg, binding.textJPG)
        }
        binding.jpeg.setOnClickListener {
            setFileTypeUnselected()
            setQualitySelected(binding.jpeg, binding.textJPEG)
        }
        binding.webp.setOnClickListener {
            setFileTypeUnselected()
            setQualitySelected(binding.webp, binding.textWEBP)
        }
        binding.svg.setOnClickListener {
            setFileTypeUnselected()
            setQualitySelected(binding.svg, binding.textSVG)
        }
        binding.gif.setOnClickListener {
            setFileTypeUnselected()
            setQualitySelected(binding.gif, binding.textGif)
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
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(binding.imageView2)
            }
        }
    }


    private fun setQualitySelected(v: CardView, t: TextView) {
        v.setCardBackgroundColor(Color.parseColor("#4c2d11"))
        t.setTextColor(Color.parseColor("#F6EBDE"))

    }

    private fun setQualityUnselected() {

        binding.ch1.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.ch2.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.ch3.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.lowTexxt.setTextColor(Color.parseColor("#4c2d11"))
        binding.medText.setTextColor(Color.parseColor("#4c2d11"))
        binding.highText.setTextColor(Color.parseColor("#4c2d11"))

    }

    private fun setFileTypeUnselected() {

        binding.png.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.jpg.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.jpeg.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.webp.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.svg.setCardBackgroundColor(Color.parseColor("#F6EBDE"))
        binding.gif.setCardBackgroundColor(Color.parseColor("#F6EBDE"))

        binding.textPNG.setTextColor(Color.parseColor("#4c2d11"))
        binding.textSVG.setTextColor(Color.parseColor("#4c2d11"))
        binding.textJPG.setTextColor(Color.parseColor("#4c2d11"))
        binding.textJPEG.setTextColor(Color.parseColor("#4c2d11"))
        binding.textWEBP.setTextColor(Color.parseColor("#4c2d11"))
        binding.textGif.setTextColor(Color.parseColor("#4c2d11"))
    }

}
