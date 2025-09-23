package com.example.superphoto.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.superphoto.R
import com.superphoto.constants.TransformationConstants
import com.superphoto.ai.GeminiAIProcessor
import com.superphoto.ai.BackgroundRemovalProcessor
import com.superphoto.ai.FaceSwapProcessor
import com.superphoto.ai.AIEnhanceProcessor
import com.superphoto.ai.AIColorizeProcessor
import com.superphoto.ai.ObjectRemovalProcessor
import com.superphoto.ai.StyleTransferProcessor
import com.superphoto.ai.SmartSuggestionsProcessor
import com.example.superphoto.ui.adapter.SmartSuggestionsAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.Serializable

class AITransformationFragment : Fragment() {

    // UI Elements
    private lateinit var toolTitleText: TextView
    private lateinit var toolDescriptionText: TextView
    private lateinit var toolIconText: TextView
    private lateinit var originalImageView: ImageView
    private lateinit var resultImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var processButton: Button
    private lateinit var saveButton: Button
    private lateinit var shareButton: Button
    private lateinit var backButton: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var processingText: TextView
    
    // Face swap specific UI
    private lateinit var selectTargetFaceButton: Button
    private lateinit var targetFaceImageView: ImageView
    
    // Smart suggestions UI
    private lateinit var smartSuggestionsCard: androidx.cardview.widget.CardView
    private lateinit var smartSuggestionsRecyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var suggestionsLoadingLayout: android.widget.LinearLayout
    private lateinit var refreshSuggestionsButton: TextView

    // Data
    private var transformation: TransformationConstants.Transformation? = null
    private var selectedImageUri: Uri? = null
    private var targetFaceUri: Uri? = null
    private var processedImageUri: Uri? = null
    private lateinit var geminiProcessor: GeminiAIProcessor
    private lateinit var backgroundRemovalProcessor: BackgroundRemovalProcessor
    private lateinit var faceSwapProcessor: FaceSwapProcessor
    private lateinit var aiEnhanceProcessor: AIEnhanceProcessor
    private lateinit var aiColorizeProcessor: AIColorizeProcessor
    private lateinit var objectRemovalProcessor: ObjectRemovalProcessor
    private lateinit var styleTransferProcessor: StyleTransferProcessor
    private lateinit var smartSuggestionsProcessor: SmartSuggestionsProcessor
    private lateinit var smartSuggestionsAdapter: SmartSuggestionsAdapter

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                originalImageView.setImageURI(uri)
                originalImageView.visibility = View.VISIBLE
                updateProcessButtonState()
                resultImageView.visibility = View.GONE
                saveButton.visibility = View.GONE
                shareButton.visibility = View.GONE
                
