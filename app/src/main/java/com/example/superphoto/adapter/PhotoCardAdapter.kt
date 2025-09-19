package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.model.PhotoCard

class PhotoCardAdapter(
    private var photoCards: List<PhotoCard>,
    private val onPhotoCardClick: (PhotoCard) -> Unit
) : RecyclerView.Adapter<PhotoCardAdapter.PhotoCardViewHolder>() {

    class PhotoCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoCardImage: ImageView = itemView.findViewById(R.id.templateImage)
        val photoCardTitle: TextView = itemView.findViewById(R.id.templateName)
        val premiumBadge: ImageView = itemView.findViewById(R.id.premiumBadge)
        val popularBadge: TextView = itemView.findViewById(R.id.popularBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template, parent, false)
        return PhotoCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoCardViewHolder, position: Int) {
        val photoCard = photoCards[position]

        holder.photoCardTitle.text = photoCard.title
        
        // Use imageResource if available, otherwise set default placeholder
        if (photoCard.imageResource != null) {
            holder.photoCardImage.setImageResource(photoCard.imageResource)
        } else {
            holder.photoCardImage.setImageResource(R.drawable.template_default_sample)
        }

        // Show badge text if available
        if (photoCard.badge.isNotEmpty()) {
            holder.popularBadge.text = photoCard.badge
            holder.popularBadge.visibility = View.VISIBLE
            // Hide premium badge when using text badge
            holder.premiumBadge.visibility = View.GONE
        } else {
            holder.popularBadge.visibility = View.GONE
            holder.premiumBadge.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onPhotoCardClick(photoCard)
        }
    }

    override fun getItemCount() = photoCards.size

    fun updatePhotoCards(newPhotoCards: List<PhotoCard>) {
        photoCards = newPhotoCards
        notifyDataSetChanged()
    }

    fun filterByCategory(category: String) {
        // This would be implemented with a full dataset and filtering logic
        notifyDataSetChanged()
    }
}