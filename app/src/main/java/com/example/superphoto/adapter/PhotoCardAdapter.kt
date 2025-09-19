package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        // Load image bằng Glide (nhẹ và cache tốt hơn setImageResource)
        Glide.with(holder.itemView.context)
            .load(photoCard.imageResource ?: R.drawable.template_default_sample)
            .placeholder(R.drawable.template_default_sample)
            .into(holder.photoCardImage)

        // Badge
        if (photoCard.badge.isNotEmpty()) {
            holder.popularBadge.text = photoCard.badge
            holder.popularBadge.visibility = View.VISIBLE
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
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = photoCards.size
            override fun getNewListSize() = newPhotoCards.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return photoCards[oldItemPosition].id == newPhotoCards[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return photoCards[oldItemPosition] == newPhotoCards[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        photoCards = newPhotoCards
        diffResult.dispatchUpdatesTo(this)
    }
}
