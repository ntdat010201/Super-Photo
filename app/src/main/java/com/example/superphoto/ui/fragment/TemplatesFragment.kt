package com.example.superphoto.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superphoto.R
import com.example.superphoto.adapter.PhotoCardAdapter
import com.example.superphoto.adapter.TemplateCategoryAdapter
import com.example.superphoto.model.PhotoCard
import com.example.superphoto.model.TemplateCategory

class TemplatesFragment : Fragment() {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var templatesRecyclerView: RecyclerView
    private lateinit var categoryAdapter: TemplateCategoryAdapter
    private lateinit var photoCardAdapter: PhotoCardAdapter

    private val categories = mutableListOf(
        TemplateCategory("for_you", "For you", "🔥", true),
        TemplateCategory("ai_music_videos", "AI Music Videos", "🎵", false),
        TemplateCategory("meme_generator", "Meme Generator", "😂", false),
        TemplateCategory("dance", "Dance", "💃", false),
        TemplateCategory("magic", "Magic", "✨", false),
        TemplateCategory("portrait", "Portrait", "👤", false),
        TemplateCategory("landscape", "Landscape", "🌄", false)
    )

    private val allPhotoCards = listOf(
        // For you photo cards
        PhotoCard("1", "Dance on street", "🔥 Hot", R.drawable.img1),
        PhotoCard("2", "Magic Elf", "✨ Premium", R.drawable.img2),
        PhotoCard("3", "Dance at sunset", "🔥 Hot", R.drawable.img3),
        PhotoCard("4", "AI Lion", "✨ Premium", R.drawable.img4),
        PhotoCard("5", "Heart Hands", "💖 Popular", R.drawable.img5),
        PhotoCard("6", "Magic Heads", "✨ Premium", R.drawable.img6),
        PhotoCard("7", "Character Style", "🎨 New", R.drawable.img7),
        PhotoCard("8", "Motor Couple", "🔥 Hot", R.drawable.img8),
        PhotoCard("9", "Wedding Style", "💖 Popular", R.drawable.img9),
        PhotoCard("10", "Hug Heart", "🔥 Hot", R.drawable.img10),

        // AI Music Videos photo cards
        PhotoCard("11", "Music Video Style 1", "✨ Premium", R.drawable.img11),
        PhotoCard("12", "Music Video Style 2", "🎵 Music", R.drawable.img12),

        // Meme Generator photo cards
        PhotoCard("13", "Meme Style 1", "😂 Viral", R.drawable.img13),
        PhotoCard("14", "Meme Style 2", "😂 Funny", R.drawable.img1),

        // Dance photo cards
        PhotoCard("15", "Modern Dance", "💃 Dance", R.drawable.img2),
        PhotoCard("16", "Hip Hop Style", "🎤 Urban", R.drawable.img3),

        // Magic photo cards
        PhotoCard("17", "Magic Effect 1", "✨ Premium", R.drawable.img4),
        PhotoCard("18", "Spell Casting", "🔮 Magic", R.drawable.img5),

        // Portrait photo cards
        PhotoCard("19", "Classic Portrait", "👤 Classic", R.drawable.img6),
        PhotoCard("20", "Artistic Portrait", "🎨 Art", R.drawable.img7),

        // Landscape photo cards
        PhotoCard("21", "Nature Scene", "🌿 Nature", R.drawable.img8),
        PhotoCard("22", "Urban Landscape", "🏙️ Urban", R.drawable.img9),

        PhotoCard("23", "Hug Heart", "🔥 Hot", R.drawable.img7),
        PhotoCard("24", "Hug Heart", "🔥 Hot", R.drawable.img4),

        )

    private val currentPhotoCards = mutableListOf<PhotoCard>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_templates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupRecyclerViews()
        loadPhotoCardsForCategory("for_you") // Load default category
    }

    private fun setupViews(view: View) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        templatesRecyclerView = view.findViewById(R.id.templatesRecyclerView)
    }

    private fun setupRecyclerViews() {
        // Setup categories RecyclerView
        categoryAdapter = TemplateCategoryAdapter(categories) { category ->
            onCategorySelected(category)
        }
        categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

        // Setup photo cards RecyclerView
        photoCardAdapter = PhotoCardAdapter(currentPhotoCards) { photoCard ->
            onPhotoCardSelected(photoCard)
        }
        templatesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = photoCardAdapter
            setHasFixedSize(true)   // cải thiện hiệu năng
            itemAnimator = null     // tắt animation thừa để cuộn mượt
        }
    }

    private fun onCategorySelected(category: TemplateCategory) {
        loadPhotoCardsForCategory(category.id)
    }

    private fun onPhotoCardSelected(photoCard: PhotoCard) {
        // TODO: Navigate to photo card preview or editing screen
    }

    private fun loadPhotoCardsForCategory(categoryId: String) {
        val newList = when (categoryId) {
            "for_you" -> allPhotoCards.filter {
                it.badge.contains("Hot") || it.badge.contains("Popular")
            }

            "ai_music_videos" -> allPhotoCards.filter {
                it.badge.contains("Music") || it.title.contains("Music Video")
            }

            "meme_generator" -> allPhotoCards.filter {
                it.badge.contains("Viral") || it.badge.contains("Funny")
            }

            "dance" -> allPhotoCards.filter {
                it.badge.contains("Dance") || it.title.contains("Dance")
            }

            "magic" -> allPhotoCards.filter {
                it.badge.contains("Magic") || it.title.contains("Magic")
            }

            "portrait" -> allPhotoCards.filter {
                it.badge.contains("Classic") || it.badge.contains("Art") || it.title.contains("Portrait")
            }

            "landscape" -> allPhotoCards.filter {
                it.badge.contains("Nature") || it.badge.contains("Urban") || it.title.contains("Landscape")
            }

            else -> allPhotoCards
        }


        photoCardAdapter.updatePhotoCards(newList)
    }


    companion object {
        fun newInstance() = TemplatesFragment()
    }
}
