package com.example.imageconvertpro.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageconvertpro.R
import com.example.imageconvertpro.adapters.ImageFolderAdapter
import com.example.imageconvertpro.databinding.FragmentMultipleImageCompressorBinding
import com.example.imageconvertpro.mvvm.ImageViewModel

class MultipleImageCompressor : Fragment() {
    private lateinit var binding: FragmentMultipleImageCompressorBinding
    private lateinit var viewModel: ImageViewModel
    private lateinit var adapter: ImageFolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMultipleImageCompressorBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        adapter = ImageFolderAdapter()
        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.imageFolders.observe(viewLifecycleOwner, Observer { folders ->
            adapter.submitList(folders)
        })

        return binding.root
    }
}