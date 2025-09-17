package com.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.superphoto.model.PhotoCard

class EffectsPhotoCardAdapter(
    private val photoCards: List<PhotoCard>,
    private val onCardClick: (PhotoCard) -> Unit = {}
) : RecyclerView.Adapter<EffectsPhotoCardAdapter.PhotoCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_effects_photo_card, parent, false)
        return PhotoCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoCardViewHolder, position: Int) {
        holder.bind(photoCards[position])
    }

    override fun getItemCount(): Int = photoCards.size

    inner class PhotoCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val backgroundView: View = itemView.findViewById(R.id.backgroundView)
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
            
            // Set background gradient
            backgroundView.setBackgroundResource(photoCard.backgroundGradient)
            
            // Set click listeners
            tryButton.setOnClickListener { onCardClick(photoCard) }
            itemView.setOnClickListener { onCardClick(photoCard) }
        }
    }
}