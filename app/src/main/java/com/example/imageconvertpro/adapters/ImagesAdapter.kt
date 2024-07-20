    package com.example.imageconvertpro.adapters

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.recyclerview.widget.DiffUtil
    import androidx.recyclerview.widget.ListAdapter
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.imageconvertpro.R
    import com.example.imageconvertpro.dataclasses.Image
    import com.example.imageconvertpro.databinding.ImageSelectionBinding
    import com.example.imageconvertpro.vars.imageList

    class ImagesAdapter(
        private val onItemClicked: (Image) -> Unit,
        private val onImageSelectedListener: OnImageSelectedListener
    ) : ListAdapter<Image, ImagesAdapter.ImageViewHolder>(DiffCallback()) {

        private val selectedImages = mutableListOf<String>()
        interface OnImageSelectedListener {
            fun onImageSelected(selectedImages: List<String>)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val binding = ImageSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageViewHolder(binding, onItemClicked, onImageSelectedListener, selectedImages)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val image = getItem(position)
            holder.bind(image)
        }

        class ImageViewHolder(
            private val binding: ImageSelectionBinding,
            private val onItemClicked: (Image) -> Unit,
            private val onImageSelectedListener: OnImageSelectedListener,
            private val selectedImages: MutableList<String>
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(image: Image) {
                Glide.with(binding.imageView4.context)
                    .load(image.uri)
                    .into(binding.imageView4)

                if (image.isSelected) {
                    binding.blackLay.visibility = View.VISIBLE
                    binding.tick.visibility = View.VISIBLE
                } else {
                    binding.blackLay.visibility = View.GONE
                    binding.tick.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    if (image.isSelected) {
                        image.isSelected = false
                        binding.blackLay.visibility = View.GONE
                        binding.tick.visibility = View.GONE
                        selectedImages.remove(image.uri.toString())
                    } else {
                        if (selectedImages.size < 6) {
                            image.isSelected = true
                            binding.blackLay.visibility = View.VISIBLE
                            binding.tick.visibility = View.VISIBLE
                            selectedImages.add(image.uri.toString())
                        }
                        else{
                            Toast.makeText(itemView.context, "Max limit reached", Toast.LENGTH_SHORT).show()
                        }
                    }

                    onImageSelectedListener.onImageSelected(selectedImages)
                }
            }
        }

        class DiffCallback : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem == newItem
            }
        }
    }