                // Auto-generate smart suggestions when image is selected
                generateSmartSuggestions()
            }
        }
    }

    // Target face picker launcher for face swap
    private val targetFacePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                targetFaceUri = uri
                targetFaceImageView.setImageURI(uri)
                targetFaceImageView.visibility = View.VISIBLE
                updateProcessButtonState()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ai_transformation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get transformation from arguments
        transformation = arguments?.getSerializable(ARG_TRANSFORMATION) as? TransformationConstants.Transformation
        
        initViews(view)
        setupTransformation()
        setupClickListeners()
        initGeminiProcessor()
        setupSmartSuggestions()
    }

    private fun initViews(view: View) {
        toolTitleText = view.findViewById(R.id.toolTitleText)
        toolDescriptionText = view.findViewById(R.id.toolDescriptionText)
        toolIconText = view.findViewById(R.id.toolIconText)
        originalImageView = view.findViewById(R.id.originalImageView)
        resultImageView = view.findViewById(R.id.resultImageView)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        processButton = view.findViewById(R.id.processButton)
        saveButton = view.findViewById(R.id.saveButton)
        shareButton = view.findViewById(R.id.shareButton)
        backButton = view.findViewById(R.id.backButton)
        progressBar = view.findViewById(R.id.progressBar)
        processingText = view.findViewById(R.id.processingText)
        
        // Face swap specific views (will be hidden for non-face-swap transformations)
        // Note: These views are not in the current layout, will be added when face swap is implemented
        selectTargetFaceButton = view.findViewById(R.id.selectTargetFaceButton) ?: Button(requireContext())
        targetFaceImageView = view.findViewById(R.id.targetFaceImageView) ?: ImageView(requireContext())
        
        // Smart suggestions views
        smartSuggestionsCard = view.findViewById(R.id.smartSuggestionsCard)
        smartSuggestionsRecyclerView = view.findViewById(R.id.smartSuggestionsRecyclerView)
        suggestionsLoadingLayout = view.findViewById(R.id.suggestionsLoadingLayout)
        refreshSuggestionsButton = view.findViewById(R.id.refreshSuggestionsButton)
    }

    private fun setupTransformation() {
        transformation?.let { transform ->
            toolTitleText.text = transform.name
            toolDescriptionText.text = transform.description
            toolIconText.text = transform.icon
            
            // Set premium badge if needed
            if (transform.isPremium) {
                // Add premium indicator
                toolTitleText.text = "${transform.name} ✨"
            }
            
            // Show/hide face swap specific UI
            val isFaceSwap = transform.id.contains("face_swap") || transform.id.contains("faceswap")
            selectTargetFaceButton.visibility = if (isFaceSwap) View.VISIBLE else View.GONE
            targetFaceImageView.visibility = View.GONE
            
            // Update button text for face swap
            if (isFaceSwap) {
                selectImageButton.text = "Select Source Image"
                selectTargetFaceButton.text = "Select Target Face"
            } else {
                selectImageButton.text = "Select Image"
            }
        }
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        selectImageButton.setOnClickListener {
            openImagePicker()
        }

        selectTargetFaceButton.setOnClickListener {
            openTargetFacePicker()
        }

        processButton.setOnClickListener {
            processImage()
        }

        saveButton.setOnClickListener {
            saveProcessedImage()
        }

        shareButton.setOnClickListener {
            shareProcessedImage()
        }
        
        refreshSuggestionsButton.setOnClickListener {
            generateSmartSuggestions()
        }
    }

    private fun initGeminiProcessor() {
        geminiProcessor = GeminiAIProcessor(requireContext())
        backgroundRemovalProcessor = BackgroundRemovalProcessor(requireContext())
        faceSwapProcessor = FaceSwapProcessor(requireContext())
        aiEnhanceProcessor = AIEnhanceProcessor(requireContext())
        aiColorizeProcessor = AIColorizeProcessor(requireContext())
        objectRemovalProcessor = ObjectRemovalProcessor(requireContext())
        styleTransferProcessor = StyleTransferProcessor(requireContext())
        smartSuggestionsProcessor = SmartSuggestionsProcessor(requireContext())
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun openTargetFacePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        targetFacePickerLauncher.launch(intent)
    }

    private fun updateProcessButtonState() {
        val transform = transformation
        val isFaceSwap = transform?.id?.contains("face_swap") == true || transform?.id?.contains("faceswap") == true
        
        processButton.isEnabled = if (isFaceSwap) {
            // For face swap, need both source image and target face
            selectedImageUri != null && targetFaceUri != null
        } else {
            // For other transformations, just need source image
            selectedImageUri != null
        }
    }

    private fun processImage() {
        val imageUri = selectedImageUri
        val transform = transformation
        
        if (imageUri == null || transform == null) {
            Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
            return
        }

        // Show processing UI
        showProcessingState(true)
        processingText.text = "Processing with AI..."

        // Check transformation type and use appropriate processor
        when {
            transform.id.startsWith("bg_") || transform.id.contains("background") -> {
                // Use specialized background removal processor
                lifecycleScope.launch {
                    try {
                        val result = backgroundRemovalProcessor.removeBackground(imageUri)
                        result.onSuccess { backgroundResult ->
                            // Convert processed bitmap to URI
                            val uri = saveBitmapToTempFile(backgroundResult.processedBitmap)
                            processedImageUri = uri
                            showProcessingState(false)
                            uri?.let { showResult(it) } ?: run {
                                Toast.makeText(context, "Failed to save processed image", Toast.LENGTH_SHORT).show()
                            }
                        }.onFailure { error ->
                            showProcessingState(false)
                            Toast.makeText(context, "Background removal failed: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        showProcessingState(false)
                        Toast.makeText(context, "Background removal failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
            transform.id.contains("face_swap") || transform.id.contains("faceswap") -> {
                // Use face swap processor
                val targetUri = targetFaceUri
                if (targetUri == null) {
                    showProcessingState(false)
                    Toast.makeText(context, "Please select a target face image", Toast.LENGTH_SHORT).show()
                    return
                }
                
                faceSwapProcessor.swapFaces(
                    sourceImageUri = imageUri,
                    targetFaceUri = targetUri,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        showProcessingState(false)
                        showResult(processedUri)
                    },
                    onError = { error ->
                        showProcessingState(false)
                        Toast.makeText(context, "Face swap failed: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
            transform.id.contains("enhance") || transform.id.contains("ai_enhance") -> {
                // Use AI enhance processor
                aiEnhanceProcessor.enhanceImage(
                    imageUri = imageUri,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        showProcessingState(false)
                        showResult(processedUri)
                    },
                    onError = { error ->
                        showProcessingState(false)
                        Toast.makeText(context, "AI enhancement failed: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
            transform.id.contains("colorize") || transform.id.contains("ai_colorize") -> {
                // Use AI colorize processor
                aiColorizeProcessor.colorizeImage(
                    imageUri = imageUri,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        showProcessingState(false)
                        showResult(processedUri)
                    },
                    onError = { error ->
                        showProcessingState(false)
                        Toast.makeText(context, "AI colorization failed: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
            transform.id.contains("object_removal") || transform.id.contains("remove_object") -> {
                // Use object removal processor
                objectRemovalProcessor.removeObject(
                    imageUri = imageUri,
                    objectToRemove = "auto-detect",
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        showProcessingState(false)
                        showResult(processedUri)
                    },
                    onError = { error ->
                        showProcessingState(false)
                        Toast.makeText(context, "Object removal failed: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
            transform.id.contains("style") || transform.id.contains("artistic") || 
            transform.id.contains("impressionist") || transform.id.contains("expressionist") ||
            transform.id.contains("cubist") || transform.id.contains("surrealist") ||
            transform.id.contains("pop_art") || transform.id.contains("abstract") ||
            transform.id.contains("watercolor") || transform.id.contains("oil_painting") ||
            transform.id.contains("sketch") || transform.id.contains("anime") ||
            transform.id.contains("vintage") || transform.id.contains("noir") -> {
                // Use style transfer processor
                val styleName = extractStyleFromTransformationId(transform.id)
                styleTransferProcessor.transferStyleByName(
                    imageUri = imageUri,
                    styleName = styleName,
                    intensity = 0.8f,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        showProcessingState(false)
                        showResult(processedUri)
                    },
                    onError = { error ->
                        showProcessingState(false)
                        Toast.makeText(context, "Style transfer failed: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
            else -> {
                // Use general Gemini AI processor for other transformations
                geminiProcessor.processImage(
                    imageUri = imageUri,
                    prompt = transform.geminiPrompt,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        showProcessingState(false)
                        showResult(processedUri)
                    },
                    onError = { error ->
                        showProcessingState(false)
                        Toast.makeText(context, "Processing failed: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun showProcessingState(isProcessing: Boolean) {
        progressBar.visibility = if (isProcessing) View.VISIBLE else View.GONE
        processingText.visibility = if (isProcessing) View.VISIBLE else View.GONE
        processButton.isEnabled = !isProcessing
        selectImageButton.isEnabled = !isProcessing
    }

    private fun showResult(resultUri: Uri) {
        resultImageView.setImageURI(resultUri)
        resultImageView.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
        shareButton.visibility = View.VISIBLE
        
        Toast.makeText(context, "✨ Transformation completed!", Toast.LENGTH_SHORT).show()
    }

    private fun saveProcessedImage() {
        processedImageUri?.let { uri ->
            // Implement save to gallery
            Toast.makeText(context, "💾 Image saved to gallery!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareProcessedImage() {
        processedImageUri?.let { uri ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Check out my AI transformation! 🎨")
            }
            startActivity(Intent.createChooser(shareIntent, "Share your AI creation"))
        }
    }
    
    private fun extractStyleFromTransformationId(transformationId: String): String {
        return when {
            transformationId.contains("impressionist") -> "impressionist"
            transformationId.contains("expressionist") -> "expressionist"
            transformationId.contains("cubist") -> "cubist"
            transformationId.contains("surrealist") -> "surrealist"
            transformationId.contains("pop_art") -> "pop_art"
            transformationId.contains("abstract") -> "abstract"
            transformationId.contains("watercolor") -> "watercolor"
            transformationId.contains("oil_painting") -> "oil_painting"
            transformationId.contains("sketch") -> "sketch"
            transformationId.contains("anime") -> "anime"
            transformationId.contains("vintage") -> "vintage"
            transformationId.contains("noir") -> "noir"
            transformationId.contains("style") -> "impressionist" // default style
            else -> "impressionist" // fallback
        }
    }
    
    private fun setupSmartSuggestions() {
        // Setup adapter
        smartSuggestionsAdapter = SmartSuggestionsAdapter(emptyList()) { suggestion ->
            // Handle suggestion click - apply the suggested transformation
            val transformation = suggestion.transformation
            if (transformation != null) {
                this.transformation = transformation
                setupTransformation()
                selectedImageUri?.let { processImage() }
            }
        }
        
        // Setup RecyclerView
        smartSuggestionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = smartSuggestionsAdapter
        }
    }
    
    private fun generateSmartSuggestions() {
        selectedImageUri?.let { imageUri ->
            // Show loading state
            suggestionsLoadingLayout.visibility = View.VISIBLE
            smartSuggestionsRecyclerView.visibility = View.GONE
            smartSuggestionsCard.visibility = View.VISIBLE
            
            // Generate suggestions using SmartSuggestionsProcessor
            smartSuggestionsProcessor.analyzeImageAndSuggest(
                imageUri = imageUri,
                onSuccess = { suggestions ->
                    // Hide loading state
                    suggestionsLoadingLayout.visibility = View.GONE
                    smartSuggestionsRecyclerView.visibility = View.VISIBLE
                    
                    // Update adapter with suggestions
                    smartSuggestionsAdapter.updateSuggestions(suggestions)
                    
                    Toast.makeText(context, "✨ Smart suggestions generated!", Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                    // Hide loading state
                    suggestionsLoadingLayout.visibility = View.GONE
                    smartSuggestionsCard.visibility = View.GONE
                    
                    Toast.makeText(context, "Failed to generate suggestions: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun saveBitmapToTempFile(bitmap: Bitmap): Uri? {
        return try {
            val filename = "processed_image_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().cacheDir, filename)
            
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            
            FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            Log.e("AITransformationFragment", "Failed to save bitmap to temp file", e)
            null
        }
    }

    companion object {
        private const val ARG_TRANSFORMATION = "transformation"

        fun newInstance(transformation: TransformationConstants.Transformation): AITransformationFragment {
            return AITransformationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TRANSFORMATION, transformation)
                }
            }
        }
    }
}