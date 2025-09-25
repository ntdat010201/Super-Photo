package com.example.superphoto.ui.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.superphoto.R
import com.example.superphoto.data.repository.AIGenerationRepository
import com.example.superphoto.data.model.AspectRatio
import com.example.superphoto.data.model.StyleOption
import com.example.superphoto.utils.GenerationStatusManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AIImagesFragment : Fragment() {

    // Dependency injection
    private val aiGenerationRepository: AIGenerationRepository by inject()
    
    // Status manager
    private lateinit var statusManager: GenerationStatusManager

    // UI Elements
    private lateinit var uploadArea: LinearLayout
    private lateinit var selectedImageView: ImageView
    private lateinit var uploadPlaceholder: LinearLayout
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

    // Result section
    private lateinit var resultSection: LinearLayout
    private lateinit var resultImageView: ImageView
    private lateinit var loadingProgress: ProgressBar
    private lateinit var downloadButton: Button
    private lateinit var shareButton: Button

    // State variables
    private var selectedAspectRatio = "1:1" // Default aspect ratio
    private var selectedStyle = "None" // Default style
    private var selectedImageUri: Uri? = null
    private var generatedImageUri: Uri? = null
    private var isGenerating = false

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
        
        // Initialize status manager
        statusManager = GenerationStatusManager(requireContext(), aiGenerationRepository, lifecycleScope)
    }

    private fun initViews(view: View) {
        // Upload area
        uploadArea = view.findViewById(R.id.uploadArea)
        selectedImageView = view.findViewById(R.id.selectedImageView)
        uploadPlaceholder = view.findViewById(R.id.uploadPlaceholder)

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

        // Result section
        resultSection = view.findViewById(R.id.resultSection)
        resultImageView = view.findViewById(R.id.resultImageView)
        loadingProgress = view.findViewById(R.id.loadingProgress)
        downloadButton = view.findViewById(R.id.downloadButton)
        shareButton = view.findViewById(R.id.shareButton)
    }

    private fun setupClickListeners() {
        // TextWatcher for prompt EditText
        promptEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateGenerateButtonState()
            }
        })
        
        // Upload area click listener
        uploadArea.setOnClickListener {
            animateClick(uploadArea)
            openImagePicker()
        }

        // Selected image click listener for re-selection
        selectedImageView.setOnClickListener {
            animateClick(selectedImageView)
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

        // Result action buttons
        downloadButton.setOnClickListener {
            animateClick(downloadButton)
            downloadGeneratedImage()
        }

        shareButton.setOnClickListener {
            animateClick(shareButton)
            shareGeneratedImage()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun updateUploadArea(uri: Uri) {
        // Hide placeholder and show selected image
        uploadPlaceholder.visibility = View.GONE
        selectedImageView.visibility = View.VISIBLE
        
        // Load image into ImageView
        selectedImageView.setImageURI(uri)
        
        // Update generate button state
        updateGenerateButtonState()
        
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
        updateGenerateButtonState()
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
        updateGenerateButtonState()
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

        if (isGenerating) {
            Toast.makeText(context, "Generation in progress, please wait...", Toast.LENGTH_SHORT).show()
            return
        }

        isGenerating = true
        updateGenerateButtonState()
        
        // Show loading state
        showLoadingState()

        lifecycleScope.launch {
            try {
                // Convert selectedStyle to StyleOption enum
                val styleOption = when (selectedStyle.lowercase()) {
                    "photo" -> StyleOption.PHOTO
                    "anime" -> StyleOption.ANIME
                    "illustration" -> StyleOption.ILLUSTRATION
                    else -> StyleOption.NONE
                }

                // Convert selectedAspectRatio to AspectRatio enum
                val aspectRatioOption = when (selectedAspectRatio) {
                    "16:9" -> AspectRatio.LANDSCAPE
                    "9:16" -> AspectRatio.PORTRAIT
                    "3:4" -> AspectRatio.PHOTO
                    else -> AspectRatio.SQUARE
                }

                val result = aiGenerationRepository.generateAIImage(
                    sourceImageUri = selectedImageUri,
                    prompt = prompt,
                    aspectRatio = aspectRatioOption.value,
                    style = styleOption.value
                )

                result.fold(
                    onSuccess = { response ->
                        Toast.makeText(
                            context,
                            "AI image generation started! Task ID: ${response.taskId}",
                            Toast.LENGTH_SHORT
                        ).show()
                        
                        // Start status polling
                        statusManager.startStatusPolling(response.taskId, object : GenerationStatusManager.StatusCallback {
                            override fun onProgress(progress: Int, message: String) {
                                // Update progress on main thread
                                activity?.runOnUiThread {
                                    Toast.makeText(context, "Progress: $progress% - $message", Toast.LENGTH_SHORT).show()
                                }
                            }
                            
                            override fun onCompleted(resultUri: Uri?) {
                                activity?.runOnUiThread {
                                    isGenerating = false
                                    updateGenerateButtonState()
                                    
                                    if (resultUri != null) {
                                        generatedImageUri = resultUri
                                        showGeneratedImage()
                                    } else {
                                        hideLoadingState()
                                        Toast.makeText(context, "Generation completed but no result available", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            
                            override fun onFailed(error: String) {
                                activity?.runOnUiThread {
                                    isGenerating = false
                                    updateGenerateButtonState()
                                    hideLoadingState()
                                    Toast.makeText(context, "Generation failed: $error", Toast.LENGTH_LONG).show()
                                }
                            }
                        })
                    },
                    onFailure = { exception ->
                        isGenerating = false
                        updateGenerateButtonState()
                        Toast.makeText(
                            context,
                            "Error: ${exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        hideLoadingState()
                    }
                )
            } catch (e: Exception) {
                isGenerating = false
                updateGenerateButtonState()
                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                hideLoadingState()
            }
        }
    }

    private fun showLoadingState() {
        // Show result section
        resultSection.visibility = View.VISIBLE
        
        // Show loading, hide result image and buttons
        loadingProgress.visibility = View.VISIBLE
        resultImageView.visibility = View.GONE
        downloadButton.visibility = View.GONE
        shareButton.visibility = View.GONE
        
        // Disable generate button during processing
        generateButton.isEnabled = false
        generateButton.alpha = 0.6f
        
        Toast.makeText(
            context,
            "Generating AI image with style: $selectedStyle, aspect ratio: $selectedAspectRatio",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showGeneratedImage() {
        // Hide loading
        loadingProgress.visibility = View.GONE
        
        // Show result image and buttons
        resultImageView.visibility = View.VISIBLE
        downloadButton.visibility = View.VISIBLE
        shareButton.visibility = View.VISIBLE
        
        // Load generated image
        resultImageView.setImageURI(generatedImageUri)
        
        // Re-enable generate button
        generateButton.isEnabled = true
        generateButton.alpha = 1f
        
        Toast.makeText(context, "AI image generated successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun hideLoadingState() {
        // Hide loading and result section
        loadingProgress.visibility = View.GONE
        resultSection.visibility = View.GONE
        
        // Re-enable generate button
        generateButton.isEnabled = true
        generateButton.alpha = 1f
    }

    private fun updateGenerateButtonState() {
        val hasImage = selectedImageUri != null
        val hasPrompt = promptEditText.text.toString().trim().isNotEmpty()
        val canGenerate = hasImage && hasPrompt && !isGenerating
        
        generateButton.isEnabled = canGenerate
        
        if (canGenerate) {
            generateButton.alpha = 1.0f
            generateButton.text = "Generate AI Image"
        } else if (isGenerating) {
            generateButton.alpha = 0.7f
            generateButton.text = "Generating..."
        } else {
            generateButton.alpha = 0.5f
            generateButton.text = "Generate AI Image"
        }
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

    private fun downloadGeneratedImage() {
        if (generatedImageUri == null) {
            Toast.makeText(context, "No image to download", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Create download intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(generatedImageUri, "image/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            
            // For demo purposes, just show a toast
            // In real implementation, you would save the image to gallery
            Toast.makeText(context, "Image download started", Toast.LENGTH_SHORT).show()
            
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareGeneratedImage() {
        if (generatedImageUri == null) {
            Toast.makeText(context, "No image to share", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, generatedImageUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            
            val chooser = Intent.createChooser(shareIntent, "Share AI Generated Image")
            startActivity(chooser)
            
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to share image", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(): AIImagesFragment {
            return AIImagesFragment()
        }
    }
}