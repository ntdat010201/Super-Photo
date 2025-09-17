package com.example.superphoto.domain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.domain.model.PhotoSample

class PhotoSampleAdapter(
    private var photoSamples: List<PhotoSample>,
    private val onItemClick: (PhotoSample) -> Unit
) : RecyclerView.Adapter<PhotoSampleAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPhotoSample: ImageView = itemView.findViewById(R.id.iv_photo_sample)
        private val tvPhotoTitle: TextView = itemView.findViewById(R.id.tv_photo_title)
        private val tvPhotoSubtitle: TextView = itemView.findViewById(R.id.tv_photo_subtitle)

        fun bind(photoSample: PhotoSample) {
            tvPhotoTitle.text = photoSample.title
            tvPhotoSubtitle.text = photoSample.subtitle
            ivPhotoSample.setImageResource(photoSample.imageRes)
            
            itemView.setOnClickListener {
                onItemClick(photoSample)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo_sample, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoSamples[position])
    }

    override fun getItemCount(): Int = photoSamples.size

    fun updateItems(newItems: List<PhotoSample>) {
        photoSamples = newItems
        notifyDataSetChanged()
    }
}