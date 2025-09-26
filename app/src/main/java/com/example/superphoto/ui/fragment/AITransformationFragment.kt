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
import com.example.superphoto.ui.components.EnhancedLoadingManager
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.Serializable
import com.example.superphoto.utils.StorageHelper
import android.graphics.BitmapFactory

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
    
    // Enhanced Loading Manager
    private lateinit var enhancedLoadingManager: EnhancedLoadingManager

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
        
        // Initialize Enhanced Loading Manager
        enhancedLoadingManager = EnhancedLoadingManager(
            requireContext(),
            view.findViewById(R.id.mainContainer) // Assuming main container exists
        )
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
            showErrorMessage("Please select an image first")
            return
        }

        // Validate API configuration before processing
        if (!validateAPIConfiguration()) {
            showErrorMessage("API not configured. Please check your API keys in settings.")
            return
        }

        // Show enhanced loading based on transformation type
        val loadingConfig = getLoadingConfigForTransformation(transform)
        enhancedLoadingManager.show(loadingConfig)
        enhancedLoadingManager.setOnCancelListener {
            // Handle cancellation
            showProcessingState(false)
            showErrorMessage("Processing cancelled")
        }

        // Check transformation type and use appropriate processor
        when {
            transform.id.startsWith("bg_") || transform.id.contains("background") -> {
                processBackgroundTransformation(imageUri)
            }
            transform.id.contains("face_swap") || transform.id.contains("faceswap") -> {
                processFaceSwapTransformation(imageUri)
            }
            transform.id.contains("enhance") || transform.id.contains("ai_enhance") -> {
                processEnhanceTransformation(imageUri)
            }
            transform.id.contains("colorize") || transform.id.contains("ai_colorize") -> {
                processColorizeTransformation(imageUri)
            }
            transform.id.contains("object_removal") || transform.id.contains("remove_object") -> {
                processObjectRemovalTransformation(imageUri)
            }
            transform.id.contains("style") || transform.id.contains("artistic") -> {
                processStyleTransferTransformation(imageUri)
            }
            else -> {
                // Fallback to general AI processing
                processGeneralAITransformation(imageUri, transform)
            }
        }
    }

    private fun validateAPIConfiguration(): Boolean {
        return try {
            com.superphoto.config.APIConfig.isConfigured()
        } catch (e: Exception) {
            false
        }
    }

    private fun processBackgroundTransformation(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading image...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI detecting objects...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Removing background...")
                
                val result = backgroundRemovalProcessor.removeBackground(imageUri)
                result.onSuccess { backgroundResult ->
                    val uri = saveBitmapToTempFile(backgroundResult.processedBitmap)
                    processedImageUri = uri
                    
                    // Hide enhanced loading
                    enhancedLoadingManager.hide()
                    showProcessingState(false)
                    
                    uri?.let { 
                        showResult(it)
                        showSuccessMessage("Background removed successfully!")
                    } ?: run {
                        showErrorMessage("Failed to save processed image")
                    }
                }.onFailure { error ->
                    enhancedLoadingManager.hide()
                    handleProcessingError("Background removal", error)
                }
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("Background removal", e)
            }
        }
    }

    private fun processFaceSwapTransformation(imageUri: Uri) {
        val targetUri = targetFaceUri
        if (targetUri == null) {
            enhancedLoadingManager.hide()
            showProcessingState(false)
            showErrorMessage("Please select a target face image")
            return
        }
        
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading images...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI detecting faces...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Swapping faces...")
                
                faceSwapProcessor.swapFaces(
                    sourceImageUri = imageUri,
                    targetFaceUri = targetUri,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        
                        // Hide enhanced loading
                        enhancedLoadingManager.hide()
                        showProcessingState(false)
                        
                        showResult(processedUri)
                        showSuccessMessage("Face swap completed successfully!")
                    },
                    onError = { error ->
                        enhancedLoadingManager.hide()
                        handleProcessingError("Face swap", Exception(error))
                    }
                )
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("Face swap", e)
            }
        }
    }

    private fun processEnhanceTransformation(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading image...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI analyzing quality...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Enhancing image...")
                
                aiEnhanceProcessor.enhanceImage(
                    imageUri = imageUri,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        
                        // Hide enhanced loading
                        enhancedLoadingManager.hide()
                        showProcessingState(false)
                        
                        showResult(processedUri)
                        showSuccessMessage("Image enhanced successfully!")
                    },
                    onError = { error ->
                        enhancedLoadingManager.hide()
                        handleProcessingError("AI enhancement", Exception(error))
                    }
                )
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("AI enhancement", e)
            }
        }
    }

    private fun processColorizeTransformation(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading image...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI analyzing colors...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Colorizing image...")
                
                aiColorizeProcessor.colorizeImage(
                    imageUri = imageUri,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        
                        // Hide enhanced loading
                        enhancedLoadingManager.hide()
                        showProcessingState(false)
                        
                        showResult(processedUri)
                        showSuccessMessage("Image colorized successfully!")
                    },
                    onError = { error ->
                        enhancedLoadingManager.hide()
                        handleProcessingError("AI colorization", Exception(error))
                    }
                )
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("AI colorization", e)
            }
        }
    }

    private fun processObjectRemovalTransformation(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading image...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI detecting objects...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Removing objects...")
                
                objectRemovalProcessor.removeObject(
                    imageUri = imageUri,
                    objectToRemove = "auto-detect",
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        
                        // Hide enhanced loading
                        enhancedLoadingManager.hide()
                        showProcessingState(false)
                        
                        showResult(processedUri)
                        showSuccessMessage("Object removed successfully!")
                    },
                    onError = { error ->
                        enhancedLoadingManager.hide()
                        handleProcessingError("Object removal", Exception(error))
                    }
                )
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("Object removal", e)
            }
        }
    }

    private fun processStyleTransferTransformation(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading image...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI analyzing style...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Applying style...")
                
                val styleName = getStyleNameFromTransformationId(transformation?.id ?: "")
                styleTransferProcessor.transferStyleByName(
                    imageUri = imageUri,
                    styleName = styleName,
                    intensity = 0.8f,
                    onSuccess = { processedUri: Uri ->
                        processedImageUri = processedUri
                        
                        // Hide enhanced loading
                        enhancedLoadingManager.hide()
                        showProcessingState(false)
                        
                        showResult(processedUri)
                        showSuccessMessage("Style applied successfully!")
                    },
                    onError = { error: String ->
                        enhancedLoadingManager.hide()
                        handleProcessingError("Style transfer", Exception(error))
                    }
                )
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("Style transfer", e)
            }
        }
    }

    private fun processGeneralAITransformation(imageUri: Uri, transform: TransformationConstants.Transformation) {
        lifecycleScope.launch {
            try {
                // Update progress steps
                enhancedLoadingManager.updateStep(1, "Uploading image...")
                kotlinx.coroutines.delay(500)
                
                enhancedLoadingManager.updateStep(2, "AI processing...")
                kotlinx.coroutines.delay(1000)
                
                enhancedLoadingManager.updateStep(3, "Generating result...")
                
                geminiProcessor.processImage(
                    imageUri = imageUri,
                    prompt = transform.geminiPrompt,
                    onSuccess = { processedUri ->
                        processedImageUri = processedUri
                        
                        // Hide enhanced loading
                        enhancedLoadingManager.hide()
                        showProcessingState(false)
                        
                        showResult(processedUri)
                        showSuccessMessage("Transformation completed successfully!")
                    },
                    onError = { error ->
                        enhancedLoadingManager.hide()
                        handleProcessingError("AI transformation", Exception(error))
                    }
                )
            } catch (e: Exception) {
                enhancedLoadingManager.hide()
                handleProcessingError("AI transformation", e)
            }
        }
    }

    private fun handleProcessingError(operation: String, error: Throwable) {
        showProcessingState(false)
        
        val errorMessage = when {
            error.message?.contains("network", ignoreCase = true) == true -> 
                "Network error. Please check your internet connection and try again."
            error.message?.contains("api", ignoreCase = true) == true -> 
                "API error. Please check your API configuration."
            error.message?.contains("timeout", ignoreCase = true) == true -> 
                "Request timeout. Please try again with a smaller image."
            error.message?.contains("quota", ignoreCase = true) == true -> 
                "API quota exceeded. Please try again later."
            error.message?.contains("unauthorized", ignoreCase = true) == true -> 
                "Unauthorized access. Please check your API key."
            else -> "$operation failed: ${error.message ?: "Unknown error"}"
        }
        
        showErrorMessage(errorMessage)
        Log.e("AITransformationFragment", "$operation error", error)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(context, "❌ $message", Toast.LENGTH_LONG).show()
    }

    private fun showSuccessMessage(message: String) {
        Toast.makeText(context, "✅ $message", Toast.LENGTH_SHORT).show()
    }

    private fun showProcessingState(isProcessing: Boolean) {
        progressBar.visibility = if (isProcessing) View.VISIBLE else View.GONE
        processingText.visibility = if (isProcessing) View.VISIBLE else View.GONE
        processButton.isEnabled = !isProcessing
        selectImageButton.isEnabled = !isProcessing
        selectTargetFaceButton.isEnabled = !isProcessing
        
        if (isProcessing) {
            processButton.text = "Processing..."
            processButton.alpha = 0.6f
        } else {
            processButton.text = "Process Image"
            processButton.alpha = 1.0f
        }
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
            lifecycleScope.launch {
                try {
                    // Read bitmap from URI
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    
                    if (bitmap != null) {
                        // Save to external storage using StorageHelper
                        val savedFile = StorageHelper.saveImageToExternalStorage(
                            context = requireContext(),
                            bitmap = bitmap,
                            subfolder = "ai_transformations",
                            filename = "transformed_${System.currentTimeMillis()}.jpg"
                        )
                        
                        savedFile?.let { file ->
                            Toast.makeText(context, 
                                "💾 Image saved to ${file.absolutePath}", 
                                Toast.LENGTH_LONG).show()
                            Log.d("AITransformation", "Image saved successfully: ${file.absolutePath}")
                        } ?: run {
                            Toast.makeText(context, "❌ Failed to save image", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "❌ Failed to read image", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("AITransformation", "Error saving image", e)
                    Toast.makeText(context, "❌ Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
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
            // Show enhanced loading for suggestions
            val suggestionsConfig = EnhancedLoadingManager.LoadingConfig(
                title = "🤖 AI Analysis",
                message = "Analyzing your image to suggest transformations...",
                step1Text = "📤 Uploading image...",
                step2Text = "🧠 AI analyzing content...",
                step3Text = "💡 Generating suggestions...",
                estimatedTime = "10-15 seconds",
                showCancel = true
            )
            
            enhancedLoadingManager.show(suggestionsConfig)
            enhancedLoadingManager.setOnCancelListener {
                // Cancel callback - hide suggestions card
                smartSuggestionsCard.visibility = View.GONE
            }
            
            lifecycleScope.launch {
                try {
                    // Update progress steps
                    enhancedLoadingManager.updateStep(1, "Uploading image...")
                    kotlinx.coroutines.delay(500)
                    
                    enhancedLoadingManager.updateStep(2, "AI analyzing content...")
                    kotlinx.coroutines.delay(1000)
                    
                    enhancedLoadingManager.updateStep(3, "Generating suggestions...")
                    
                    // Generate suggestions using SmartSuggestionsProcessor
                    smartSuggestionsProcessor.analyzeImageAndSuggest(
                        imageUri = imageUri,
                        onSuccess = { suggestions ->
                            // Hide enhanced loading
                            enhancedLoadingManager.hide()
                            
                            // Show suggestions
                            smartSuggestionsRecyclerView.visibility = View.VISIBLE
                            smartSuggestionsCard.visibility = View.VISIBLE
                            
                            // Update adapter with suggestions
                            smartSuggestionsAdapter.updateSuggestions(suggestions)
                            
                            Toast.makeText(context, "✨ Smart suggestions generated!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { error ->
                            // Hide enhanced loading
                            enhancedLoadingManager.hide()
                            smartSuggestionsCard.visibility = View.GONE
                            
                            Toast.makeText(context, "Failed to generate suggestions: $error", Toast.LENGTH_LONG).show()
                        }
                    )
                } catch (e: Exception) {
                    enhancedLoadingManager.hide()
                    smartSuggestionsCard.visibility = View.GONE
                    Toast.makeText(context, "Error generating suggestions: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
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

    /**
     * Map transformation ID thành style name cho StyleTransferProcessor
     */
    private fun getStyleNameFromTransformationId(transformationId: String): String {
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
            else -> "impressionist" // default style
        }
    }

    /**
     * Lấy loading configuration phù hợp với loại transformation
     */
    private fun getLoadingConfigForTransformation(transform: TransformationConstants.Transformation): EnhancedLoadingManager.LoadingConfig {
        return when {
            transform.id.startsWith("bg_") || transform.id.contains("background") -> {
                EnhancedLoadingManager.backgroundRemovalConfig()
            }
            transform.id.contains("face_swap") || transform.id.contains("faceswap") -> {
                EnhancedLoadingManager.faceSwapConfig()
            }
            transform.id.contains("enhance") || transform.id.contains("ai_enhance") -> {
                EnhancedLoadingManager.aiEnhanceConfig()
            }
            transform.id.contains("colorize") || transform.id.contains("ai_colorize") -> {
                EnhancedLoadingManager.colorizeConfig()
            }
            transform.id.contains("object_removal") || transform.id.contains("remove_object") -> {
                EnhancedLoadingManager.objectRemovalConfig()
            }
            else -> {
                // Default AI processing config
                EnhancedLoadingManager.LoadingConfig(
                    title = transform.name,
                    message = "AI is processing your image...",
                    icon = transform.icon,
                    estimatedTime = "20-40 seconds"
                )
            }
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