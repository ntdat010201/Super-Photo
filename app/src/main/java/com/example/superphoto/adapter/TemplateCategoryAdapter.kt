package com.example.superphoto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.model.TemplateCategory

class TemplateCategoryAdapter(
    private var categories: List<TemplateCategory>,
    private val onCategoryClick: (TemplateCategory) -> Unit
) : RecyclerView.Adapter<TemplateCategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: TextView = itemView.findViewById(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        val isSelected = position == selectedPosition

        holder.categoryName.text = category.name
        holder.categoryIcon.text = category.icon

        // Update selection state
        val context = holder.itemView.context
        if (isSelected) {
            holder.itemView.setBackgroundResource(R.drawable.template_category_selected)
            holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.categoryIcon.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.template_category_unselected)
            holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            holder.categoryIcon.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
        }

        holder.itemView.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val oldPosition = selectedPosition
                selectedPosition = currentPosition
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
                onCategoryClick(category)
            }
        }
    }

    override fun getItemCount() = categories.size

    fun updateCategories(newCategories: List<TemplateCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}