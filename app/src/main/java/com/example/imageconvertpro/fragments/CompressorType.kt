package com.example.imageconvertpro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.imageconvertpro.R
import com.example.imageconvertpro.databinding.FragmentCompressorTypeBinding
import com.example.imageconvertpro.databinding.FragmentImageCompressorBinding

class CompressorType : Fragment() {
private lateinit var binding : FragmentCompressorTypeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompressorTypeBinding.inflate(layoutInflater)
        binding.materialCardView.setOnClickListener {
            findNavController().navigate(R.id.action_compressorType_to_imageCompressor)
        }
        binding.materiadView.setOnClickListener {
            findNavController().navigate(R.id.action_compressorType_to_multipleImageCompressor)
        }
        return binding.root
    }


}