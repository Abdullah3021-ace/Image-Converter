package com.example.imageconvertpro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imageconvertpro.R
import com.example.imageconvertpro.adapters.ImagesAdapter
import com.example.imageconvertpro.databinding.FragmentSelectImagesBinding
import com.example.imageconvertpro.mvvm.ImageViewModel
import com.example.imageconvertpro.vars.folderName
import com.example.imageconvertpro.vars.imageList

class SelectImages : Fragment(), ImagesAdapter.OnImageSelectedListener {

    private lateinit var binding: FragmentSelectImagesBinding
    private val viewModel: ImageViewModel by viewModels()
    private lateinit var imagesAdapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagesAdapter = ImagesAdapter({ image ->
            image.isSelected = !image.isSelected
            imagesAdapter.notifyItemChanged(imagesAdapter.currentList.indexOf(image))
        }, this)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = imagesAdapter
        }

        viewModel.imagesInFolder.observe(viewLifecycleOwner, Observer { images ->
            imagesAdapter.submitList(images)
        })

        viewModel.fetchImagesByFolder(folderName)


        binding.done.setOnClickListener {
            findNavController().navigate(R.id.action_selectImages_to_MCompressor)
        }
    }

    override fun onImageSelected(selectedImages: List<String>) {
        if (selectedImages.isEmpty()) {
            binding.done.visibility = View.INVISIBLE
        } else {
            binding.done.visibility = View.VISIBLE
        }
        imageList = selectedImages.toMutableList()
    }
}
