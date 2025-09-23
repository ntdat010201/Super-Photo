package com.example.superphoto.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.superphoto.ai.SmartSuggestion

class SmartSuggestionsAdapter(
    private var suggestions: List<SmartSuggestion>,
    private val onSuggestionClick: (SmartSuggestion) -> Unit
) : RecyclerView.Adapter<SmartSuggestionsAdapter.SuggestionViewHolder>() {

    class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.suggestion_icon)
        val titleTextView: TextView = itemView.findViewById(R.id.suggestion_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.suggestion_description)
        val confidenceTextView: TextView = itemView.findViewById(R.id.suggestion_confidence)
        val priorityBadge: TextView = itemView.findViewById(R.id.suggestion_priority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_smart_suggestion, parent, false)
        return SuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = suggestions[position]
        
        // Set transformation icon
        holder.iconImageView.setImageResource(getIconResource(suggestion.transformation.icon))
        
        // Set title and description
        holder.titleTextView.text = suggestion.transformation.name
        holder.descriptionTextView.text = suggestion.reason
        
        // Set confidence percentage
        val confidencePercent = (suggestion.confidence * 100).toInt()
        holder.confidenceTextView.text = "${confidencePercent}%"
        
        // Set priority badge
        holder.priorityBadge.text = "#${suggestion.priority}"
        holder.priorityBadge.setBackgroundResource(getPriorityBackground(suggestion.priority))
        
        // Set click listener
        holder.itemView.setOnClickListener {
            onSuggestionClick(suggestion)
        }
        
        // Add animation based on priority
        holder.itemView.alpha = if (suggestion.priority <= 2) 1.0f else 0.8f
    }

    override fun getItemCount(): Int = suggestions.size

    fun updateSuggestions(newSuggestions: List<SmartSuggestion>) {
        suggestions = newSuggestions
        notifyDataSetChanged()
    }

    private fun getIconResource(iconName: String): Int {
        return when (iconName) {
            "🎨" -> R.drawable.ic_style_transfer
            "🖼️" -> R.drawable.ic_background_removal
            "👤" -> R.drawable.ic_face_swap
            "✨" -> R.drawable.ic_ai_enhance
            "🎨" -> R.drawable.ic_colorize
            "🗑️" -> R.drawable.ic_object_removal
            "🤖" -> R.drawable.ic_ai_general
            else -> R.drawable.ic_ai_general
        }
    }

    private fun getPriorityBackground(priority: Int): Int {
        return when (priority) {
            1 -> R.drawable.bg_priority_high
            2 -> R.drawable.bg_priority_medium
            3 -> R.drawable.bg_priority_normal
            else -> R.drawable.bg_priority_low
        }
    }
}