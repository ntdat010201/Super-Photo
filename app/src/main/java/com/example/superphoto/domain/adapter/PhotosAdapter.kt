package com.example.superphoto.domain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.domain.model.PhotoItem

class PhotosAdapter(
    private val photos: List<PhotoItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {
    
    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_rescue_photo)
        private val labelView: TextView = itemView.findViewById(R.id.tv_rescue_action)
        
        fun bind(photo: PhotoItem) {
            imageView.setImageResource(photo.imageResId)
            labelView.setText(photo.labelResId)
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }
    
    override fun getItemCount() = photos.size
}