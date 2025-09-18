package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.model.FeaturedCard

class FeaturedCardAdapter(
    private val featuredCards: List<FeaturedCard>,
    private val onCardClick: (FeaturedCard) -> Unit = {}
) : RecyclerView.Adapter<FeaturedCardAdapter.FeaturedCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_card, parent, false)
        return FeaturedCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeaturedCardViewHolder, position: Int) {
        holder.bind(featuredCards[position])
    }

    override fun getItemCount(): Int = featuredCards.size

    inner class FeaturedCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        private val badgeText: TextView = itemView.findViewById(R.id.badgeText)
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val tryButton: CardView = itemView.findViewById(R.id.tryButton)

        fun bind(featuredCard: FeaturedCard) {
            titleText.text = featuredCard.title
            badgeText.text = featuredCard.badge

            
            // Set photo image if available
            featuredCard.imageResource?.let { imageRes ->
                photoImageView.setImageResource(imageRes)
                photoImageView.visibility = View.VISIBLE

            } ?: run {
                photoImageView.visibility = View.GONE

            }
            
            // Set click listeners
            tryButton.setOnClickListener { onCardClick(featuredCard) }
            itemView.setOnClickListener { onCardClick(featuredCard) }
        }
    }
}