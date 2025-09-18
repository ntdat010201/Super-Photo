package com.example.superphoto.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.superphoto.R

class CreateFragment : Fragment() {

    // Tab UI Elements
    private lateinit var tabImageToVideo: LinearLayout
    private lateinit var tabTextToVideo: LinearLayout
    private lateinit var tabLipSync: LinearLayout
    private lateinit var tabAiImages: LinearLayout
    
    // State variables
    private var selectedTab = 0 // 0: Image to Video, 1: Text to Video, 2: Lip Sync, 3: AI Images

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupClickListeners()
        updateTabSelection(0) // Default to Image to Video
        loadFragment(ImageToVideoFragment()) // Load default fragment
    }

    private fun initViews(view: View) {
        // Tab navigation
        tabImageToVideo = view.findViewById(R.id.tabImageToVideo)
        tabTextToVideo = view.findViewById(R.id.tabTextToVideo)
        tabLipSync = view.findViewById(R.id.tabLipSync)
        tabAiImages = view.findViewById(R.id.tabAiImages)
    }

    private fun setupClickListeners() {
        // Tab navigation clicks
        tabImageToVideo.setOnClickListener { selectTab(0) }
        tabTextToVideo.setOnClickListener { selectTab(1) }
        tabLipSync.setOnClickListener { selectTab(2) }
        tabAiImages.setOnClickListener { selectTab(3) }
    }

    private fun selectTab(tabIndex: Int) {
        selectedTab = tabIndex
        
        // Add click animation
        val tabs = listOf(tabImageToVideo, tabTextToVideo, tabLipSync, tabAiImages)
        animateClick(tabs[tabIndex])
        
        updateTabSelection(tabIndex)
        
        // Load corresponding fragment
        when (tabIndex) {
            0 -> loadFragment(ImageToVideoFragment())
            1 -> loadFragment(TextToVideoFragment())
            2 -> loadFragment(LipSyncFragment())
            3 -> loadFragment(AIImagesFragment())
        }
    }

    private fun updateTabSelection(selectedIndex: Int) {
        val tabs = listOf(tabImageToVideo, tabTextToVideo, tabLipSync, tabAiImages)
        
        tabs.forEachIndexed { index, tab ->
            val isSelected = index == selectedIndex
            
            // Update background
            val backgroundRes = if (isSelected) R.drawable.ai_tab_selected else R.drawable.ai_tab_selector
            tab.setBackgroundResource(backgroundRes)
            
            // Update icon and text colors
            val iconView = tab.getChildAt(0) as ImageView
            val textView = tab.getChildAt(1) as TextView
            
            val colorRes = if (isSelected) R.color.white else R.color.ai_tab_unselected
            val color = ContextCompat.getColor(requireContext(), colorRes)
            
            iconView.setColorFilter(color)
            textView.setTextColor(color)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun animateClick(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 150
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }

    companion object {
        fun newInstance() = CreateFragment()
    }
}