package com.example.imageconvertpro.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageconvertpro.databinding.ImageCompressItemBinding

class ImageAdapter(private val images: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val binding: ImageCompressItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageCompressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUri = images[position]
        Glide.with(holder.binding.iview.context)
            .load(imageUri)
            .into(holder.binding.iview)
    }

    override fun getItemCount(): Int = images.size
}
