package com.example.imageconvertpro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageconvertpro.R
import com.example.imageconvertpro.databinding.FolderItemBinding
import com.example.imageconvertpro.dataclasses.ImageFolder
import com.example.imageconvertpro.vars.folderName

class ImageFolderAdapter :
    ListAdapter<ImageFolder, ImageFolderAdapter.ViewHolder>(ImageFolderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FolderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: FolderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageFolder: ImageFolder) {

            binding.folderName.text = imageFolder.name
            binding.itemSize.text = imageFolder.count.toString()

            Glide.with(binding.folderImg.context)
                .load(imageFolder.thumbnailUri)
                .into(binding.folderImg)

            itemView.setOnClickListener {
                folderName = imageFolder.name
                it.findNavController().navigate(R.id.action_multipleImageCompressor_to_selectImages)
            }
        }
    }
}

class ImageFolderDiffCallback : DiffUtil.ItemCallback<ImageFolder>() {
    override fun areItemsTheSame(oldItem: ImageFolder, newItem: ImageFolder): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ImageFolder, newItem: ImageFolder): Boolean {
        return oldItem == newItem
    }
}