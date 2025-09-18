package com.example.superphoto.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.superphoto.R

class AIImagesFragment : Fragment() {

    // UI Elements
    private lateinit var uploadArea: LinearLayout
    private lateinit var promptEditText: EditText
    private lateinit var refreshPromptButton: ImageView
    private lateinit var refreshHintsButton: ImageView

    // Hint chips
    private lateinit var hintDancingStreet: TextView
    private lateinit var hintVolcanoSea: TextView
    private lateinit var hintSurfingSea: TextView

    // Aspect ratio buttons
    private lateinit var aspectRatio1to1: TextView
    private lateinit var aspectRatio16to9: TextView
    private lateinit var aspectRatio9to16: TextView
    private lateinit var aspectRatio3to4: TextView

    // Style selection
    private lateinit var styleNone: LinearLayout
    private lateinit var stylePhoto: LinearLayout
    private lateinit var styleAnime: LinearLayout
    private lateinit var styleIllustration: LinearLayout

    private lateinit var generateButton: Button

    // State variables
    private var selectedAspectRatio = "1:1" // Default aspect ratio
    private var selectedStyle = "None" // Default style
    private var selectedImageUri: Uri? = null

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                updateUploadArea(uri)
            }
        }
    }

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
        setupClickListeners()
        updateAspectRatioSelection("1:1") // Set default selection
        updateStyleSelection("None") // Set default selection
    }

    private fun initViews(view: View) {
        // Upload area
        uploadArea = view.findViewById(R.id.uploadArea)

        // Prompt section
        promptEditText = view.findViewById(R.id.promptEditText)
        refreshPromptButton = view.findViewById(R.id.refreshPromptButton)

        // Hints section
        refreshHintsButton = view.findViewById(R.id.refreshHintsButton)
        hintDancingStreet = view.findViewById(R.id.hintDancingStreet)
        hintVolcanoSea = view.findViewById(R.id.hintVolcanoSea)
        hintSurfingSea = view.findViewById(R.id.hintSurfingSea)

        // Aspect ratio buttons
        aspectRatio1to1 = view.findViewById(R.id.aspectRatio1to1)
        aspectRatio16to9 = view.findViewById(R.id.aspectRatio16to9)
        aspectRatio9to16 = view.findViewById(R.id.aspectRatio9to16)
        aspectRatio3to4 = view.findViewById(R.id.aspectRatio3to4)

        // Style selection
        styleNone = view.findViewById(R.id.styleNone)
        stylePhoto = view.findViewById(R.id.stylePhoto)
        styleAnime = view.findViewById(R.id.styleAnime)
        styleIllustration = view.findViewById(R.id.styleIllustration)

        // Generate button
        generateButton = view.findViewById(R.id.generateButton)
    }

    private fun setupClickListeners() {
        // Upload area click
        uploadArea.setOnClickListener {
            animateClick(uploadArea)
            openImagePicker()
        }

        // Refresh buttons
        refreshPromptButton.setOnClickListener {
            animateClick(refreshPromptButton)
            refreshPrompt()
        }

        refreshHintsButton.setOnClickListener {
            animateClick(refreshHintsButton)
            refreshHints()
        }

        // Hint chips
        hintDancingStreet.setOnClickListener {
            animateClick(hintDancingStreet)
            applyHint("Dancing Street")
        }

        hintVolcanoSea.setOnClickListener {
            animateClick(hintVolcanoSea)
            applyHint("Volcano by the Sea")
        }

        hintSurfingSea.setOnClickListener {
            animateClick(hintSurfingSea)
            applyHint("Surfing on the sea")
        }

        // Aspect ratio selection
        aspectRatio1to1.setOnClickListener {
            animateClick(aspectRatio1to1)
            updateAspectRatioSelection("1:1")
        }

        aspectRatio16to9.setOnClickListener {
            animateClick(aspectRatio16to9)
            updateAspectRatioSelection("16:9")
        }

        aspectRatio9to16.setOnClickListener {
            animateClick(aspectRatio9to16)
            updateAspectRatioSelection("9:16")
        }

        aspectRatio3to4.setOnClickListener {
            animateClick(aspectRatio3to4)
            updateAspectRatioSelection("3:4")
        }

        // Style selection
        styleNone.setOnClickListener {
            animateClick(styleNone)
            updateStyleSelection("None")
        }

        stylePhoto.setOnClickListener {
            animateClick(stylePhoto)
            updateStyleSelection("Photo")
        }

        styleAnime.setOnClickListener {
            animateClick(styleAnime)
            updateStyleSelection("Anime")
        }

        styleIllustration.setOnClickListener {
            animateClick(styleIllustration)
            updateStyleSelection("Illustration")
        }

        // Generate button
        generateButton.setOnClickListener {
            animateClick(generateButton)
            generateAIImage()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun updateUploadArea(uri: Uri) {
        // TODO: Update upload area to show selected image
        Toast.makeText(context, "Image selected successfully", Toast.LENGTH_SHORT).show()
    }

    private fun refreshPrompt() {
        val prompts = listOf(
            "Create a vibrant street art scene with colorful graffiti and urban atmosphere",
            "Transform this into a magical fantasy landscape with mystical creatures",
            "Generate a futuristic cyberpunk cityscape with neon lights and flying cars",
            "Create a peaceful nature scene with flowing water and lush greenery",
            "Transform into a vintage retro style with warm tones and classic elements"
        )
        promptEditText.setText(prompts.random())
    }

    private fun refreshHints() {
        val hintSets = listOf(
            Triple("Ocean Waves", "Mountain Peak", "Forest Path"),
            Triple("City Lights", "Desert Sunset", "Snowy Village"),
            Triple("Space Station", "Underwater City", "Sky Castle"),
            Triple("Dancing Street", "Volcano by the Sea", "Surfing on the sea")
        )

        val selectedSet = hintSets.random()
        hintDancingStreet.text = selectedSet.first
        hintVolcanoSea.text = selectedSet.second
        hintSurfingSea.text = selectedSet.third
    }

    private fun applyHint(hint: String) {
        val currentText = promptEditText.text.toString()
        val newText = if (currentText.isEmpty()) {
            hint
        } else {
            "$currentText, $hint"
        }
        promptEditText.setText(newText)
        Toast.makeText(context, "Hint applied: $hint", Toast.LENGTH_SHORT).show()
    }

    private fun updateAspectRatioSelection(ratio: String) {
        selectedAspectRatio = ratio

        // Reset all aspect ratio buttons
        val aspectRatios = listOf(aspectRatio1to1, aspectRatio16to9, aspectRatio9to16, aspectRatio3to4)
        aspectRatios.forEach { button ->
            button.setBackgroundResource(R.drawable.ai_aspect_ratio_unselected)
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.ai_text_secondary))
            button.typeface = null
        }

        // Highlight selected aspect ratio
        val selectedButton = when (ratio) {
            "1:1" -> aspectRatio1to1
            "16:9" -> aspectRatio16to9
            "9:16" -> aspectRatio9to16
            "3:4" -> aspectRatio3to4
            else -> aspectRatio1to1
        }

        selectedButton.setBackgroundResource(R.drawable.ai_aspect_ratio_selected)
        selectedButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.ai_text_primary))
        selectedButton.setTypeface(null, android.graphics.Typeface.BOLD)
    }

    private fun updateStyleSelection(style: String) {
        selectedStyle = style

        // Reset all style buttons
        val styles = listOf(styleNone, stylePhoto, styleAnime, styleIllustration)
        styles.forEach { styleLayout ->
            styleLayout.setBackgroundResource(R.drawable.ai_style_unselected)
            val textView = styleLayout.getChildAt(1) as TextView
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.ai_text_secondary))
            textView.typeface = null
        }

        // Highlight selected style
        val selectedStyleLayout = when (style) {
            "None" -> styleNone
            "Photo" -> stylePhoto
            "Anime" -> styleAnime
            "Illustration" -> styleIllustration
            else -> styleNone
        }

        selectedStyleLayout.setBackgroundResource(R.drawable.ai_style_selected)
        val textView = selectedStyleLayout.getChildAt(1) as TextView
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.ai_text_primary))
        textView.setTypeface(null, android.graphics.Typeface.BOLD)
    }

    private fun generateAIImage() {
        val prompt = promptEditText.text.toString().trim()

        if (prompt.isEmpty()) {
            Toast.makeText(context, "Please enter a prompt", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Implement AI image generation logic
        Toast.makeText(
            context,
            "Generating AI image with style: $selectedStyle, aspect ratio: $selectedAspectRatio",
            Toast.LENGTH_LONG
        ).show()
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
        fun newInstance(): AIImagesFragment {
            return AIImagesFragment()
        }
    }
}