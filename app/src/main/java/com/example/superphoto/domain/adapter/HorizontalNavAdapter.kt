package com.example.superphoto.domain.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.domain.model.NavItem

class HorizontalNavAdapter(
    private var navItems: List<NavItem>,
    private val onItemClick: (NavItem, Int) -> Unit
) : RecyclerView.Adapter<HorizontalNavAdapter.NavViewHolder>() {

    private var selectedPosition = 0

    inner class NavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivNavIcon: ImageView = itemView.findViewById(R.id.iv_nav_icon)
        private val tvNavTitle: TextView = itemView.findViewById(R.id.tv_nav_title)
        private val viewIndicator: View = itemView.findViewById(R.id.view_indicator)

        fun bind(navItem: NavItem, position: Int) {
            tvNavTitle.text = navItem.title
            ivNavIcon.setImageResource(navItem.iconRes)
            
            val isSelected = position == selectedPosition
            
            // Update colors based on selection state
            val iconColor = if (isSelected) {
                ContextCompat.getColor(itemView.context, R.color.bottom_nav_selected)
            } else {
                ContextCompat.getColor(itemView.context, R.color.bottom_nav_unselected)
            }
            
            val textColor = if (isSelected) {
                ContextCompat.getColor(itemView.context, R.color.bottom_nav_selected)
            } else {
                ContextCompat.getColor(itemView.context, R.color.bottom_nav_unselected)
            }
            
            ivNavIcon.setColorFilter(iconColor)
            tvNavTitle.setTextColor(textColor)
            viewIndicator.visibility = if (isSelected) View.VISIBLE else View.GONE
            
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(navItem, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_nav, parent, false)
        return NavViewHolder(view)
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        holder.bind(navItems[position], position)
    }

    override fun getItemCount(): Int = navItems.size

    fun updateItems(newItems: List<NavItem>) {
        navItems = newItems
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }
}