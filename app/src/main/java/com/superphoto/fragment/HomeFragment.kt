package com.superphoto.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.superphoto.R
import com.superphoto.adapter.FeaturedCardAdapter
import com.superphoto.adapter.HorizontalPhotoCardAdapter
import com.superphoto.model.FeaturedCard
import com.superphoto.model.PhotoCard

class HomeFragment : Fragment() {

    private lateinit var featuredViewPager: ViewPager2
    private lateinit var recentlyViewedRecyclerView: RecyclerView
    private lateinit var hotEffectsRecyclerView: RecyclerView
    private lateinit var trendingRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViews(view)
        setupFeaturedCards()
        setupRecyclerViews()
    }

    private fun initViews(view: View) {
        featuredViewPager = view.findViewById(R.id.featuredViewPager)
        recentlyViewedRecyclerView = view.findViewById(R.id.recentlyViewedRecyclerView)
        hotEffectsRecyclerView = view.findViewById(R.id.hotEffectsRecyclerView)
        trendingRecyclerView = view.findViewById(R.id.trendingRecyclerView)
        
        // Setup See All click listeners
        view.findViewById<View>(R.id.seeAllRecentlyViewed)?.setOnClickListener {
            // TODO: Navigate to Recently Viewed full screen
        }
        
        view.findViewById<View>(R.id.seeAllHotEffects)?.setOnClickListener {
            // TODO: Navigate to Hot Effects full screen
        }
        
        view.findViewById<View>(R.id.seeAllTrending)?.setOnClickListener {
            // TODO: Navigate to Trending full screen
        }
    }

    private fun setupFeaturedCards() {
        val featuredCards = listOf(
            FeaturedCard(
                id = "1",
                title = "AI Portrait",
                description = "Transform your photos with AI",
                badge = "🔥 Trending",
                backgroundGradient = R.drawable.card_gradient_purple
            ),
            FeaturedCard(
                id = "2",
                title = "Vintage Filter",
                description = "Classic retro effects",
                badge = "✨ New",
                backgroundGradient = R.drawable.card_gradient_orange
            ),
            FeaturedCard(
                id = "3",
                title = "Neon Glow",
                description = "Futuristic neon effects",
                badge = "🔥 Hot",
                backgroundGradient = R.drawable.card_gradient_blue
            )
        )

        val adapter = FeaturedCardAdapter(featuredCards) { card ->
            // Handle card click
        }
        featuredViewPager.adapter = adapter
    }

    private fun setupRecyclerViews() {
        // Recently Viewed
        val recentlyViewedCards = listOf(
            PhotoCard("1", "Sunset", "🔥 Hot", R.drawable.card_gradient_orange),
            PhotoCard("2", "Ocean", "", R.drawable.card_gradient_blue),
            PhotoCard("3", "Forest", "✨ New", R.drawable.card_gradient_green),
            PhotoCard("4", "City", "", R.drawable.card_gradient_purple),
            PhotoCard("5", "Mountain", "🔥 Popular", R.drawable.card_gradient_brown)
        )

        recentlyViewedRecyclerView.layoutManager = 
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recentlyViewedRecyclerView.adapter = HorizontalPhotoCardAdapter(recentlyViewedCards) { card ->
            // Handle card click
        }

        // Hot Effects
        val hotEffectsCards = listOf(
            PhotoCard("1", "Vintage", "🔥 Hot", R.drawable.card_gradient_yellow),
            PhotoCard("2", "Black & White", "", R.drawable.card_gradient_gray),
            PhotoCard("3", "Sepia", "✨ Classic", R.drawable.card_gradient_brown),
            PhotoCard("4", "Neon", "🔥 Trending", R.drawable.card_gradient_purple),
            PhotoCard("5", "Retro", "", R.drawable.card_gradient_orange)
        )

        hotEffectsRecyclerView.layoutManager = 
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hotEffectsRecyclerView.adapter = HorizontalPhotoCardAdapter(hotEffectsCards) { card ->
            // Handle card click
        }

        // Trending
        val trendingCards = listOf(
            PhotoCard("1", "Dance at sunset", "🔥 Hot", R.drawable.card_gradient_yellow),
            PhotoCard("2", "Heart Hands", "🔥 Popular", R.drawable.card_gradient_brown),
            PhotoCard("3", "City Lights", "✨ New", R.drawable.card_gradient_blue),
            PhotoCard("4", "Nature", "", R.drawable.card_gradient_green),
            PhotoCard("5", "Portrait", "🔥 Trending", R.drawable.card_gradient_purple)
        )

        trendingRecyclerView.layoutManager = 
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        trendingRecyclerView.adapter = HorizontalPhotoCardAdapter(trendingCards) { card ->
            // Handle card click
        }
    }



    companion object {
        fun newInstance() = HomeFragment()
    }
}