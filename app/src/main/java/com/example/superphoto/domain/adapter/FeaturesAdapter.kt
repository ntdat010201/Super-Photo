package com.example.superphoto.domain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.domain.model.FeatureItem

class FeaturesAdapter(
    private val features: List<FeatureItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {
    
    inner class FeatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.iv_feature_icon)
        private val nameView: TextView = itemView.findViewById(R.id.tv_feature_name)
        
        fun bind(feature: FeatureItem) {
            iconView.setImageResource(feature.iconResId)
            nameView.setText(feature.labelResId)
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feature, parent, false)
        return FeatureViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(features[position])
    }
    
    override fun getItemCount() = features.size
}