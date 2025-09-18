package com.example.superphoto.ui.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.superphoto.R

class AIImagesFragment : Fragment() {

    private lateinit var promptEditText: EditText
    private lateinit var negativePromptEditText: EditText
    private lateinit var generateButton: Button
    
    // Style selection
    private lateinit var styleRealistic: TextView
    private lateinit var styleArtistic: TextView
    private lateinit var styleCartoon: TextView
    private lateinit var styleAnime: TextView
    private lateinit var styleAbstract: TextView
    private lateinit var styleVintage: TextView
    
    // Size selection
    private lateinit var sizeSquare: TextView
    private lateinit var sizePortrait: TextView
    private lateinit var sizeLandscape: TextView
    
    // Count selection
    private lateinit var count1: TextView
    private lateinit var count2: TextView
    private lateinit var count4: TextView
    
    private var selectedStyle = "Realistic"
    private var selectedSize = "Square"
    private var selectedCount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ai_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initViews(view)
        setupListeners()
        updateGenerateButtonState()
    }

    private fun initViews(view: View) {
        promptEditText = view.findViewById(R.id.promptEditText)
        negativePromptEditText = view.findViewById(R.id.negativePromptEditText)
        generateButton = view.findViewById(R.id.generateButton)
        
        // Style views
        styleRealistic = view.findViewById(R.id.styleRealistic)
        styleArtistic = view.findViewById(R.id.styleArtistic)
        styleCartoon = view.findViewById(R.id.styleCartoon)
        styleAnime = view.findViewById(R.id.styleAnime)
        styleAbstract = view.findViewById(R.id.styleAbstract)
        styleVintage = view.findViewById(R.id.styleVintage)
        
        // Size views
        sizeSquare = view.findViewById(R.id.sizeSquare)
        sizePortrait = view.findViewById(R.id.sizePortrait)
        sizeLandscape = view.findViewById(R.id.sizeLandscape)
        
        // Count views
        count1 = view.findViewById(R.id.count1)
        count2 = view.findViewById(R.id.count2)
        count4 = view.findViewById(R.id.count4)
    }

    private fun setupListeners() {
        // Prompt text watcher
        promptEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateGenerateButtonState()
            }
        })

        // Style selection listeners
        styleRealistic.setOnClickListener { selectStyle("Realistic", styleRealistic) }
        styleArtistic.setOnClickListener { selectStyle("Artistic", styleArtistic) }
        styleCartoon.setOnClickListener { selectStyle("Cartoon", styleCartoon) }
        styleAnime.setOnClickListener { selectStyle("Anime", styleAnime) }
        styleAbstract.setOnClickListener { selectStyle("Abstract", styleAbstract) }
        styleVintage.setOnClickListener { selectStyle("Vintage", styleVintage) }

        // Size selection listeners
        sizeSquare.setOnClickListener { selectSize("Square", sizeSquare) }
        sizePortrait.setOnClickListener { selectSize("Portrait", sizePortrait) }
        sizeLandscape.setOnClickListener { selectSize("Landscape", sizeLandscape) }

        // Count selection listeners
        count1.setOnClickListener { selectCount(1, count1) }
        count2.setOnClickListener { selectCount(2, count2) }
        count4.setOnClickListener { selectCount(4, count4) }

        // Generate button listener
        generateButton.setOnClickListener {
            if (isValidInput()) {
                generateImages()
            }
        }
    }

    private fun selectStyle(style: String, selectedView: TextView) {
        selectedStyle = style
        
        // Reset all style buttons
        val styleViews = listOf(styleRealistic, styleArtistic, styleCartoon, styleAnime, styleAbstract, styleVintage)
        styleViews.forEach { view ->
            view.setBackgroundResource(R.drawable.ai_duration_button_unselected)
            view.setTextColor(resources.getColor(R.color.ai_text_secondary, null))
        }
        
        // Highlight selected style
        selectedView.setBackgroundResource(R.drawable.ai_duration_button)
        selectedView.setTextColor(resources.getColor(R.color.ai_text_primary, null))
        
        // Add animation
        animateSelection(selectedView)
    }

    private fun selectSize(size: String, selectedView: TextView) {
        selectedSize = size
        
        // Reset all size buttons
        val sizeViews = listOf(sizeSquare, sizePortrait, sizeLandscape)
        sizeViews.forEach { view ->
            view.setBackgroundResource(R.drawable.ai_duration_button_unselected)
            view.setTextColor(resources.getColor(R.color.ai_text_secondary, null))
        }
        
        // Highlight selected size
        selectedView.setBackgroundResource(R.drawable.ai_duration_button)
        selectedView.setTextColor(resources.getColor(R.color.ai_text_primary, null))
        
        // Add animation
        animateSelection(selectedView)
    }

    private fun selectCount(count: Int, selectedView: TextView) {
        selectedCount = count
        
        // Reset all count buttons
        val countViews = listOf(count1, count2, count4)
        countViews.forEach { view ->
            view.setBackgroundResource(R.drawable.ai_duration_button_unselected)
            view.setTextColor(resources.getColor(R.color.ai_text_secondary, null))
        }
        
        // Highlight selected count
        selectedView.setBackgroundResource(R.drawable.ai_duration_button)
        selectedView.setTextColor(resources.getColor(R.color.ai_text_primary, null))
        
        // Add animation
        animateSelection(selectedView)
    }

    private fun animateSelection(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.1f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.1f, 1.0f)
        scaleX.duration = 200
        scaleY.duration = 200
        scaleX.start()
        scaleY.start()
    }

    private fun isValidInput(): Boolean {
        return promptEditText.text.toString().trim().isNotEmpty()
    }

    private fun updateGenerateButtonState() {
        val isValid = isValidInput()
        generateButton.isEnabled = isValid
        generateButton.alpha = if (isValid) 1.0f else 0.5f
    }

    private fun generateImages() {
        val prompt = promptEditText.text.toString().trim()
        val negativePrompt = negativePromptEditText.text.toString().trim()
        
        // Disable generate button during processing
        generateButton.isEnabled = false
        generateButton.text = "Generating..."
        
        // TODO: Implement actual image generation logic
        // This would typically involve calling an AI image generation API
        // For now, we'll simulate the process
        
        // Simulate generation delay
        generateButton.postDelayed({
            generateButton.isEnabled = true
            generateButton.text = "🎨 Generate Images"
            
            // TODO: Handle generated images
            // Show results in a new screen or dialog
            
        }, 3000) // 3 second simulation
    }

    companion object {
        fun newInstance(): AIImagesFragment {
            return AIImagesFragment()
        }
    }
}