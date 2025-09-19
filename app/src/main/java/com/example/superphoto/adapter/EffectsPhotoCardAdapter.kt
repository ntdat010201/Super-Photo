package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.model.PhotoCard

class EffectsPhotoCardAdapter(
    private val onCardClick: (PhotoCard) -> Unit = {}
) : ListAdapter<PhotoCard, EffectsPhotoCardAdapter.PhotoCardViewHolder>(PhotoCardDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_effects_photo_card, parent, false)
        return PhotoCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val backgroundView: View = itemView.findViewById(R.id.backgroundView)
        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        private val badgeText: TextView = itemView.findViewById(R.id.badgeText)
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val tryButton: CardView = itemView.findViewById(R.id.tryButton)

        fun bind(photoCard: PhotoCard) {
            titleText.text = photoCard.title

            // Show/hide badge
            if (photoCard.badge.isNotEmpty()) {
                badgeText.text = photoCard.badge
                badgeText.visibility = View.VISIBLE
            } else {
                badgeText.visibility = View.GONE
            }

            // Display image if available, otherwise show gradient background
            photoCard.imageResource?.let { imageRes ->
                photoImageView.setImageResource(imageRes)
                photoImageView.visibility = View.VISIBLE
            } ?: run {
                photoImageView.visibility = View.GONE
            }

            // Set click listeners
            tryButton.setOnClickListener { onCardClick(photoCard) }
            itemView.setOnClickListener { onCardClick(photoCard) }
        }
    }

    object PhotoCardDiffCallback : DiffUtil.ItemCallback<PhotoCard>() {
        override fun areItemsTheSame(oldItem: PhotoCard, newItem: PhotoCard): Boolean {
            // So sánh ID để xác định xem đây có phải là cùng một mục hay không
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoCard, newItem: PhotoCard): Boolean {
            // So sánh nội dung để kiểm tra xem có thay đổi gì không
            return oldItem == newItem
        }
    }
}