package com.example.imageconvertpro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.imageconvertpro.R
import com.example.imageconvertpro.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.imageCompressor.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_compressorType)
        }
        binding.imageConverter.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_imageConverter)
        }
        binding.convertToPdf.setOnClickListener {
//            findNavController().navigate(R.id.action_home2_to_compressorType)
        }
        binding.myCreations.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_myCreations)
        }



        return binding.root
    }


}