package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.model.Celebrity

class CelebrityAdapter(
    private val celebrities: List<Celebrity>,
    private val onCelebrityClick: (Celebrity) -> Unit
) : RecyclerView.Adapter<CelebrityAdapter.CelebrityViewHolder>() {

    private var selectedCelebrityId: String? = null

    inner class CelebrityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.celebrityCard)
        val imageView: ImageView = itemView.findViewById(R.id.celebrityImage)
        val nameText: TextView = itemView.findViewById(R.id.celebrityName)
        val selectedIndicator: View = itemView.findViewById(R.id.selectedIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CelebrityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_celebrity, parent, false)
        return CelebrityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CelebrityViewHolder, position: Int) {
        val celebrity = celebrities[position]
        
        holder.nameText.text = celebrity.name
        
        // Set celebrity image from resource or use placeholder
        if (celebrity.imageResource != null) {
            holder.imageView.setImageResource(celebrity.imageResource)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_person_placeholder)
        }
        
        // Update selection state
        val isSelected = celebrity.id == selectedCelebrityId
        holder.selectedIndicator.visibility = if (isSelected) View.VISIBLE else View.GONE
        
        // Update card appearance for selection
        if (isSelected) {
            holder.cardView.setCardBackgroundColor(
                holder.itemView.context.getColor(android.R.color.holo_blue_light)
            )
        } else {
            holder.cardView.setCardBackgroundColor(
                holder.itemView.context.getColor(R.color.card_background)
            )
        }
        
        holder.cardView.setOnClickListener {
            onCelebrityClick(celebrity)
        }
    }

    override fun getItemCount(): Int = celebrities.size

    fun setSelectedCelebrity(celebrityId: String) {
        selectedCelebrityId = celebrityId
        notifyDataSetChanged()
    }
}