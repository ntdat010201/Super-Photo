package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.model.PhotoCard

class HorizontalPhotoCardAdapter(
    private val photoCards: List<PhotoCard>,
    private val onCardClick: (PhotoCard) -> Unit = {}
) : RecyclerView.Adapter<HorizontalPhotoCardAdapter.PhotoCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_photo_card, parent, false)
        return PhotoCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoCardViewHolder, position: Int) {
        holder.bind(photoCards[position])
    }

    override fun getItemCount(): Int = photoCards.size

    inner class PhotoCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
            
            // Set photo image
            photoCard.imageResource?.let { resourceId ->
                photoImageView.setImageResource(resourceId)
                photoImageView.visibility = View.VISIBLE

            } ?: run {
                // Fallback to gradient background if no image
                photoImageView.visibility = View.GONE

            }
            
            // Set click listeners
            tryButton.setOnClickListener { onCardClick(photoCard) }
            itemView.setOnClickListener { onCardClick(photoCard) }
        }
    }
}